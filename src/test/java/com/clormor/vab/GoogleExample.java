package com.clormor.vab;

import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

/**
 * <p>
 * Contains examples on how to use the web client libraries in use on this project.
 * </p>
 * 
 * @author clormor
 * 
 */
public class GoogleExample {
	
	public static void main(String [] args) throws Exception {
		htmlUnitExample();
	}
	
	static void seleniumExample() {
		// Create a new instance of the Firefox driver
		// Notice that the remainder of the code relies on the interface,
		// not the implementation.
		WebDriver driver = new FirefoxDriver();

		// And now use this to visit Google
		driver.navigate().to("http://www.google.com");

		// Find the text input element by its name
		WebElement element = driver.findElement(By.name("q"));

		// Enter something to search for
		element.sendKeys("Cheese!");

		// Now submit the form. WebDriver will find the form for us from the
		// element
		element.submit();

		// Check the title of the page
		System.out.println("Page title is: " + driver.getTitle());

		// Google's search is rendered dynamically with JavaScript.
		// Wait for the page to load, timeout after 10 seconds
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.getTitle().toLowerCase().startsWith("cheese!");
			}
		});

		// Should see: "cheese! - Google Search"
		System.out.println("Page title is: " + driver.getTitle());

		// Close the browser
		driver.quit();
	}

	static void htmlUnitExample() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		WebClient webClient = new WebClient();
		HtmlPage page = webClient.getPage("http://www.google.com");

		HtmlInput searchBox = page.getElementByName("q");
		searchBox.setValueAttribute("htmlunit");

		HtmlSubmitInput googleSearchSubmitButton = page
				.getElementByName("btnG"); // sometimes it's "btnK"
		page = googleSearchSubmitButton.click();

		HtmlDivision resultStatsDiv = page
				.getFirstByXPath("//div[@id='resultStats']");

		System.out.println(resultStatsDiv.asText()); // About 301,000 results
		webClient.closeAllWindows();
	}
}
