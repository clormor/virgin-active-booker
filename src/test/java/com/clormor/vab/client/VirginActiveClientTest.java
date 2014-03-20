package com.clormor.vab.client;

import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import com.clormor.vab.client.VirginActiveClientImpl;

public class VirginActiveClientTest {

	private VirginActiveClientImpl testCli;
	
	@Before
	public void setup() {
		testCli = new VirginActiveClientImpl();
	}
	
	@Test (expected = ParseException.class)
	public void parseNoArgs() throws ParseException {
		String[] args = {""};
		testCli.processArgs(args);
	}
	
	@Test (expected = ParseException.class)
	public void parseBookAndList() throws ParseException {
		String[] args = {"-u", "me", "-p", "whatever", "-list", "-book"};
		testCli.processArgs(args);
	}
	
	@Test (expected = ParseException.class)
	public void parseNoBookOrList() throws ParseException {
		String[] args = {"-u", "me", "-p", "whatever"};
		testCli.processArgs(args);
	}
	
}
