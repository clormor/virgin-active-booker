package com.clormor.vab.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

public class VirginActiveClientTest {

	private VirginActiveClientImpl testCli;
	
	@Before
	public void setup() {
		testCli = new TestClientImpl();
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
		String[] args = {"-u", "me", "-p", "whatever", "-l"};
		testCli.processArgs(args);
		assertEquals(testCli.getRelativeDate(),0);
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
	
	@Test
	public void helpCalledOnly() throws ParseException {
		String[] args = {"-u", "me", "-p", "whatever", "-b", "-help", "-t", "20"};
		testCli.processArgs(args);
		testCli.run();
		
		assertTrue(((TestClientImpl) testCli).helpDisplayed);
		assertFalse(((TestClientImpl) testCli).listed);
		assertFalse(((TestClientImpl) testCli).booked);
		
		String[] args2 = {"-u", "me", "-p", "whatever", "-list", "-h", "-t", "20"};
		testCli.processArgs(args2);
		testCli.run();
		
		assertTrue(((TestClientImpl) testCli).helpDisplayed);
		assertFalse(((TestClientImpl) testCli).listed);
		assertFalse(((TestClientImpl) testCli).booked);
	}

	@Test
	public void listCallsList() throws ParseException {
		String[] args = {"-u", "me", "-p", "whatever", "-l"};
		testCli.processArgs(args);
		testCli.run();
		
		assertFalse(((TestClientImpl) testCli).helpDisplayed);
		assertTrue(((TestClientImpl) testCli).listed);
		assertFalse(((TestClientImpl) testCli).booked);		
	}
	
	@Test
	public void bookCallsBook() throws ParseException {
		String[] args = {"-u", "me", "-p", "whatever", "-book", "-t", "19"};
		testCli.processArgs(args);
		testCli.run();
		
		assertFalse(((TestClientImpl) testCli).helpDisplayed);
		assertFalse(((TestClientImpl) testCli).listed);
		assertTrue(((TestClientImpl) testCli).booked);		
	}
	
	@Test (expected = ParseException.class)
	public void bookNoTime() throws ParseException {
		String[] args = {"-u", "me", "-p", "whatever", "-book"};
		testCli.processArgs(args);
	}
}
