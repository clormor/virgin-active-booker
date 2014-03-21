package com.clormor.vab.client;

import org.apache.commons.cli.ParseException;

public interface VirginActiveClient {

	void processArgs(String[] args) throws ParseException;
	
	void run();
	
	void printHelpMessage();
}
