package com.clormor.vab.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.clormor.vab.model.VirginBookingDate;
import com.clormor.vab.model.VirginConstants;
import com.clormor.vab.model.VirginModel;
import com.clormor.vab.model.VirginTennisCourt;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

public class HtmlUnitController {
	
	private final VirginModel model = new VirginModel();
	private final WebClient webClient = new WebClient();
	private HtmlPage currentPage = null;

	public HtmlUnitController() {
		webClient.getOptions().setJavaScriptEnabled(true);
	}

	HtmlRadioButtonInput getElementForBookingDate(DateTime date) {
		VirginBookingDate bookingDate = model.getBookingDate(date);
		String dateId = null;

		switch (bookingDate) {
		case TODAY:
			dateId = "rbToday";
			break;
		case TODAY_PLUS_1:
			dateId = "rbTomorrow";
			break;
		case TODAY_PLUS_2:
			dateId = "rbDay3";
			break;
		case TODAY_PLUS_3:
			dateId = "rbDay4";
			break;
		case TODAY_PLUS_4:
			dateId = "rbDay5";
			break;
		case TODAY_PLUS_5:
			dateId = "rbDay6";
			break;
		case TODAY_PLUS_6:
			dateId = "rbDay7";
			break;
		case TODAY_PLUS_7:
			dateId = "rbDay8";
			break;
		default:
			return null;
		}

		return (HtmlRadioButtonInput) currentPage.getElementById(dateId);
	}

	HtmlRadioButtonInput getElementForBookingTime(int hourOfDay) {
		String hourOfDayRadioButtonId = "rb_" + hourOfDay + "_0";
		return (HtmlRadioButtonInput) currentPage.getElementById(hourOfDayRadioButtonId);
	}

	List<VirginTennisCourt> getAvailableCourts() {
		List<VirginTennisCourt> courts = new ArrayList<VirginTennisCourt>();

		HtmlSelect courtsSelectElement = (HtmlSelect) currentPage.getElementById("alb_5");

		for (HtmlOption courtOption : courtsSelectElement.getOptions()) {
			courts.add(getCourtFromOption(courtOption));
		}

		return courts;
	}

	VirginTennisCourt getCourtFromOption(HtmlOption courtOption) {
		String courtName = courtOption.getText();
		String lastChar = courtName.substring(courtName.length() - 1,
				courtName.length());

		for (VirginTennisCourt court : VirginTennisCourt.values()) {
			if (court.getName().equalsIgnoreCase(lastChar)) {
				return court;
			}
		}
		
		throw new RuntimeException("Could not derive TennisCourt from HtmlOption: " + courtOption.getText());
	}
	
	public VirginTennisCourt bookCourt(int hourOfDay) throws IOException {
		HtmlRadioButtonInput hourOfDayButton = getElementForBookingTime(hourOfDay);
		
		if (hourOfDayButton == null) {
			return null;
		}
		
		try {
			hourOfDayButton.setChecked(true);
			System.out.println("sleeping...");
			Thread.sleep(5000);
			System.out.println("awake...");
		} catch (InterruptedException e) {
			// do nothing
		}

		//TODO figure out how to get HtmlUnit to find the dynamically-loaded select input
		currentPage = (HtmlPage) currentPage.refresh();
		HtmlSelect courtsSelectElement = (HtmlSelect) currentPage.getElementById("alb_5");

		HtmlOption bookedCourtElement = null;
		
		List<HtmlOption> bookedCourtElements = courtsSelectElement.getSelectedOptions();
		for (HtmlOption option : bookedCourtElements) {
			System.out.println("Option: " + option.getText());
			bookedCourtElement = option;
		}
		
		VirginTennisCourt result = getCourtFromOption(bookedCourtElement);
		
		
		HtmlSubmitInput proceedStep4Button = currentPage.getElementByName("rpProceed_b");
		currentPage = proceedStep4Button.click();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// do nothing
		}
		
		HtmlSubmitInput confirmButton = currentPage.getElementByName("rpProceed_b");
//		currentPage = confirmButton.click();
		return result;
	}
	
	public String printAvailableCourts(int hourOfDay) {
		StringBuilder message = new StringBuilder();
		message.append(hourOfDay).append(":00\t--> ");

		HtmlRadioButtonInput hourOfDayButton = getElementForBookingTime(hourOfDay);

		if (hourOfDayButton == null) {
			message.append("not available\n");
			return message.toString();
		}

		try {
			hourOfDayButton.setChecked(true);
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// do nothing
		}

		for (VirginTennisCourt court : getAvailableCourts()) {
			message.append(court.getName()).append(", ");
		}

		message.deleteCharAt(message.lastIndexOf(", "));
		message.append("\n");
		return message.toString();
	}

	public void login(String username, String password) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		currentPage = webClient.getPage(VirginConstants.VIRGIN_PORTAL_URL);
		
		HtmlInput usernameInput = currentPage.getElementByName("edUsername");
		usernameInput.setValueAttribute(username);

		HtmlInput passwordInput = currentPage.getElementByName("edPassword");
		passwordInput.setValueAttribute(password);
		
		final HtmlSubmitInput button =  currentPage.getElementByName("btnOK");
		currentPage = button.click();
	}

	public void logout() {
		webClient.closeAllWindows();
	}

	public void newCourtBooking(DateTime date) throws IOException {
		HtmlSubmitInput newBookingButton = currentPage.getElementByName("btnNB");
		currentPage = newBookingButton.click();

		HtmlRadioButtonInput listViewRadioButton = (HtmlRadioButtonInput) currentPage.getElementById("rbSearch");
		listViewRadioButton.setChecked(true);
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// do nothing
		}

		HtmlSubmitInput proceedStep2Button = currentPage.getElementByName("rpGoStep2_b");
		currentPage = proceedStep2Button.click();

		HtmlRadioButtonInput elementForBookingDate = getElementForBookingDate(date);
		elementForBookingDate.setChecked(true);

		HtmlSubmitInput proceedStep3Button = currentPage.getElementByName("rpProceed_b");
		currentPage = proceedStep3Button.click();
	}

}
