package com.clormor.vab.view;

import java.text.SimpleDateFormat;
import java.util.List;

import org.joda.time.DateTime;

import com.clormor.vab.controller.HtmlUnitController;
import com.clormor.vab.controller.IVirginController;
import com.clormor.vab.model.VirginConstants;
import com.clormor.vab.model.VirginTennisCourt;

public class CommandLineView {

	private final String username;
	private final String password;
	private final IVirginController controller;

	public CommandLineView(String username, String password) {
		this.username = username;
		this.password = password;
		controller = new HtmlUnitController();
	}

	public void printAvailableCourts(DateTime date) throws Exception {
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

	public void bookCourts(DateTime date, int hourOfDay, List<String> courts, List<Boolean> environments) throws Exception {
		controller.login(username, password);

		controller.newCourtBooking(date);
		
		StringBuilder message = new StringBuilder();
		VirginTennisCourt court = controller.bookCourt(hourOfDay, courts, environments);
		
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
