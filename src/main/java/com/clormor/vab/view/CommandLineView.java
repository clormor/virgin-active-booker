package com.clormor.vab.view;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;

import org.joda.time.DateTime;

import com.clormor.vab.controller.HtmlUnitController;
import com.clormor.vab.model.VirginConstants;
import com.clormor.vab.model.VirginTennisCourt;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

public class CommandLineView {

	private final String username;
	private final String password;
	private final HtmlUnitController controller;

	public CommandLineView(String username, String password) {
		this.username = username;
		this.password = password;
		controller = new HtmlUnitController();
	}

	public void printAvailableCourts(DateTime date) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		controller.login(username, password);

		controller.newCourtBooking(date);

		StringBuilder message = new StringBuilder();
		message.append(new SimpleDateFormat("EEE, MMM d").format(date.toDate()));
		message.append("\n--------------------------------\n");
		for (int hourOfDay = VirginConstants.EARLIEST_COURT_BOOKING_TIME; hourOfDay <= VirginConstants.LATEST_COURT_BOOKING_TIME; hourOfDay++) {
			message.append(controller.printAvailableCourts(hourOfDay));
		}

		controller.logout();
		System.out.println(message);
	}

	public void bookCourts(DateTime date, int hourOfDay) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		controller.login(username, password);

		controller.newCourtBooking(date);
		
		StringBuilder message = new StringBuilder();
		VirginTennisCourt court = controller.bookCourt(hourOfDay);
		
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