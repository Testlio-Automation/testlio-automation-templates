package com.testlio.lib.pagefactory.mobile;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.testlio.lib.pagefactory.TestlioElementException;

import java.util.function.Supplier;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static com.testlio.lib.pagefactory.PageUtils.isElementStaleReferenceExceptionThrown;

@Slf4j
public class MobileElementResearcher {

	private MobileElementResearcher() {}

	public static WebElement researchElement(WebDriver driver, ElementLocator locator) {
        return getWebElement(driver, locator, () -> locator.findElement());
	}

	public static WebElement researchElement(WebDriver driver, By locator) {
        return getWebElement(driver, locator, () -> driver.findElement(locator));
	}

	private static <L> WebElement getWebElement(WebDriver driver, L locator, Supplier<WebElement> elementSearch) {
        WebElement neededElement = null;
        boolean founded = false;
        int cycle = 1;
        while (!founded && cycle <= 2) {
            try {
                neededElement = elementSearch.get();
                founded = true;
                break;
            } catch (Exception e) {
                handleWebDriverException(e, () -> getFluentWaitForElementToBeEnabled(driver, elementSearch));
            }
            cycle++;
        }

        return ofNullable(neededElement).orElseThrow(
                () -> new NoSuchElementException("Element " + locator + " not interactible or was not found"));
    }

	private static void handleWebDriverException(Exception e, Runnable runnableFluentWait) {
        String timeoutMessage = "Timeout waiting for presence of element";
        if (isElementStaleReferenceExceptionThrown(e)) {
            log.error(format("element is not attached to the layout, performing search once again: %s", e.getMessage()));
            try {
                runnableFluentWait.run();
            } catch (TimeoutException e1) {
                log.error(timeoutMessage);
            }
        } else if (e instanceof NoSuchElementException) {
            log.error(format("element not found in the layout, performing search once again: %s", e.getMessage()));
            try {
                runnableFluentWait.run();
            } catch (TimeoutException e1) {
                log.error(timeoutMessage);
            }
        } else if (e.getMessage().contains("NoSuchElementError")) {
            try {
                runnableFluentWait.run();
            } catch (TimeoutException e1) {
                log.error(timeoutMessage);
            }
        } else if (e.getMessage().contains("unexpected end of stream")) {
            try {
                runnableFluentWait.run();
            } catch (TimeoutException e1) {
                log.error(timeoutMessage);
            }
        } else {
            throw new TestlioElementException(e);
        }
    }

    public static void getFluentWaitForElementToBeEnabled(WebDriver driver, Supplier<WebElement> elementSearch) {
        FluentWait<WebDriver> wait = new WebDriverWait(driver, 5);
        wait.ignoring(StaleElementReferenceException.class).ignoring(NoSuchElementException.class)
                .ignoring(WebDriverException.class).until(w -> elementSearch.get().isEnabled());
    }

}