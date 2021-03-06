package com.clormor.vab.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.clormor.vab.model.VirginBookingDate;
import com.clormor.vab.model.VirginConstants;
import com.clormor.vab.model.VirginCourtBooking;
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
import com.google.common.collect.Iterables;

public class HtmlUnitController implements IVirginController {

	private final VirginModel model = new VirginModel();
	WebClient client = new WebClient(BrowserVersion.FIREFOX_24);
	
	HtmlPage currentPage = null;

	public HtmlUnitController() {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");

		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit")
				.setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient")
				.setLevel(Level.OFF);

		client.getOptions().setJavaScriptEnabled(true);
	    client.getOptions().setTimeout(60000);
	    client.getOptions().setRedirectEnabled(true);
	    client.getOptions().setJavaScriptEnabled(true);
	    client.getOptions().setThrowExceptionOnFailingStatusCode(false);
	    client.getOptions().setThrowExceptionOnScriptError(false);
	    client.getOptions().setCssEnabled(false);
	    client.getOptions().setAppletEnabled(false);
	    client.getOptions().setActiveXNative(false);
	    client.getOptions().setUseInsecureSSL(true);
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
		return (HtmlRadioButtonInput) currentPage
				.getElementById(hourOfDayRadioButtonId);
	}

	List<VirginTennisCourt> getAvailableCourts() {
		List<VirginTennisCourt> courts = new ArrayList<VirginTennisCourt>();

		HtmlSelect courtsSelectElement = (HtmlSelect) currentPage
				.getElementById("alb_5");

		for (HtmlOption courtOption : courtsSelectElement.getOptions()) {
			courts.add(getCourtFromText(courtOption.getText()));
		}

		return courts;
	}

	VirginTennisCourt getCourtFromText(final String courtName) {
		String lastChar = courtName.substring(courtName.length() - 1,
				courtName.length());

		for (VirginTennisCourt court : VirginTennisCourt.values()) {
			if (court.getName().equalsIgnoreCase(lastChar)) {
				return court;
			}
		}

		throw new RuntimeException("Could not derive TennisCourt from String: "
				+ courtName);
	}

	public VirginTennisCourt bookCourt(int hourOfDay, List<String> courts,
			List<Boolean> environments) throws IOException {

		Collection<VirginTennisCourt> potentialCourts = model
				.getMatchingCourts(courts, null, environments);

		HtmlRadioButtonInput hourOfDayButton = getElementForBookingTime(hourOfDay);

		if (hourOfDayButton == null) {
			return null;
		}

		currentPage = hourOfDayButton.click();
		waitForJavaScript();
		
		HtmlSelect courtsSelectElement = (HtmlSelect) currentPage
				.getElementById("alb_5");

		VirginTennisCourt result = null;
		List<HtmlOption> bookedCourtElements = courtsSelectElement.getOptions();
		for (HtmlOption option : bookedCourtElements) {
			VirginTennisCourt court = getCourtFromText(option.getText());
			if (potentialCourts.contains(court)) {
				result = court;
				break;
			}
		}

		HtmlSubmitInput proceedStep4Button = currentPage
				.getElementByName("rpProceed_b");
		currentPage = proceedStep4Button.click();

		HtmlSubmitInput confirmButton = currentPage
				.getElementByName("rpProceed_b");
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
		waitForJavaScript();
		
		for (VirginTennisCourt court : getAvailableCourts()) {
			message.append(court.getName()).append(", ");
		}

		message.deleteCharAt(message.lastIndexOf(", "));
		message.append("\n");
		return message.toString();
	}

	@Override
	public HtmlPage login(final String username, final String password)
			throws FailingHttpStatusCodeException, MalformedURLException,
			IOException {
		currentPage = client.getPage(VirginConstants.VIRGIN_PORTAL_URL);

		HtmlInput usernameInput = currentPage.getElementByName("edUsername");
		usernameInput.setValueAttribute(username);

		HtmlInput passwordInput = currentPage.getElementByName("edPassword");
		passwordInput.setValueAttribute(password);

		final HtmlSubmitInput button = currentPage.getElementByName("btnOK");
		return button.click();
	}

	@Override
	public void logout() {
		client.closeAllWindows();
	}

	@Override
	public void newCourtBooking(final HtmlPage currentPage, final DateTime date)
			throws IOException {
		HtmlSubmitInput newBookingButton = currentPage
				.getElementByName("btnNB");
		this.currentPage = newBookingButton.click();

		HtmlRadioButtonInput listViewRadioButton = (HtmlRadioButtonInput) this.currentPage
				.getElementById("rbSearch");
		this.currentPage = listViewRadioButton.click();

		HtmlSubmitInput proceedStep2Button = this.currentPage
				.getElementByName("rpGoStep2_b");
		this.currentPage = proceedStep2Button.click();

		HtmlRadioButtonInput elementForBookingDate = getElementForBookingDate(date);
		elementForBookingDate.setChecked(true);

		HtmlSubmitInput proceedStep3Button = this.currentPage
				.getElementByName("rpProceed_b");
		this.currentPage = proceedStep3Button.click();
	}

	@Override
	public HtmlPage myBookings(final HtmlPage currentPage) throws Exception {
		HtmlImageInput viewBookingsButton = currentPage
				.getElementByName("btnViewMy");
		return (HtmlPage) viewBookingsButton.click();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VirginCourtBooking> getAllBookings(HtmlPage myBookingsPage) {
		List<VirginCourtBooking> bookings = new ArrayList<VirginCourtBooking>();

		// retrieve all bookings in the page using an xpath query
		List<DomElement> bookingNodes = (List<DomElement>) myBookingsPage.getByXPath("/html/body/form/table/tbody/tr/td/table/tbody/tr/td[@id='tcGrid']/ul");
		if (bookingNodes == null) {
			return bookings;
		}

		for (DomElement bookingNode : bookingNodes) {
			// check the header says 'Tennis Court'
			DomElement header = Iterables.getOnlyElement((List<DomElement>)bookingNode.getByXPath("li[@class='ul_pnl_h']/span[1]"));
			if (header.getTextContent().equals("Tennis Court")) {
				
				// get the date/time
				DomElement dateNode = Iterables.getOnlyElement((List<DomElement>) bookingNode.getByXPath("li[3]/span[2]"));
				DomElement timeNode = Iterables.getOnlyElement((List<DomElement>) bookingNode.getByXPath("li[3]/span[4]"));
				String dateTime = String.format("%s %s", dateNode.getTextContent(), timeNode.getTextContent());
				DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
				DateTime bookingDate = formatter.parseDateTime(dateTime.toString());
				
				// get the court
				DomElement courtNode = Iterables.getOnlyElement((List<DomElement>) bookingNode.getByXPath("li[@class='ul_pnl_rowb']/span[2]"));
				VirginTennisCourt court = getCourtFromText(courtNode.getTextContent());
				
				// add the booking to the return list
				bookings.add(new VirginCourtBooking(court, bookingDate));
			}
		}

		return bookings;
	}
	
	/**
	 * <p>Pauses execution until JavaScript processes complete, or throws a run-time exception if it times out.</p>
	 */
	private void waitForJavaScript() {
		int maxTries = 60;
		int processesStillExecuting = 1;
		
		while (processesStillExecuting > 0 & maxTries > 0) {
			maxTries --;
			processesStillExecuting = client.waitForBackgroundJavaScript(1000);
		}
		
		if (processesStillExecuting != 0) {
			throw new RuntimeException("failed to execute java script, timing out!");
		}
	}

}
