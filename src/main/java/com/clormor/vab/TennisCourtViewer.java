package com.clormor.vab;

import java.util.List;

import org.joda.time.DateTime;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.clormor.vab.controller.VirginActiveCliBookingController;
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
		for (int hourOfDay = VirginActiveConstants.EARLIEST_COURT_BOOKING_TIME; hourOfDay <= VirginActiveConstants.LATEST_COURT_BOOKING_TIME; hourOfDay++) {
			message.append(hourOfDay).append(":00 --> ");
			List<WebElement> availableCourts = controller.getAvailableCourts(hourOfDay);
			message.append(controller.prettyPrintCourts(availableCourts));
			message.append("\n");
		}

		System.out.println(message);
		controller.logout();
	}

}
