package com.clormor.vab.model;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class VirginConstants {

	public static final int EARLIEST_COURT_BOOKING_TIME = 7;
	public static final int LATEST_COURT_BOOKING_TIME = 22;
	public static final int MAX_BOOK_AHEAD_DAY = 7;
	public static final String VIRGIN_PORTAL_URL = "https://memberportal.esporta.com/";
	public static final DateTimeZone UK_TIME_ZONE = DateTimeZone.forID("Europe/London");
	public static final DateTimeFormatter DATE_FORMATTER 
    = DateTimeFormat.forPattern("EEE, MMM d").withZone(UK_TIME_ZONE);
}
