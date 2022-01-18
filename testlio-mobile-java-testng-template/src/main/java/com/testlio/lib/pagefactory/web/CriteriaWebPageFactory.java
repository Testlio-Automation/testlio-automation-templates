package com.testlio.lib.pagefactory.web;

import com.testlio.lib.properties.Execution;
import com.testlio.lib.utility.helpers.ExecutionHelper;
import com.testlio.lib.pagefactory.IPageLoadedCriteria;
import com.testlio.lib.pagefactory.WebPage;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.FluentWait;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Page Factory wrapper class used to get Page Object using Page Factory
 * approach but with criteria loading parameter that will be set in each Page
 * Object
 *
 * @author Taras.Lytvyn
 */
@Slf4j
public class CriteriaWebPageFactory extends PageFactory {

	/**
	 * should be used instead of initElements when we get the Page Factory approach
	 * with Page Objects in Web tests
	 *
	 * @param driver
	 *            remote driver
	 * @param pageClassToProxy
	 *            Page Object class
	 * @return instance of Page Object we pass to class parameter
	 */
	public static <T extends WebPage> T criteriaInitWebElements(WebDriver driver, Class<T> pageClassToProxy) {
		return criteriaInitElements(driver, pageClassToProxy);
		/*
		 * driver.manage() .timeouts() .pageLoadTimeout(
		 * Integer.valueOf(PropertyReader.loadProperty("timeout")), SECONDS);
		 */
	}

	/**
	 * should be used instead of initElements when we get the Page Factory approach
	 * with Page Objects in Web tests
	 *
	 * @param driver
	 *            remote driver
	 * @param pageClassToProxy
	 *            Page Object class
	 * @return instance of Page Object we pass to class parameter
	 */
	private static <T extends IPageLoadedCriteria> T criteriaInitElements(WebDriver driver, Class<T> pageClassToProxy) {
		T page = null;
		try {
			page = instantiatePageAndWaitLoaded(driver, pageClassToProxy);
		} catch (UnhandledAlertException e1) {
			WebBrowserAlertHandler wbah = new WebBrowserAlertHandler(driver);
			try {
				wbah.handleAlert(e1);
				page = instantiatePageAndWaitLoaded(driver, pageClassToProxy);
			} catch (UnhandledAlertException e2) {
				wbah.handleAlert(e2);
				page = instantiatePageAndWaitLoaded(driver, pageClassToProxy);
			}
		}

		return page;
	}

	public static void initElements(WebDriver driver, Object page) {
		initElements(driver, new CustomDefaultElementLocatorFactory(driver), page);
	}

	public static void initElements(WebDriver driver, ElementLocatorFactory factory, Object page) {
		final ElementLocatorFactory factoryRef = factory;
		initElements(new CustomDefaultFieldDecorator(factoryRef, driver), page);
	}

	private static <T extends IPageLoadedCriteria> T instantiatePageAndWaitLoaded(WebDriver driver,
			Class<T> pageClassToProxy) {
		T page = instantiatePage(driver, pageClassToProxy);
		initElements(driver, page);
		waitForNextPageToLoad(driver);
		waitForPageObjectCriteria(driver, page);
		return page;
	}

	/**
	 * Page Factory duplicate method because it is private in Page Factory
	 *
	 * @param driver
	 * @param pageClassToProxy
	 * @return
	 */
	private static <T> T instantiatePage(WebDriver driver, Class<T> pageClassToProxy) {
		try {
			try {
				Constructor<T> constructor = pageClassToProxy.getConstructor(WebDriver.class);
				return constructor.newInstance(driver);
			} catch (NoSuchMethodException e) {
				return pageClassToProxy.newInstance();
			}
		} catch (InstantiationException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Wait until Page is loaded in DOM
	 *
	 * @param driver
	 *            remote driver
	 */
	public static void waitForNextPageToLoad(WebDriver driver) {
		FluentWait<WebDriver> driverWait = ElementSearcher.getSearchingDriverWait(driver);

		try {
			driverWait.until(isTrue -> ((JavascriptExecutor) driver).executeScript("return document.readyState")
					.equals("complete"));
		} catch (WebDriverException e) {
			log.error("Some js was not loaded");
		}
	}

	/**
	 * Wait for page is loaded by it's loaded criteria
	 *
	 * @param driver
	 *            remote driver
	 * @param page
	 *            Page Object
	 */
	private static <T extends IPageLoadedCriteria> void waitForPageObjectCriteria(WebDriver driver, T page) {
		FluentWait<WebDriver> driverWait = ElementSearcher.getSearchingDriverWait(driver);
		// Non-Chromium browsers take few more seconds to load entire page
		if (Execution.properties().isNonChromiumExecution()) {
			ExecutionHelper.sleepInSeconds(10);
		}
		driverWait.until(page.isPageLoaded());
	}
}
