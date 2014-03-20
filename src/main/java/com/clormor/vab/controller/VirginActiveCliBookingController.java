package com.clormor.vab.controller;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.clormor.vab.model.TennisBookingModel;
import com.clormor.vab.model.TennisCourt;
import com.clormor.vab.model.VirginActiveBookingDate;
import com.google.common.base.Function;

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

	public List<TennisCourt> getAvailableCourts() {
		List<TennisCourt> courts = new ArrayList<TennisCourt>();

		Select courtsSelectElement = new Select(waitForElement(By.id("alb_5")));

		for (WebElement courtElement : courtsSelectElement.getOptions()) {
			String courtName = courtElement.getText();
			String lastChar = courtName.substring(courtName.length() - 1,
					courtName.length());

			for (TennisCourt court : TennisCourt.values()) {
				if (court.getName().equalsIgnoreCase(lastChar)) {
					courts.add(court);
				}
			}
		}

		return courts;
	}

	/**
	 * Private method that acts as an arbiter of implicit timeouts of sorts..
	 * sort of like a Wait For Ajax method.
	 */
	public WebElement waitForElement(By by) {
		// times out after 2 seconds
		WebDriverWait wait = new WebDriverWait(driver, 2);

		try {
			wait.until(presenceOfElementLocated(by));
			return driver.findElement(by);
		} catch (Exception e) {
			return null;
		}
	}

	public boolean bookCourt(int hourOfDay) {
		WebElement hourOfDayButton = getElementForBookingTime(hourOfDay);
		
		if (hourOfDayButton == null) {
			return false;
		}
		
		try {
			hourOfDayButton.click();
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// do nothing
		}
		
		WebElement proceedStep4Button = waitForElement(By.id("rpProceed_b"));
		proceedStep4Button.click();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// do nothing
		}
		
		WebElement confirmButton = waitForElement(By.id("rpProceed_b"));
//		confirmButton.click();
		return true;
	}
	
	public String printAvailableCourts(int hourOfDay) {
		StringBuilder message = new StringBuilder();
		message.append(hourOfDay).append(":00\t--> ");

		WebElement hourOfDayButton = getElementForBookingTime(hourOfDay);

		if (hourOfDayButton == null) {
			message.append("not available\n");
			return message.toString();
		}

		try {
			hourOfDayButton.click();
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// do nothing
		}

		for (TennisCourt court : getAvailableCourts()) {
			message.append(court.getName()).append(", ");
		}

		message.deleteCharAt(message.lastIndexOf(", "));
		message.append("\n");
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

	private static Function<WebDriver, WebElement> presenceOfElementLocated(
			final By locator) {
		return new Function<WebDriver, WebElement>() {
			@Override
			public WebElement apply(WebDriver driver) {
				try {
					return driver.findElement(locator);
				} catch (Exception e) {
					return null;
				}
			}
		};
	}
}
