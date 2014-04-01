package com.clormor.vab.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import com.clormor.vab.controller.HtmlUnitController;
import com.clormor.vab.controller.IVirginController;
import com.clormor.vab.model.VirginConstants;
import com.clormor.vab.model.VirginCourtBooking;
import com.clormor.vab.model.VirginTennisCourt;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class CommandLineView implements ICommandLineView {

	final String username;
	final String password;
	IVirginController controller;

	public CommandLineView(String username, String password) {
		this.username = username;
		this.password = password;
		controller = new HtmlUnitController();
	}

	@Override
	public void printAvailableCourts(DateTime date) throws Exception {
		// login
		HtmlPage homePage = controller.login(username, password);

		// navigate to the court bookings page
		controller.newCourtBooking(homePage, date);

		// start building a message to print to the user
		StringBuilder message = new StringBuilder();
		message.append(new SimpleDateFormat("EEE, MMM d").format(date.toDate()));
		message.append("\n--------------------------------\n");
		
		for (int hourOfDay = VirginConstants.EARLIEST_COURT_BOOKING_TIME; hourOfDay <= VirginConstants.LATEST_COURT_BOOKING_TIME; hourOfDay++) {
			// for each available time of day, print available courts
			message.append(controller.printAvailableCourts(hourOfDay));
		}

		// logout and print the compiled message
		controller.logout();
		System.out.println(message);
	}

	@Override
	public String bookCourts(DateTime date, int hourOfDay, List<String> courts, List<Boolean> environments) throws Exception {
		// login
		HtmlPage homePage = controller.login(username, password);
		
		DateTime beginningOfDay = date.dayOfMonth().roundFloorCopy();

		// navigate to the court bookings page
		controller.newCourtBooking(homePage, beginningOfDay);

		// book a tennis court, if a suitable court is available
		VirginTennisCourt court = controller.bookCourt(hourOfDay, courts, environments);
		
		// compile a message to print to the user
		StringBuilder message = new StringBuilder();
		if (court != null) {
			message.append("Court ").append(court.getName()).append(" has been booked at ");
			message.append(hourOfDay).append(":00 on ");
			
			DateTime bookingTime = beginningOfDay.plusHours(hourOfDay);
			message.append(new SimpleDateFormat("EEE, MMM d").format(bookingTime.toDate()));
		} else {
			message.append("No courts available");
		}
		
		// logout and print the message
		controller.logout();
		return message.toString();
	}

	@Override
	public void viewBookings() throws Exception {
		// login
		HtmlPage homePage = controller.login(username, password);
		
		// navigate to to the 'my bookings' page
		HtmlPage myBookingsPage = controller.myBookings(homePage);
		
		// parse the bookings listed in the current page
		List<VirginCourtBooking> allBookings = controller.getAllBookings(myBookingsPage);
		
		// compile a message to print to the user
		StringBuilder message = new StringBuilder();
		if (allBookings != null && allBookings.size() > 0) {

			int bookingNumber = 0;
			for (VirginCourtBooking booking : allBookings) {
				Date bookingDate = booking.getBookingTime().toDate();
				message.append(++bookingNumber).append(". ");
				message.append(new SimpleDateFormat("EEE MMM d, HH:mm").format(bookingDate));
				message.append(" (Court ");
				message.append(booking.getCourt().getName());
				message.append(")\n");
			}
		} else {
			message.append("You have no bookings\n");
		}
		
		// logout and print the bookings
		controller.logout();
		System.out.println(message);
	}
}
