package com.testlio.lib.driver;

import com.testlio.lib.driver.mobile.testlio.TestlioMobileProvider;
import lombok.extern.slf4j.Slf4j;
import com.testlio.lib.driver.mobile.MobileDriverProvider;
import com.testlio.lib.driver.mobile.testlio.TestlioMobileProvider;
import com.testlio.lib.driver.mobile.local.LocalNativeMobileProvider;
import com.testlio.lib.driver.mobile.local.LocalWebMobileProvider;
import com.testlio.lib.driver.web.WebDriverProvider;
import com.testlio.lib.driver.web.local.LocalWebDriverProvider;
import com.testlio.lib.driver.web.testlio.TestlioWebDriverProvider;

import static java.lang.String.format;
import static com.testlio.lib.properties.Execution.properties;

@Slf4j
public class DriverFactoryProducer {

	private static final String errorMessageOnCreation = "Wrong execution configurations provided";

	private DriverFactoryProducer() {}

	public static DriverProvider getDriverProvider() {

		log.info(format("Provider is : %s", properties().getProvider()));

		if (properties().isWebExecution()) {
			return getWebDriverProvider();
		} else if (properties().isMobileWebExecution()) {
			return getMobileWebDriverProvider();
		} else if (properties().isMobileNativeExecution()) {
			return getMobileNativeProvider();
		}
		throw new IllegalStateException(errorMessageOnCreation);
	}

	private static WebDriverProvider getWebDriverProvider() {
		if (properties().isLocalExecution()) {
			log.info("Setting local web execution params");
			return new LocalWebDriverProvider();
		} else if (properties().isTestlioExecution()) {
		    return new TestlioWebDriverProvider();
        }
		throw new IllegalStateException(errorMessageOnCreation);
	}

	private static MobileDriverProvider getMobileWebDriverProvider() {
		if (properties().isLocalExecution()) {
			log.info("Setting local mobile web browser provider");
			return new LocalWebMobileProvider();
		} else if (properties().isTestlioExecution()) {
			log.info("Setting remote mobile web browser provider");
			return new TestlioMobileProvider();
		}
		throw new IllegalStateException(errorMessageOnCreation);
	}

	private static MobileDriverProvider getMobileNativeProvider() {
		if (properties().isLocalExecution()) {
			log.info("Setting local mobile native provider");
			return new LocalNativeMobileProvider();
		} else if (properties().isTestlioExecution()) {
			log.info("Setting remote mobile native provider");
			return new TestlioMobileProvider();
		}
		throw new IllegalStateException(errorMessageOnCreation);
	}
}