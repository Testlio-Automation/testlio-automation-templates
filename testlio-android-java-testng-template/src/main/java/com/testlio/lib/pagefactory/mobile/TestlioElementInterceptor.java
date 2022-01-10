package com.testlio.lib.pagefactory.mobile;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Method;

import static com.testlio.lib.pagefactory.PageUtils.isElementStaleReferenceExceptionThrown;
import static com.testlio.lib.pagefactory.mobile.MobileElementResearcher.researchElement;
import static com.testlio.lib.pagefactory.mobile.TestlioThrowableUtil.extractReadableException;
import static com.testlio.lib.utility.helpers.ExecutionHelper.sleepInSeconds;

@Slf4j
public class TestlioElementInterceptor extends TestlioInterceptorOfASingleElement {

	TestlioElementInterceptor(ElementLocator locator, WebDriver driver, Boolean isNotResearchable) {
		super(locator, driver, isNotResearchable);
	}

	@Override
	protected Object getObject(ElementLocator locator, Method method, Object[] args, boolean isNotResearchable)
			throws Throwable {
		try {
			if (isNotResearchable) {
				log.info("Element is not researchable, find will be performed in 1 attempt");
				WebElement element = locator.findElement();
				return method.invoke(element, args);
			} else {
				WebElement element = MobileElementResearcher.researchElement(driver, locator);
				int attemps = 0;
				boolean invoked = false;

				while (attemps < 5 && !invoked) {
					try {
						Object invocation = method.invoke(element, args);
						invoked = true;
						return invocation;
					} catch (Exception e) {
						if (isElementStaleReferenceExceptionThrown(e)) {
							element = MobileElementResearcher.researchElement(driver, locator);
							sleepInSeconds(1);
							invoked = false;
							attemps++;
							// continue;
							// return method.invoke(element, args);
						}
						/*
						 * if (e instanceof NoSuchElementException) { element =
						 * MobileElementResearcher.researchElement(driver, locator); return
						 * method.invoke(element, args); }
						 */
					}
				}
				return method.invoke(element, args);
			}
		} catch (Throwable t) {
			throw extractReadableException(t);
		}
	}
}