package com.testlio.lib.driver.mobile.local;

import com.google.common.collect.ImmutableMap;
import com.testlio.lib.driver.DriverProvider;
import com.testlio.lib.driver.mobile.MobileDriverProvider;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

import static io.appium.java_client.remote.IOSMobileCapabilityType.SHOW_XCODE_LOG;
import static io.appium.java_client.remote.IOSMobileCapabilityType.USE_NEW_WDA;
import static io.appium.java_client.remote.IOSMobileCapabilityType.XCODE_ORG_ID;
import static io.appium.java_client.remote.IOSMobileCapabilityType.XCODE_SIGNING_ID;
import static io.appium.java_client.remote.MobileCapabilityType.AUTOMATION_NAME;
import static io.appium.java_client.remote.MobileCapabilityType.DEVICE_NAME;
import static io.appium.java_client.remote.MobileCapabilityType.PLATFORM_VERSION;
import static io.appium.java_client.remote.MobileCapabilityType.UDID;
import static org.openqa.selenium.remote.CapabilityType.BROWSER_NAME;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;
import static com.testlio.lib.properties.Execution.properties;

@Slf4j
public class LocalWebMobileProvider extends MobileDriverProvider {

	@SuppressWarnings("unchecked")
	@Override
	public AppiumDriver<WebElement> createDriver() {
		if (getDriver() == null) {
			DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

			if (properties().isAndroidExecution()) {
				DriverProvider.driver.set(new AndroidDriver<MobileElement>(getLocalHubRL(),
						setAndroidLocalWebCapabilities(desiredCapabilities)));
			} else if (properties().isIOSExecution()) {
				DriverProvider.driver.set(new IOSDriver<MobileElement>(getLocalHubRL(),
						setIOSLocalWebCapabilities(desiredCapabilities)));
			} else {
				String errorMessage = "Invalid mobile platform was passed for Test Droid execution";
				log.error(errorMessage);
				throw new IllegalArgumentException(errorMessage);
			}

			setImplicitTimeOut(properties().getImplicitWait());
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

	private DesiredCapabilities setAndroidLocalWebCapabilities(DesiredCapabilities desiredCapabilities) {
		desiredCapabilities.setCapability(BROWSER_NAME, "chrome");

		desiredCapabilities.setCapability(PLATFORM_NAME, properties().getMobileDevicePlatform());
		desiredCapabilities.setCapability(PLATFORM_VERSION, properties().getMobileDevicePlatformVersion());
		desiredCapabilities.setCapability(DEVICE_NAME, properties().getMobileDeviceName());

		desiredCapabilities.setCapability("appium:chromeOptions", ImmutableMap.of("w3c", false));
        desiredCapabilities.setCapability("newCommandTimeout", 0);

		return desiredCapabilities;
	}

	private DesiredCapabilities setIOSLocalWebCapabilities(DesiredCapabilities desiredCapabilities) {
		desiredCapabilities.setCapability(BROWSER_NAME, "safari");

		desiredCapabilities.setCapability(PLATFORM_NAME, properties().getMobileDevicePlatform());
		desiredCapabilities.setCapability(PLATFORM_VERSION, properties().getMobileDevicePlatformVersion());
		desiredCapabilities.setCapability(DEVICE_NAME, properties().getMobileDeviceName());
		desiredCapabilities.setCapability(UDID, properties().getMobileDeviceUdid());
		desiredCapabilities.setCapability(AUTOMATION_NAME, "XCUITest");
		desiredCapabilities.setCapability(SHOW_XCODE_LOG, true);
		desiredCapabilities.setCapability(USE_NEW_WDA, true);
		desiredCapabilities.setCapability(XCODE_ORG_ID, properties().getXcodeOrgId());
		desiredCapabilities.setCapability(XCODE_SIGNING_ID, properties().getXcodeSigningId());

		return desiredCapabilities;
	}

	private URL getLocalHubRL() {
		try {
			return new URL(properties().getAppiumServerHubAddress());
		} catch (MalformedURLException e) {
			String errorMessage = "Invalid Local hub URL";
			log.error(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}
	}

}