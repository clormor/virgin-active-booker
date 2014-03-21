package com.clormor.vab.client;

public class TestClientImpl extends VirginActiveClientImpl {

	boolean helpDisplayed = false;
	boolean listed = false;
	boolean booked = false;
	
	@Override
	public void printHelpMessage() {
		helpDisplayed = true;
	}
	
	@Override
	void listCourts() {
		listed = true;
	}
	
	@Override
	void bookCourts() {
		booked = true;
	}
}
