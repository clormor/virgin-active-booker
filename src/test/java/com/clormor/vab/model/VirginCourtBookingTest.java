package com.clormor.vab.model;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VirginCourtBookingTest {

	private VirginCourtBooking courtBooking;
	
	@Test
	public void testConstructor() {
		courtBooking = new VirginCourtBooking(VirginTennisCourt.COURT_2, DateTime.now());
	}
	
	@Test
	public void testGetCourt() {
		courtBooking = new VirginCourtBooking(VirginTennisCourt.COURT_3, DateTime.now());
		assertEquals(VirginTennisCourt.COURT_3, courtBooking.getCourt());
	}
	
	@Test
	public void testGetDate() {
		courtBooking = new VirginCourtBooking(VirginTennisCourt.COURT_3, DateTime.now().minusDays(1));
		assertEquals(DateTime.now().getDayOfYear() - 1, courtBooking.getBookingTime().getDayOfYear());
	}
}
