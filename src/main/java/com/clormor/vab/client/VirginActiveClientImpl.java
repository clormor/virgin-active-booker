package com.clormor.vab.client;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.joda.time.DateTime;

import com.clormor.vab.model.VirginActiveConstants;
import com.clormor.vab.view.TennisCourtViewer;

public class VirginActiveClientImpl implements VirginActiveClient {

	public static void main(String[] args) {
		VirginActiveClient clientImpl = new VirginActiveClientImpl();
		
		try {
			clientImpl.processArgs(args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			clientImpl.printHelpMessage();
			System.exit(1);
		}
		
		clientImpl.run();
	}

	final Options options;
	CommandLine command;
	public int date;

	public VirginActiveClientImpl() {
		options = new Options();
		
		Option book = new Option("b", "book", false, "book courts");
		Option list = new Option("l", "list", false, "list available courts");
		Option help = new Option("h", "help", false, "print this help message");

		Option time = new Option("t", "time", true, "hour of day to list or book courts (24-hour format)");
		time.setArgName("time");
		
		Option date = new Option("d", "date", true,
				"the date (relative to today's date). Must be between 0 and "
						+ VirginActiveConstants.MAX_BOOK_AHEAD_DAY
						+ " (default: 0)");
		date.setArgName("date");

		Option username = new Option("u", "username", true,
				"you member's portal username");
		username.setArgName("username");
		username.setRequired(true);

		Option password = new Option("p", "password", true,
				"you member's portal password");
		password.setArgName("password");
		password.setRequired(true);

		options.addOption(username);
		options.addOption(password);
		options.addOption(date);
		options.addOption(help);
		options.addOption(list);
		options.addOption(book);
		options.addOption(time);
	}

	public void processArgs(String[] args) throws ParseException {
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;

		cmd = parser.parse(options, args);

		if (cmd.hasOption("list") && cmd.hasOption("book")) {
			throw new ParseException("Must specify one of book or list");
		}

		if (!cmd.hasOption("list") && !cmd.hasOption("book")) {
			throw new ParseException("Must specify one of book or list");
		}
		
		if (cmd.hasOption("book") && !cmd.hasOption("time")) {
			throw new ParseException("Must specify a time to book");
		}
		
		if (cmd.hasOption("time")) {
			try {
				int hourOfDay = Integer.parseInt(cmd.getOptionValue('t'));
				if (hourOfDay < VirginActiveConstants.EARLIEST_COURT_BOOKING_TIME || hourOfDay > VirginActiveConstants.LATEST_COURT_BOOKING_TIME) {
					throw new ParseException("value for time must be between " + VirginActiveConstants.EARLIEST_COURT_BOOKING_TIME + " and " + VirginActiveConstants.LATEST_COURT_BOOKING_TIME);
				}
			}
			catch (NumberFormatException e) {
				throw new ParseException("value for time must be between " + VirginActiveConstants.EARLIEST_COURT_BOOKING_TIME + " and " + VirginActiveConstants.LATEST_COURT_BOOKING_TIME);
			}
		}

		// verify date - assign default if necessary
		if (!cmd.hasOption("d")) {
			date = 0;
		} else {
			try {
				date = Integer.parseInt(cmd.getOptionValue('d'));
				if (date < 0 || date > VirginActiveConstants.MAX_BOOK_AHEAD_DAY) {
					throw new ParseException("value for date must be a number between 0 and " + VirginActiveConstants.MAX_BOOK_AHEAD_DAY);
				}
			} catch (NumberFormatException e) {
				throw new ParseException("value for date must be a number between 0 and " + VirginActiveConstants.MAX_BOOK_AHEAD_DAY);
			}
		}

		command = cmd;
	}

	public void run() {

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
		
	}

	void listCourts() {
		String username = command.getOptionValue("username");
		String password = command.getOptionValue("password");
		TennisCourtViewer view = new TennisCourtViewer(username, password);
		view.printAvailableCourts(DateTime.now().plusDays(getRelativeDate()));
	}
	
	void bookCourts() {
		String username = command.getOptionValue("username");
		String password = command.getOptionValue("password");
		int hourOfDay = Integer.parseInt(command.getOptionValue('t'));

		TennisCourtViewer view = new TennisCourtViewer(username, password);
		view.bookCourts(DateTime.now().plusDays(getRelativeDate()), hourOfDay);
	}
	
	public void printHelpMessage() {
		HelpFormatter formatter;
		formatter = new HelpFormatter();
		formatter.printHelp("virgin-active-booker", options);
	}
	
	int getRelativeDate() {
		return date;
	}
}
