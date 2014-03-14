package com.clormor.vab.controller;

import java.util.List;

import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.clormor.vab.model.TennisBookingModel;
import com.clormor.vab.model.VirginActiveBookingDate;

public class VirginActiveCliBookingController {

	private final TennisBookingModel model;
	private final WebDriver driver;

	public VirginActiveCliBookingController(WebDriver driver) {
		this.driver = driver;
		model = new TennisBookingModel();
	}

	public WebElement getElementForBookingDate(DateTime date) {
		VirginActiveBookingDate bookingDate = model.getBookingDate(date);
		By condition = null;

		switch (bookingDate) {
		case TODAY:
			condition = By.id("rbToday");
			break;
		case TODAY_PLUS_1:
			condition = By.id("rbTomorrow");
			break;
		case TODAY_PLUS_2:
			condition = By.id("rbDay3");
			break;
		case TODAY_PLUS_3:
			condition = By.id("rbDay4");
			break;
		case TODAY_PLUS_4:
			condition = By.id("rbDay5");
			break;
		case TODAY_PLUS_5:
			condition = By.id("rbDay6");
			break;
		case TODAY_PLUS_6:
			condition = By.id("rbDay7");
			break;
		case TODAY_PLUS_7:
			condition = By.id("rbDay8");
			break;
		default:
			return null;
		}

		return waitForElement(condition);
	}

	public WebElement getElementForBookingTime(int hourOfDay) {
		String hourOfDayRadioButtonId = "rb_" + hourOfDay + "_0";
		return waitForElement(By.id(hourOfDayRadioButtonId));
	}

	public List<WebElement> getAvailableCourts(int hourOfDay) {
		WebElement hourOfDayButton = getElementForBookingTime(hourOfDay);
		hourOfDayButton.click();

		Select availableCourtsSelect = new Select(
				waitForElement(By.id("alb_5")));

		return availableCourtsSelect.getOptions();
	}

	/**
	 * Private method that acts as an arbiter of implicit timeouts of sorts..
	 * sort of like a Wait For Ajax method.
	 */
	public WebElement waitForElement(By by) {
		int attempts = 0;
		int size = driver.findElements(by).size();

		while (size == 0) {
			size = driver.findElements(by).size();
			if (attempts == 3) {
				throw new RuntimeException(String.format(
						"Could not find %s after %d seconds", by.toString(), 3));
			}
			attempts++;
			try {
				Thread.sleep(1000); // sleep for 1 second.
			} catch (Exception x) {
				throw new RuntimeException(
						"Failed due to an exception during Thread.sleep!", x);
			}
		}

		if (size > 1)
			System.err.println("WARN: There are more than 1 " + by.toString()
					+ " 's!");

		return driver.findElement(by);
	}

	public String prettyPrintCourts(List<WebElement> courts) {
		if (courts == null || courts.size() == 0) {
			return "no available courts";
		}

		StringBuilder message = new StringBuilder();

		for (WebElement court : courts) {
			String courtName = court.getText();
			message.append(courtName.substring(courtName.length() - 1,
					courtName.length()));
			message.append(", ");
		}

		message.deleteCharAt(message.lastIndexOf(", "));
		return message.toString();
	}

	public void login(String username, String password) {
		driver.get("https://memberportal.esporta.com/");

		WebElement usernameElement = waitForElement(By.name("edUsername"));
		WebElement passwordElement = waitForElement(By.name("edPassword"));
		WebElement loginButton = waitForElement(By.name("btnOK"));

		usernameElement.sendKeys(username);
		passwordElement.sendKeys(password);
		loginButton.click();
	}

	public void logout() {
		driver.quit();
	}

	public void newCourtBooking(DateTime date) {
		WebElement newBookingButton = waitForElement(By.name("btnNB"));
		newBookingButton.click();

		WebElement listViewRadioButton = waitForElement(By.id("rbSearch"));
		listViewRadioButton.click();

		WebElement proceedStep2Button = waitForElement(By.id("rpGoStep2_b"));
		proceedStep2Button.click();

		WebElement elementForBookingDate = getElementForBookingDate(date);
		elementForBookingDate.click();

		WebElement proceedStep3Button = waitForElement(By.id("rpProceed_b"));
		proceedStep3Button.click();
	}
}
