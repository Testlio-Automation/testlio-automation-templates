package com.testlio.lib.driver.web.local;

import com.testlio.lib.env.BrowserTypes;
import com.testlio.lib.properties.Execution;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.safari.SafariDriver;
import com.testlio.lib.driver.web.WebDriverProvider;

import java.util.logging.Level;
import java.util.logging.Logger;

import static io.github.bonigarcia.wdm.WebDriverManager.chromedriver;
import static io.github.bonigarcia.wdm.WebDriverManager.edgedriver;
import static io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver;
import static io.github.bonigarcia.wdm.WebDriverManager.iedriver;
import static org.openqa.selenium.chrome.ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY;
import static org.openqa.selenium.chrome.ChromeOptions.CAPABILITY;
import static org.openqa.selenium.remote.CapabilityType.ACCEPT_SSL_CERTS;
import static org.openqa.selenium.remote.CapabilityType.HAS_NATIVE_EVENTS;

@Slf4j
public class LocalWebDriverProvider extends WebDriverProvider {

	@SuppressWarnings("unchecked")
	@Override
	@Step("Open browser window")
	public WebDriver createDriver() {
		if (getDriver() == null) {
			String browserName = Execution.properties().getBrowserName();
			String os = Execution.properties().returnLocalOSName();
			if (browserName.equals(BrowserTypes.CHROME.getName())) {
				chromedriver().setup();
				driver.set(new ChromeDriver(setChromeOptions()));
			} else if (browserName.equals(BrowserTypes.FIREFOX.getName())) {
				firefoxdriver().setup();
				driver.set(new FirefoxDriver(setFFOptions()));
			} else if (browserName.equals(BrowserTypes.EDGE.getName())) {
				edgedriver().setup();
				driver.set(new EdgeDriver(setEdgeOptions()));
			} else if (browserName.equals(BrowserTypes.IE.getName())) {
				log.info("Local OS: " + os);
				if (!os.contains("windows")) {
					throw new IllegalArgumentException(
							"Running Internet Explorer on non-windows system is not supported" +os );
				}
				log.info("Setting up IE driver");
				iedriver().setup();
				driver.set(new InternetExplorerDriver(setIEOptions()));
			} else {
				String errorMessage = "Invalid browsername was passed for local execution";
				log.error(errorMessage);
				throw new IllegalArgumentException(errorMessage);
			}

			deleteCookies();
			if (Execution.properties().isMaximizedScreen()) {
				maximizeBrowserScreen();
			}

            if (Execution.properties().isFullScreen()) {
                fullScreenBrowserScreen();
            }
			setImplicitTimeOut(Execution.properties().getImplicitWait());
		}

		return getCreatedDriver();
	}

	@Override
	@Step("Kill local driver instance")
	public void killDriver() {
		if (wasDriverCreated()) {
			deleteCookies();
			getCreatedDriver().quit();
		}
		driver.remove();
	}

	private ChromeOptions setChromeOptions() {
		System.setProperty(CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
		Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("enable-automation");
		options.addArguments("--headless");
		options.addArguments("--window-size=1920,1080");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-extensions");
		options.addArguments("--dns-prefetch-disable");
		options.addArguments("--disable-gpu");
    	//options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
		options.addArguments("disable-infobars");
		options.setCapability("testlio:browserGuid", "test");
		options.setCapability(CAPABILITY, options);
		options.setCapability(ACCEPT_SSL_CERTS, true);
		return options;
	}

	private FirefoxOptions setFFOptions() {
        LoggingPreferences pref = new LoggingPreferences();
        pref.enable(LogType.BROWSER, Level.OFF);
        pref.enable(LogType.CLIENT, Level.OFF);
        pref.enable(LogType.DRIVER, Level.OFF);
        pref.enable(LogType.PERFORMANCE, Level.OFF);
        pref.enable(LogType.PROFILER, Level.OFF);
        pref.enable(LogType.SERVER, Level.OFF);
        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");

		FirefoxOptions options = new FirefoxOptions();
        options.setCapability(CapabilityType.LOGGING_PREFS, pref);
		options.setCapability(ACCEPT_SSL_CERTS, true);
		//options.setCapability("w3c", true);
		//options.setCapability("seleniumVersion", properties().getSeleniumVersion());
		return options;
	}

	private EdgeOptions setEdgeOptions() {
		EdgeOptions options = new EdgeOptions();
		options.setCapability(ACCEPT_SSL_CERTS, true);
		return options;
	}

	private InternetExplorerOptions setIEOptions() {
		InternetExplorerOptions options = new InternetExplorerOptions();
		options.setCapability(ACCEPT_SSL_CERTS, true);
		options.setCapability(HAS_NATIVE_EVENTS, false);
		return options;
	}

}