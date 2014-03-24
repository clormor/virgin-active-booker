package com.clormor.vab.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import com.clormor.vab.model.VirginBookingDate;
import com.clormor.vab.model.VirginConstants;
import com.clormor.vab.model.VirginModel;
import com.clormor.vab.model.VirginTennisCourt;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

public class HtmlUnitController implements IVirginController {
	
	static final int JS_TIMEOUT = 2000;
	private final VirginModel model = new VirginModel();
	WebClient webClient = new WebClient(BrowserVersion.FIREFOX_24);
	HtmlPage currentPage = null;

	public HtmlUnitController() {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

	    java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
	    java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		
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
	
	public VirginTennisCourt bookCourt(int hourOfDay, List<String> courts, List<Boolean> environments) throws IOException {

		Collection<VirginTennisCourt> potentialCourts = model.getMatchingCourts(courts, null, environments);
		
		HtmlRadioButtonInput hourOfDayButton = getElementForBookingTime(hourOfDay);
		
		if (hourOfDayButton == null) {
			return null;
		}
		
		currentPage = hourOfDayButton.click();
		webClient.waitForBackgroundJavaScript(JS_TIMEOUT);
		HtmlSelect courtsSelectElement = (HtmlSelect) currentPage.getElementById("alb_5");

		VirginTennisCourt result = null;
		List<HtmlOption> bookedCourtElements = courtsSelectElement.getOptions();
		for (HtmlOption option : bookedCourtElements) {
			VirginTennisCourt court = getCourtFromOption(option);
			if (potentialCourts.contains(court)) {
				result = court;
				break;
			}
		}
		
		HtmlSubmitInput proceedStep4Button = currentPage.getElementByName("rpProceed_b");
		currentPage = proceedStep4Button.click();
		
		HtmlSubmitInput confirmButton = currentPage.getElementByName("rpProceed_b");
		currentPage = confirmButton.click();
		return result;
	}
	
	public String printAvailableCourts(int hourOfDay) throws IOException {
		StringBuilder message = new StringBuilder();
		message.append(hourOfDay).append(":00\t--> ");

		HtmlRadioButtonInput hourOfDayButton = getElementForBookingTime(hourOfDay);

		if (hourOfDayButton == null) {
			message.append("No courts available\n");
			return message.toString();
		}

		currentPage = hourOfDayButton.click();
		webClient.waitForBackgroundJavaScript(JS_TIMEOUT);

		for (VirginTennisCourt court : getAvailableCourts()) {
			message.append(court.getName()).append(", ");
		}

		message.deleteCharAt(message.lastIndexOf(", "));
		message.append("\n");
		return message.toString();
	}

	@Override
	public HtmlPage login(final String username, final String password) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		currentPage = webClient.getPage(VirginConstants.VIRGIN_PORTAL_URL);
		
		HtmlInput usernameInput = currentPage.getElementByName("edUsername");
		usernameInput.setValueAttribute(username);

		HtmlInput passwordInput = currentPage.getElementByName("edPassword");
		passwordInput.setValueAttribute(password);
		
		final HtmlSubmitInput button =  currentPage.getElementByName("btnOK");
		return button.click();
	}

	@Override
	public void logout() {
		webClient.closeAllWindows();
	}

	@Override
	public void newCourtBooking(final HtmlPage currentPage, final DateTime date) throws IOException {
		HtmlSubmitInput newBookingButton = currentPage.getElementByName("btnNB");
		this.currentPage = newBookingButton.click();

		HtmlRadioButtonInput listViewRadioButton = (HtmlRadioButtonInput) currentPage.getElementById("rbSearch");
		this.currentPage = listViewRadioButton.click();
		
		HtmlSubmitInput proceedStep2Button = currentPage.getElementByName("rpGoStep2_b");
		this.currentPage = proceedStep2Button.click();

		HtmlRadioButtonInput elementForBookingDate = getElementForBookingDate(date);
		elementForBookingDate.setChecked(true);

		HtmlSubmitInput proceedStep3Button = currentPage.getElementByName("rpProceed_b");
		this.currentPage = proceedStep3Button.click();
	}

	@Override
	public HtmlPage myBookings(final HtmlPage currentPage) throws Exception {
		HtmlImageInput viewBookingsButton = currentPage.getElementByName("btnViewMy");
		return (HtmlPage) viewBookingsButton.click();
	}

}
