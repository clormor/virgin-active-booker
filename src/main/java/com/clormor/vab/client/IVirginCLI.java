package com.clormor.vab.client;

import org.apache.commons.cli.ParseException;

public interface IVirginCLI {

	void processArgs(String[] args) throws ParseException;
	
	void run() throws Exception;
	
	void printHelpMessage();
}
