package com.clormor.vab.view;

import java.util.List;

import org.joda.time.DateTime;

public interface ICommandLineView {

	String printAvailableCourts(DateTime date, List<Integer> selectedHoursOfDay) throws Exception;
	
	String bookCourts(DateTime date, int hourOfDay, List<String> courts, List<Boolean> environments) throws Exception;
	
	void viewBookings() throws Exception;
}
