package com.clormor.vab.client;

import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

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
	
	@Test
	public void defaultDate() throws ParseException {
		String[] args = {"-u", "me", "-p", "whatever", "-list"};
		testCli.processArgs(args);
		assertTrue(testCli.date == 0);
	}

	@Test (expected = ParseException.class)
	public void nonNumericDate() throws ParseException {
		String[] args = {"-u", "me", "-p", "whatever", "-list", "-d", "blah"};
		testCli.processArgs(args);
	}
	
	@Test (expected = ParseException.class)
	public void dateOutOfRange() throws ParseException {
		String[] args = {"-u", "me", "-p", "whatever", "-list", "-d", "9"};
		testCli.processArgs(args);
	}	
}
