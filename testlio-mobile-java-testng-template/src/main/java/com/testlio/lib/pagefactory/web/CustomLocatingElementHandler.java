package com.testlio.lib.pagefactory.web;

import com.testlio.lib.pagefactory.TestlioElementException;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.FluentWait;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.time.Duration;
import java.util.NoSuchElementException;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

@Slf4j
public class CustomLocatingElementHandler implements InvocationHandler {

	private ElementSearcher elementSearcher = new ElementSearcher();

	private WebDriver driver;

	private final ElementLocator locator;
	private boolean isNotResearchable;

	public CustomLocatingElementHandler(WebDriver driver, ElementLocator locator, boolean isNotResearchable) {
		this.driver = driver;
		this.locator = locator;
		this.isNotResearchable = isNotResearchable;
	}

	public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
		if (method.getName().equals("toString") && (objects == null || objects.length == 0)) {
			return locator.toString();
		}

		WebElement element = elementSearcher.retryElementSearch(driver, locator, isNotResearchable, method);

		if ("getWrappedElement".equals(method.getName())) {
			return element;
		}

		try {
			// just to call simple getTagName() method to check element stale exception
			element.getTagName();
		} catch (Exception e1) {
            handleWebDriverException(e1, element, method);
		}

		// continue with research element and invoke method on it
		try {
			element = elementSearcher.retryElementSearch(driver, locator, isNotResearchable, method);
			return method.invoke(element, objects);

		} catch (Exception e2) {
            handleWebDriverException(e2, element, method);
			// Throwable
		} catch (Throwable e3) {
            handleThrowable(e3, element, method, objects);
		}
		try {
			return checkNotNull(method.invoke(checkNotNull(element, "Element was null on invoker"), objects));
		} catch (UndeclaredThrowableException e4) {
			log.error(e4.getMessage());
			throw new TestlioElementException(
					"Web driver method called via invoker throwed null - maybe element is not interactable");
		} catch (Throwable e5) {
			log.error(e5.getMessage());
			throw new TestlioElementException(
					"Web driver method called via invoker throwed null - maybe element is not interactable");
		}
	}

	public void handleWebDriverException(Exception e, WebElement element, Method method) {
        if (e instanceof NoSuchElementException) {
            log.error(format("Element not found in DOM: %s", element.toString()));
            FluentWait<WebDriver> driverWait = ElementSearcher.getSearchingDriverWait(driver);
            final WebElement elementClone = element;
            try {
                driverWait
                        .withTimeout(Duration.ofSeconds(15))
                        .pollingEvery(Duration.ofSeconds(1))
                        .until(wait -> elementClone.isEnabled());
            } catch (TimeoutException e1) {
                String messageForNoSuchElementAfterTimeout = "Repeat wait for element to be available failed.";
                throw new NoSuchElementException(messageForNoSuchElementAfterTimeout + " " + e1.getMessage());
            }
            element = elementSearcher.retryElementSearch(driver, locator, isNotResearchable, method);

        } else if (e instanceof StaleElementReferenceException) {
            log.error(format("Element is not attached to DOM %s",  element.toString()));
            element = elementSearcher.retryElementSearch(driver, locator, isNotResearchable, method);
        } else if (e instanceof InvalidElementStateException) {
            log.error(format("Element is not interactable to DOM %s", element.toString()));
            element = elementSearcher.retryElementSearch(driver, locator, isNotResearchable, method);
        } else if (e instanceof WebDriverException) {
            log.error(format("Web Driver exception %s", element.toString()));
            element = elementSearcher.retryElementSearch(driver, locator, isNotResearchable, method);
        } else if (e instanceof UndeclaredThrowableException) {
            log.error(
                    "Element is not interactable (UndeclaredThrowableException)...waiting for element to be interactable");
            FluentWait<WebDriver> driverWait = ElementSearcher.getSearchingDriverWait(driver);
            driverWait.until(visibilityOf(element));
        } else {
            throw new TestlioElementException(e);
        }
    }

    public Object handleThrowable(Throwable e, WebElement element, Method method, Object[] objects) {
        log.error(format("Exception error: %s", getRootCauseMessage(e)));
        if (getRootCauseMessage(e).contains("is not clickable at point")
                || getRootCauseMessage(e).contains("element not visible")) {
            log.error("Element is not clickable");
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
            element = elementSearcher.retryElementSearch(driver, locator, isNotResearchable, method);

            try {
                return method.invoke(element, objects);
            } catch (UndeclaredThrowableException e1) {
                throw new TestlioElementException("Element is not interactable " + e1.getMessage());
                // Throwable
            } catch (Throwable e2) {
                log.error(format("Exception error: %s", getRootCauseMessage(e2)));
                if (getRootCauseMessage(e2).contains("is not clickable at point")
                        || getRootCauseMessage(e2).contains("element not visible")) {
                    log.error("Element is not clickable still");
                    jsExecutor.executeScript("arguments[0].click();", element);
                }
            }
        }
        try {
            return element.getTagName();
        } catch (Exception e3) {
            throw new TestlioElementException(e3);
        }
    }

}
