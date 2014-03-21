package com.clormor.vab.view;

import java.text.SimpleDateFormat;

import org.joda.time.DateTime;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.clormor.vab.controller.VirginActiveCliBookingController;
import com.clormor.vab.model.TennisCourt;
import com.clormor.vab.model.VirginActiveConstants;

public class TennisCourtViewer {

	private final String username;
	private final String password;
	private final VirginActiveCliBookingController controller;

	public TennisCourtViewer(String username, String password) {
		this.username = username;
		this.password = password;
		controller = new VirginActiveCliBookingController(new FirefoxDriver());
	}

	public void printAvailableCourts(DateTime date) {
		controller.login(username, password);

		controller.newCourtBooking(date);

		StringBuilder message = new StringBuilder();
		message.append(new SimpleDateFormat("EEE, MMM d").format(date.toDate()));
		message.append("\n--------------------------------\n");
		for (int hourOfDay = VirginActiveConstants.EARLIEST_COURT_BOOKING_TIME; hourOfDay <= VirginActiveConstants.LATEST_COURT_BOOKING_TIME; hourOfDay++) {
			message.append(controller.printAvailableCourts(hourOfDay));
		}

		controller.logout();
		System.out.println(message);
	}

	public void bookCourts(DateTime date, int hourOfDay) {
		controller.login(username, password);

		controller.newCourtBooking(date);
		
		StringBuilder message = new StringBuilder();
		TennisCourt court = controller.bookCourt(hourOfDay);
		
		DateTime bookingTime = date.plusHours(hourOfDay);
		if (court != null) {
			message.append("Court ").append(court.getName()).append(" has been booked at ");
			message.append(hourOfDay).append(":00 on ");
			message.append(new SimpleDateFormat("EEE, MMM d").format(bookingTime.toDate()));
		} else {
			message.append("no court available");
		}
		
		controller.logout();
		System.out.println(message);
	}
}
