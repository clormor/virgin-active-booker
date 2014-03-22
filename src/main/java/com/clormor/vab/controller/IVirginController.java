package com.clormor.vab.controller;

import java.util.List;

import org.joda.time.DateTime;

import com.clormor.vab.model.VirginTennisCourt;

public interface IVirginController {

	VirginTennisCourt bookCourt(int hourOfDay, List<Boolean> environments) throws Exception;
	
	String printAvailableCourts(int hourOfDay) throws Exception;
	
	void login(String username, String password) throws Exception;
	
	void logout() throws Exception;
	
	void newCourtBooking(DateTime date) throws Exception;
}
