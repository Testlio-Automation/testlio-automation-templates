package com.testlio.lib.driver;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;

public abstract class DriverProvider implements IDriverProvidable {

	protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

	@SuppressWarnings("unchecked")
	@Override
	public WebDriver getDriver() {
		return driver.get();
	}

	public static WebDriver getCreatedDriver() {
		if (wasDriverCreated()) {
			return driver.get();
		}
		throw new IllegalStateException("Driver was not created yet");
	}

	public static boolean wasDriverCreated() {
		return ofNullable(driver.get()).isPresent();
	}

	@Step("Set implicit timeout")
	protected void setImplicitTimeOut(int seconds) {
		getCreatedDriver().manage().timeouts().implicitlyWait(seconds, SECONDS);
	}

}