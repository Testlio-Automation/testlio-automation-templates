package com.testlio.driver.mobile;

import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;

import java.io.IOException;

import static java.lang.Runtime.getRuntime;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static com.testlio.lib.properties.Execution.properties;
import static com.testlio.lib.utility.functional.OptionalConsumer.of;

@Slf4j
public class MobileDriverUtils {

	private MobileDriverUtils() {}

	@Step("Reset application")
	public static void resetApp(AppiumDriver<WebElement> driver) {
		try {
			driver.resetApp();
		} catch (Exception e) {
			log.error("Error occured while resetting the app");
		}
	}

	@Step("Clearing Chrome android cache")
	public static void clearCacheInMobileChromeWithAdb() {
		if (properties().isAndroidExecution()) {
			log.info("Clearing cache on android Chrome");
			String mobileDeviceName = properties().getMobileDeviceName();
			of(ofNullable(mobileDeviceName)).ifPresent(consumer -> {
				try {
					getRuntime().exec("adb -s " + mobileDeviceName + " shell pm clear com.android.chrome");
					log.info("Chrome cache cleared");
				} catch (Exception e) {
					log.error(format("Error clear Chrome android cache - %s", e.getMessage()));
				}
			}).ifNotPresent(() -> {
				try {
					getRuntime().exec("adb shell pm clear com.android.chrome");
					log.info("Chrome cache cleared");
				} catch (IOException e) {
					log.error(format("Error clear Chrome android cache - %s", e.getMessage()));
				}
			});
		}
	}

}
