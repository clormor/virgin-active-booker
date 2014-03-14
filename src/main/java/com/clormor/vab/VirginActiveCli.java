package com.clormor.vab;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
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
		options.addOption("p", true, "your member's portal password");
		options.addOption("u", true, "your member's portal username");
		options.addOption("view", false, "list available sessions/courts");
		options.addOption("book", false, "book a session/court");
		options.addOption("help", false, "print this help message");
		options.addOption("h", false, "print this help message");
		
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

		if (! cmd.hasOption('p')) {
			printHelpMessage();
			System.exit(1);
		}
		
		if (! cmd.hasOption('u')) {
			printHelpMessage();
			System.exit(1);
		}
		
		if (cmd.hasOption("help") || cmd.hasOption('h')) {
			formatter.printHelp("virginActiveCli", options);	
		}
		
		String username = cmd.getOptionValue('u');
		String password = cmd.getOptionValue('p');
		TennisCourtViewer tcv = new TennisCourtViewer(username, password);
		tcv.printAvailableCourts(DateTime.now().plusDays(1));
	}
	
	private void printHelpMessage() {
		formatter.printHelp("virginActive", options);
	}
}
