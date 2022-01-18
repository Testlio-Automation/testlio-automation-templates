package com.testlio.lib.driver.mobile.local;

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

import static io.appium.java_client.remote.AndroidMobileCapabilityType.APP_ACTIVITY;
import static io.appium.java_client.remote.AndroidMobileCapabilityType.APP_PACKAGE;
import static io.appium.java_client.remote.AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS;
import static io.appium.java_client.remote.IOSMobileCapabilityType.*;
import static io.appium.java_client.remote.MobileCapabilityType.*;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;

@Slf4j
public class LocalNativeMobileProvider extends MobileDriverProvider {
	
	@SuppressWarnings("unchecked")
	@Override
	public AppiumDriver<WebElement> createDriver() {
		if (getDriver() == null) {
			DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
			if (Execution.properties().isAndroidExecution()) {
				DriverProvider.driver.set(new AndroidDriver<MobileElement>(getLocalHubRL(),
						setAndroidLocalCapabilities(desiredCapabilities)));
			} else if (Execution.properties().isIOSExecution()) {
				DriverProvider.driver.set(new IOSDriver<MobileElement>(getLocalHubRL(),
						setIOSLocalCapabilities(desiredCapabilities)));
			} else {
				String errorMessage = "Invalid mobile platform was passed for Test Droid execution";
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
	
	private DesiredCapabilities setAndroidLocalCapabilities(DesiredCapabilities desiredCapabilities) {
		desiredCapabilities.setCapability(AUTOMATION_NAME, Execution.properties().getAppiumAutomationName());
		desiredCapabilities.setCapability(PLATFORM_NAME, Execution.properties().getMobileDevicePlatform());
		desiredCapabilities.setCapability(PLATFORM_VERSION, Execution.properties().getMobileDevicePlatformVersion());
		desiredCapabilities.setCapability(DEVICE_NAME, Execution.properties().getMobileDeviceName());
		desiredCapabilities.setCapability(APP, Execution.properties().getMobileAppPath());
		desiredCapabilities.setCapability(APP_PACKAGE, Execution.properties().getMobilePackageId());
		desiredCapabilities.setCapability(APP_ACTIVITY, Execution.properties().getMobileActivity());
		desiredCapabilities.setCapability(AUTO_GRANT_PERMISSIONS, Execution.properties().isGrantAutoPermissions());
		desiredCapabilities.setCapability("newCommandTimeout", "1800");
		return desiredCapabilities;
	}
	
	private DesiredCapabilities setIOSLocalCapabilities(DesiredCapabilities desiredCapabilities) {
		desiredCapabilities.setCapability(PLATFORM_NAME, Execution.properties().getMobileDevicePlatform());
		desiredCapabilities.setCapability(PLATFORM_VERSION, Execution.properties().getMobileDevicePlatformVersion());
		desiredCapabilities.setCapability(DEVICE_NAME, Execution.properties().getMobileDeviceName());
		desiredCapabilities.setCapability(APP, Execution.properties().getMobileAppPath());
		desiredCapabilities.setCapability(UDID, Execution.properties().getMobileDeviceUdid());
		desiredCapabilities.setCapability(AUTOMATION_NAME, "XCUITest");
		desiredCapabilities.setCapability(NEW_COMMAND_TIMEOUT, "7200");
		// Optional capabilities
		desiredCapabilities.setCapability(BUNDLE_ID, Execution.properties().getAppBundleId());
		desiredCapabilities.setCapability(WEB_DRIVER_AGENT_URL, Execution.properties().getWebDriverAgentUrl());
		desiredCapabilities.setCapability(AUTO_GRANT_PERMISSIONS, Execution.properties().isGrantAutoPermissions());
		desiredCapabilities.setCapability(USE_NEW_WDA, Execution.properties().isUseNewWDA());
		desiredCapabilities.setCapability(USE_PREBUILT_WDA, Execution.properties().isUsePrebuiltWDA());
		desiredCapabilities.setCapability(NO_RESET, Execution.properties().isNoReset());
		desiredCapabilities.setCapability(FULL_RESET, Execution.properties().isFullReset());

		return desiredCapabilities;
	}
	
	private URL getLocalHubRL() {
		try {
			return new URL(Execution.properties().getAppiumServerHubAddress());
		} catch (MalformedURLException e) {
			String errorMessage = "Invalid Local hub URL";
			log.error(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}
	}
}