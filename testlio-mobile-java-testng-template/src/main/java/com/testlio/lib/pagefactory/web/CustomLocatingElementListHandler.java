package com.testlio.lib.pagefactory.web;

import com.testlio.lib.properties.Execution;
import com.testlio.lib.pagefactory.TestlioElementException;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.FluentWait;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.time.Duration.ofSeconds;
import static java.util.Collections.emptyList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;
import static org.openqa.selenium.support.ui.ExpectedConditions.not;
import static org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf;

@Slf4j
public class CustomLocatingElementListHandler implements InvocationHandler {

	private ElementSearcher elementSearcher = new ElementSearcher();

	private final ElementLocator locator;
	private WebDriver driver;
	private boolean isNotResearchable;

	public CustomLocatingElementListHandler(WebDriver driver, ElementLocator locator, boolean isNotResearchable) {
		this.driver = driver;
		this.locator = locator;
		this.isNotResearchable = isNotResearchable;
	}

	public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
		List<WebElement> elements = new ArrayList<>();
		try {
			if (isNotResearchable) {
				driver.manage().timeouts().implicitlyWait(1, SECONDS);
			}
			elements = locator.findElements();
			driver.manage().timeouts().implicitlyWait(Execution.properties().getImplicitWait(), SECONDS);

			// just to call simple getSize() method to check element stale
			// exception
			if (!elements.isEmpty()) {
				try {
					elements.get(0).getTagName();
				} catch (StaleElementReferenceException e1) {
					log.error(format("Element is not attached to DOM %s", elements.get(0).toString()));
					elementSearcher.retryElementSearch(driver, locator, isNotResearchable, method);
					elements = locator.findElements();
				}
			}

			try {
				return method.invoke(elements, objects);
			} catch (InvocationTargetException e) {
				// Unwrap the underlying exception
				throw e.getCause();
			}

		} catch (Exception e) {
            handleWebDriverExceptionForListOfElements(e, elements);
		}
		try {
			return method.invoke(elements, objects);
		} catch (NullPointerException e) {
			elements = locator.findElements();
			return method.invoke(elements, objects);
		}
	}

	public void handleWebDriverExceptionForListOfElements (Exception e, List<WebElement> elements) {
        if (e instanceof StaleElementReferenceException) {
            log.error("some element from element's list is not attached to DOM, performing search once again"
                    + e.getMessage());
            FluentWait<WebDriver> driverWait = ElementSearcher.getSearchingDriverWait(driver);
            try {
                if (isNotResearchable) {
                    driver.manage().timeouts().implicitlyWait(1, SECONDS);
                    for (WebElement element : elements) {
                        driverWait.withTimeout(ofSeconds(2)).pollingEvery(ofSeconds(1))
                                .until(not(stalenessOf(element)));
                    }
                } else {
                    elements = locator.findElements();
                    for (WebElement element : elements) {
                        driverWait.until(not(stalenessOf(element)));
                    }
                }
            } finally {
                driver.manage().timeouts().implicitlyWait(Execution.properties().getImplicitWait(),
                        SECONDS);
            }

        } else if (getRootCauseMessage(e)
                .contains("Right-hand side of 'instanceof' is not callable")) {
            log.error("Right-hand side of 'instanceof' is not callable appeared");
            log.error("Empty list of elements for search criteria is initiliazed");
            elements = emptyList();
        } else {
            throw new TestlioElementException(e);
        }
    }
}