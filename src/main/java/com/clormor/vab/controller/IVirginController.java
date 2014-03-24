package com.clormor.vab.controller;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;

import com.clormor.vab.model.VirginTennisCourt;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public interface IVirginController {

	VirginTennisCourt bookCourt(int hourOfDay, List<String> courts, List<Boolean> environments) throws Exception;
	
	String printAvailableCourts(int hourOfDay) throws Exception;
	
	HtmlPage login(final String username, final String password) throws Exception;
	
	void logout() throws Exception;
	
	void newCourtBooking(final HtmlPage currentPage, final DateTime date) throws Exception;
	
	HtmlPage myBookings(final HtmlPage currentPage) throws Exception;

	List<Pair<VirginTennisCourt, DateTime>> getAllBookings(
			HtmlPage myBookingsPage);
}
