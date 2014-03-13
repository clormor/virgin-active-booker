package com.clormor.vab.controller;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class TennisCourtSeleniumController {

	public String getDateElement(DateTime date) {
		DateTime current = DateTime.now().withTimeAtStartOfDay();
		DateTime requestedDate  = date.withTimeAtStartOfDay();
		Days daysBetween = Days.daysBetween(current, requestedDate);
		
		return "";
	}
	
}
