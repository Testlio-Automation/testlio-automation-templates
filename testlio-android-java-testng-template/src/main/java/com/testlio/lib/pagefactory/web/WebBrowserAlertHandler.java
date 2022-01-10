package com.testlio.lib.pagefactory.web;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

@Slf4j
public class WebBrowserAlertHandler {

	private WebDriver driver;

	public WebBrowserAlertHandler(WebDriver driver) {
		this.driver = driver;
	}

	public void handleAlert(WebDriverException e) {
		if (e instanceof UnhandledAlertException) {
			log.info("Alert detected - will be accepting click");
			clickOnAcceptAlert();
		}
	}

	private void clickOnAcceptAlert() {
		boolean alertClicked = false;
		int clickTries = 0;
		while (!alertClicked && clickTries < 7) {
			try {
				driver.switchTo().alert().accept();
				driver.switchTo().defaultContent();
				clickTries++;
			} catch (NoAlertPresentException e3) {
				log.info("Alert not detected again");
				driver.switchTo().defaultContent();
				alertClicked = true;
			}
		}
		if (!alertClicked) {
			throw new IllegalStateException("Alert was not closed");
		}
	}
}
