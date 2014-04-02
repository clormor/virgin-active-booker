package com.clormor.vab.client;

import org.apache.commons.cli.ParseException;

import com.clormor.vab.view.CommandLineView;

public class TestCLI extends VirginCLI {

	private boolean printedHelp;
	
	public TestCLI(String[] args, CommandLineView view) throws ParseException {
		super(args);
		this.view = view;
	}
	
	@Override
	public void printHelpMessage() {
		this.printedHelp = true;
	}
	
	public boolean helpWasPrinted() {
		return printedHelp;
	}
}
