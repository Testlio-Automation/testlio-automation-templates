package com.testlio.lib.driver.mobile.testlio;

import com.testlio.lib.driver.DriverProvider;
import com.testlio.lib.driver.mobile.MobileDriverProvider;
import com.testlio.lib.properties.Execution;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
public class TestlioMobileProvider extends MobileDriverProvider {
	
	@SuppressWarnings("unchecked")
	@Override
	public AppiumDriver<WebElement> createDriver() {
		if (getDriver() == null) {
			DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
			if (Execution.properties().isAndroidExecution()) {
				DriverProvider.driver.set(new AndroidDriver<>(getLocalHubRL(), desiredCapabilities));
			} else if (Execution.properties().isIOSExecution()) {
				DriverProvider.driver.set(new IOSDriver<MobileElement>(getLocalHubRL(), desiredCapabilities));
			} else {
				String errorMessage = "Invalid mobile platform was passed for AWS execution";
				log.error(errorMessage);
				throw new IllegalArgumentException(errorMessage);
			}
			setImplicitTimeOut(Execution.properties().getImplicitWait());
		}

		return (AppiumDriver<WebElement>) DriverProvider.getCreatedDriver();
	}
	
	@Override
	public void killDriver() {
		if (DriverProvider.wasDriverCreated()) {
			DriverProvider.getCreatedDriver().quit();
		}
		DriverProvider.driver.remove();
	}
	
	private URL getLocalHubRL() {
		try {
			return new URL("http://127.0.0.1:4723/wd/hub");
		} catch (MalformedURLException e) {
			String errorMessage = "Invalid Local hub URL";
			log.error(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}
	}
}