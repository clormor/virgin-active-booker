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

	public static void main(String[] args) throws ParseException {
		VirginActiveClient clientImpl = new VirginActiveClientImpl();
		clientImpl.processArgs(args);
		clientImpl.run();
	}

	final Options options;
	String username;
	String password;
	int date;

	public VirginActiveClientImpl() {
		options = new Options();
		options.addOption("list", false, "list available sessions/courts");
		options.addOption("book", false, "book a session/court");
		options.addOption("help", false, "print this help message");

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

		username = cmd.getOptionValue("username");
		password = cmd.getOptionValue("password");
	}

	public void run() {
		TennisCourtViewer view = new TennisCourtViewer(username, password);
		view.printAvailableCourts(DateTime.now().plusDays(1));
	}

	private void printHelpMessage() {
		HelpFormatter formatter;
		formatter = new HelpFormatter();
		formatter.printHelp("virginActive", options);
	}
}
