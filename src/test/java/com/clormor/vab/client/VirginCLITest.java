package com.clormor.vab.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.cli.ParseException;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.clormor.vab.view.CommandLineView;

public class VirginCLITest {

	@Mock
	CommandLineView view;
	
	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test (expected = ParseException.class)
	public void parseBookAndList() throws ParseException {
		String[] args = {"-u", "me", "-p", "whatever", "-list", "-book"};
		new VirginCLI(args);
	}
	
	@Test (expected = ParseException.class)
	public void parseNoBookOrList() throws ParseException {
		String[] args = {"-u", "me", "-p", "whatever"};
		new VirginCLI(args);
	}
	
	@Test
	public void defaultDate() throws Exception {
		doNothing().when(view).printAvailableCourts(any(DateTime.class));
		
		String[] args = {"-u", "me", "-p", "whatever", "-l"};
		VirginCLI cli = new TestCLI(args, view);
		
		assertEquals(cli.getRelativeDate(),0);
	}

	@Test (expected = ParseException.class)
	public void nonNumericDate() throws ParseException {
		String[] args = {"-u", "me", "-p", "whatever", "-list", "-d", "blah"};
		new VirginCLI(args);
	}
	
	@Test (expected = ParseException.class)
	public void dateOutOfRange() throws ParseException {
		String[] args = {"-u", "me", "-p", "whatever", "-list", "-d", "9"};
		new VirginCLI(args);
	}
	
	@Test
	public void helpCalledOnly() throws Exception {
		String[] args = {"-u", "me", "-p", "whatever", "-b", "-help", "-t", "20"};
		new TestCLI(args, view).run();
		
		verify(view, never()).bookCourts(any(DateTime.class), anyInt(), anyListOf(String.class), anyListOf(Boolean.class));
		
		String[] args2 = {"-u", "me", "-p", "whatever", "-list", "-h", "-t", "20"};
		new VirginCLI(args2).run();
		
		verify(view, never()).printAvailableCourts(any(DateTime.class));
	}

	@Test
	public void listCallsList() throws ParseException, Exception {
		doNothing().when(view).printAvailableCourts(any(DateTime.class));;

		String[] args = {"-u", "me", "-p", "whatever", "-l"};
		new TestCLI(args, view).run();
		
		verify(view, times(1)).printAvailableCourts(any(DateTime.class));
	}
	
	@Test
	public void bookCallsBook() throws Exception {
		when(view.bookCourts(any(DateTime.class), anyInt(), anyListOf(String.class), anyListOf(Boolean.class))).thenReturn("");
		
		String[] args = {"-u", "me", "-p", "whatever", "-book", "-t", "19"};
		
		new TestCLI(args, view).run();
		
		verify(view, times(1)).bookCourts(any(DateTime.class), anyInt(), anyListOf(String.class), anyListOf(Boolean.class));
	}
	
	@Test (expected = ParseException.class)
	public void bookNoTime() throws ParseException {
		String[] args = {"-u", "me", "-p", "whatever", "-book"};
		new VirginCLI(args);
	}
	
	@Test (expected=ParseException.class)
	public void timeOutOfRange() throws ParseException {
		String[] args = {"-u", "me", "-p", "whatever", "-book", "-t", "23"};
		new VirginCLI(args);
	}
	
	@Test (expected=ParseException.class)
	public void timeNaN() throws ParseException {
		String[] args = {"-u", "me", "-p", "whatever", "-book", "-t", "blah"};
		new VirginCLI(args);
	}

	@Test
	public void indoorOption() throws ParseException {
		String[] args = {"-u", "me", "-p", "whatever", "-book", "-t", "9", "-indoor"};
		new VirginCLI(args);
	}
	
	@Test
	public void outdoorOption() throws ParseException {
		String[] args = {"-u", "me", "-p", "whatever", "-book", "-t", "9", "-outdoor"};
		new VirginCLI(args);
	}
	
	@Test
	public void courtOption() throws ParseException {
		String[] args = {"-u", "me", "-p", "whatever", "-book", "-t", "9", "-court", "1"};
		new VirginCLI(args);
	}
	
	@Test (expected=ParseException.class)
	public void courtList() throws ParseException {
		String[] args = {"-u", "me", "-p", "whatever", "-book", "-t", "9", "-court", "1,2"};
		new VirginCLI(args);
	}
	
	@Test
	public void view() throws Exception {
		String[] args = {"-u", "me", "-p", "whatever", "-view"};
		new VirginCLI(args);
		
		String[] args2 = {"-u", "me", "-p", "whatever", "-v"};
		new VirginCLI(args2);
	}
	
	@Test (expected=ParseException.class)
	public void viewAndBook() throws ParseException {
		String[] args = {"-u", "me", "-p", "whatever", "-v", "-b", "-d", "2", "-t", "1"};
		new VirginCLI(args);
	}
	
	@Test (expected=ParseException.class)
	public void viewAndList() throws ParseException {	
		String[] args2 = {"-u", "me", "-p", "whatever", "-v", "-l", "-d", "2"};
		new VirginCLI(args2);
	}
	
	@Test
	public void viewCallsView() throws Exception {
		doNothing().when(view).viewBookings();
		
		String[] args = {"-u", "me", "-p", "whatever", "-v"};
		new TestCLI(args, view).run();
		
		verify(view, times(1)).viewBookings();
	}
	
	@Test
	public void no_args_prints_help() throws Exception {
		String[] args = {};
		TestCLI testCli = null;
		
		try {
			testCli = new TestCLI(args, view);
		} catch (Exception e) {
			assertTrue(e instanceof ParseException);
			assertNotNull(testCli);
			assertTrue(testCli.helpWasPrinted());
		}
	}
}

