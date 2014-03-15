package com.clormor.vab;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.joda.time.DateTime;

public class VirginActiveCli {

	public static void main(String[] args) {
		new VirginActiveCli(args);
	}
	
	final HelpFormatter formatter;
	final Options options;

	public VirginActiveCli(String[] args) {
		formatter = new HelpFormatter();
		options = new Options();
		options.addOption("view", false, "list available sessions/courts");
		options.addOption("book", false, "book a session/court");
		options.addOption("help", false, "print this help message");

		Option username = new Option("u", "username", true, "you member's portal username");
		username.setArgName("username");
		username.setRequired(true);

		Option password = new Option("p", "password", true, "you member's portal password");
		password.setArgName("password");
		password.setRequired(true);
		
		options.addOption(username);
		options.addOption(password);
		
		processArgs(args);
	}

	private void processArgs(String[] args) {
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;
		
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			printHelpMessage();
			System.exit(1);
		}

		if (! cmd.hasOption("password")) {
			printHelpMessage();
			System.exit(1);
		}
		
		if (! cmd.hasOption("username")) {
			printHelpMessage();
			System.exit(1);
		}
		
		if (cmd.hasOption("help")) {
			formatter.printHelp("virginActiveCli", options);	
		}
		
		String username = cmd.getOptionValue("username");
		String password = cmd.getOptionValue("password");
		TennisCourtViewer tcv = new TennisCourtViewer(username, password);
		tcv.printAvailableCourts(DateTime.now().plusDays(1));
	}
	
	private void printHelpMessage() {
		formatter.printHelp("virginActive", options);
	}
}
