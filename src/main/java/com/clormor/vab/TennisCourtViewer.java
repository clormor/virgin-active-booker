package com.clormor.vab;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class TennisCourtViewer {
	
	private final String username;
	private final String password;

	public TennisCourtViewer(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public void printAvailableCourts() {
		// Create a new instance of the Firefox driver
		// Notice that the remainder of the code relies on the interface,
		// not the implementation.
		WebDriver driver = new FirefoxDriver();

		driver.get("https://memberportal.esporta.com/");
		WebElement usernameElement = waitForElement(driver,
				By.name("edUsername"));
		WebElement passwordElement = waitForElement(driver,
				By.name("edPassword"));
		WebElement loginButton = waitForElement(driver, By.name("btnOK"));

		usernameElement.sendKeys(username);
		passwordElement.sendKeys(password);
		loginButton.click();

		WebElement newBookingButton = waitForElement(driver, By.name("btnNB"));
		newBookingButton.click();

		WebElement listViewRadioButton = waitForElement(driver,
				By.id("rbSearch"));
		listViewRadioButton.click();

		WebElement proceedStep2Button = waitForElement(driver,
				By.id("rpGoStep2_b"));
		proceedStep2Button.click();

		WebElement day8RadioButton = waitForElement(driver, By.id("rbDay8"));
		day8RadioButton.click();

		WebElement proceedStep3Button = waitForElement(driver,
				By.id("rpProceed_b"));
		proceedStep3Button.click();

		WebElement _6pmRadioButton = waitForElement(driver, By.id("rb_11_0"));
		_6pmRadioButton.click();

		Select availableCourtsSelect = new Select(waitForElement(driver,
				By.id("alb_5")));

		List<WebElement> availableCourtElements = availableCourtsSelect
				.getOptions();
		for (WebElement availableCourt : availableCourtElements) {
			System.out.println(availableCourt.getText() + " is available");
		}

		driver.quit();
	}

	/**
	 * Private method that acts as an arbiter of implicit timeouts of sorts..
	 * sort of like a Wait For Ajax method.
	 */
	private static WebElement waitForElement(WebDriver driver, By by) {
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
}
