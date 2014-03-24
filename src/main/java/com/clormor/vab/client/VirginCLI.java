package com.clormor.vab.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.joda.time.DateTime;

import com.clormor.vab.model.VirginConstants;
import com.clormor.vab.view.CommandLineView;

public class VirginCLI implements Runnable {

	CommandLineView view;

	public static void main(String[] args) throws Exception {
		VirginCLI clientImpl = null;

		try {
			clientImpl = new VirginCLI(args);
			clientImpl.run();
		} catch (Exception e) {
			if (clientImpl != null) {
				clientImpl.printHelpMessage();
			} else {
				e.printStackTrace();
			}
			System.exit(1);
		}

	}

	final Options options;
	CommandLine command;

	public VirginCLI(String[] args) throws ParseException {
		options = new Options();

		Option indoor = new Option("indoor",
				"match any indoor courts (booking)");
		Option outdoor = new Option("outdoor",
				"match any outdoor courts (booking)");
		Option book = new Option("b", "book", false, "book courts");
		Option list = new Option("l", "list", false, "list available courts");
		Option viewOption = new Option("v", "view", false, "view my bookings");
		Option help = new Option("h", "help", false, "print this help message");

		Option court = new Option("court", true, "specify which court to book");
		court.setArgName("court");

		Option time = new Option("t", "time", true,
				"hour of day to list or book courts (24-hour format)");
		time.setArgName("time");

		Option date = new Option("d", "date", true,
				"the date (relative to today's date). Must be between 0 and "
						+ VirginConstants.MAX_BOOK_AHEAD_DAY + " (default: 0)");
		date.setArgName("date");

		Option username = new Option("u", "username", true,
				"your member's portal username");
		username.setArgName("username");

		Option password = new Option("p", "password", true,
				"your member's portal password");
		password.setArgName("password");

		options.addOption(username);
		options.addOption(password);
		options.addOption(date);
		options.addOption(help);
		options.addOption(list);
		options.addOption(book);
		options.addOption(viewOption);
		options.addOption(time);
		options.addOption(indoor);
		options.addOption(outdoor);
		options.addOption(court);

		command = processArgs(args);
		view = new CommandLineView(command.getOptionValue("u"),
				command.getOptionValue("p"));
	}

	CommandLine processArgs(String[] args) throws ParseException {
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = parser.parse(options, args);

		// if help requested, ignore all other arguments
		if (cmd.hasOption("help")) {
			return cmd;
		}

		// check only one of the required actions is requested
		int requestedActions = 0;
		String[] actionOptions = new String[] { "l", "b", "v" };
		for (String option : actionOptions) {
			if (cmd.hasOption(option)) {
				requestedActions++;
			}
		}

		if (requestedActions != 1) {
			throw new ParseException("Must specify one of view, list or book");
		}

		if (cmd.hasOption("book") && !cmd.hasOption("time")) {
			throw new ParseException("Must specify a time to book");
		}

		if (cmd.hasOption("court")) {
			String court = cmd.getOptionValue("court");
			if (court.length() > 1 || court.contains("-")
					|| court.contains(",") || court.contains(" ")) {
				throw new ParseException(
						"Specify a single court only e.g. 'a', 'B', or '5'");
			}
		}

		if (cmd.hasOption("time")) {
			try {
				int hourOfDay = Integer.parseInt(cmd.getOptionValue('t'));
				if (hourOfDay < VirginConstants.EARLIEST_COURT_BOOKING_TIME
						|| hourOfDay > VirginConstants.LATEST_COURT_BOOKING_TIME) {
					throw new ParseException("value for time must be between "
							+ VirginConstants.EARLIEST_COURT_BOOKING_TIME
							+ " and "
							+ VirginConstants.LATEST_COURT_BOOKING_TIME);
				}
			} catch (NumberFormatException e) {
				throw new ParseException("value for time must be between "
						+ VirginConstants.EARLIEST_COURT_BOOKING_TIME + " and "
						+ VirginConstants.LATEST_COURT_BOOKING_TIME);
			}
		}

		// verify date - assign default if necessary
		if (cmd.hasOption("d")) {
			try {
				int date = Integer.parseInt(cmd.getOptionValue('d'));
				if (date < 0 || date > VirginConstants.MAX_BOOK_AHEAD_DAY) {
					throw new ParseException(
							"value for date must be a number between 0 and "
									+ VirginConstants.MAX_BOOK_AHEAD_DAY);
				}
			} catch (NumberFormatException e) {
				throw new ParseException(
						"value for date must be a number between 0 and "
								+ VirginConstants.MAX_BOOK_AHEAD_DAY);
			}
		}

		return cmd;
	}

	@Override
	public void run() {

		try {
			if (command.hasOption("help")) {
				printHelpMessage();
				return;
			}

			if (command.hasOption("book")) {
				bookCourts();
			}

			if (command.hasOption("list")) {
				listCourts();
			}
			
			if (command.hasOption("view")) {
				viewBookings();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	void listCourts() throws Exception {
		view.printAvailableCourts(DateTime.now().plusDays(getRelativeDate()));
	}
	
	void viewBookings() throws Exception {
		view.viewBookings();
	}

	void bookCourts() throws Exception {
		List<Boolean> environments = new ArrayList<Boolean>();
		List<String> courts = new ArrayList<String>();

		int hourOfDay = Integer.parseInt(command.getOptionValue('t'));

		if (command.hasOption("indoor")) {
			environments.add(true);
		}

		if (command.hasOption("outdoor")) {
			environments.add(false);
		}

		if (command.hasOption("court")) {
			courts.add(command.getOptionValue("court"));
		}

		view.bookCourts(DateTime.now().plusDays(getRelativeDate()), hourOfDay,
				courts, environments);
	}

	public void printHelpMessage() {
		HelpFormatter formatter;
		formatter = new HelpFormatter();
		formatter.printHelp("virgin-active-booker", options);
	}

	int getRelativeDate() {
		return Integer.parseInt(command.getOptionValue('d', "0"));
	}
}
