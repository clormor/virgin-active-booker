package com.clormor.vab.model;

import org.joda.time.DateTime;

public class VirginCourtBooking {

	private final VirginTennisCourt court;
	private final DateTime bookingTime;
	
	public VirginCourtBooking(VirginTennisCourt court, DateTime bookingTime) {
		this.court = court;
		this.bookingTime = bookingTime;
	}
	
	public VirginTennisCourt getCourt() {
		return court;
	}
	
	public DateTime getBookingTime() {
		return bookingTime;
	}
}
