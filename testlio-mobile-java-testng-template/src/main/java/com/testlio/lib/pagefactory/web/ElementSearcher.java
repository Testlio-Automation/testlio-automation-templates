package com.testlio.lib.pagefactory.web;

import com.testlio.lib.properties.Execution;
import com.testlio.lib.utility.helpers.ExecutionHelper;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.NoSuchElementException;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.support.ui.ExpectedConditions.not;
import static org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

@Slf4j
public class ElementSearcher {

    private static final int MAX_STALE_ELEMENT_RETRIES = 10;

    public WebElement findElement(WebDriver driver, SearchContext searchContext, CustomAbstractAnnotations annotations,
                                  WebElement cachedElementOnFirstSearch) {

        boolean shouldCache = annotations.isLookupCached();
        By by = annotations.buildBy();

        @Nullable
        WebElement neededElement = null;

        boolean founded = false;
        int cycle = 0;

        int retryQuantity = (annotations.isNotResearchable()) ? 1 : MAX_STALE_ELEMENT_RETRIES;

        while (!founded && cycle < retryQuantity) {
            try {

                if (cachedElementOnFirstSearch != null && shouldCache) {
                    return cachedElementOnFirstSearch;
                }

                neededElement = searchContext.findElement(by);

                if (shouldCache) {
                    cachedElementOnFirstSearch = neededElement;
                }
                founded = true;
                break;
            } catch (StaleElementReferenceException e) {
                log.error(format("element is not attached to the DOM, performing search once again: %s", e.getMessage()));
                FluentWait<WebDriver> driverWait = getSearchingDriverWait(driver);
                driverWait.until(not(stalenessOf(neededElement)));
                driverWait.until(visibilityOf(neededElement));
            } catch (InvalidElementStateException e) {
                log.error("Element is not interactable...waiting for element to be interactable");
                FluentWait<WebDriver> driverWait = getSearchingDriverWait(driver);
                driverWait.until(visibilityOf(neededElement));
            } catch (UndeclaredThrowableException e) {
                log.error(
                        "Element is not interactable (UndeclaredThrowableException)...waiting for element to be interactable");
                FluentWait<WebDriver> driverWait = getSearchingDriverWait(driver);
                driverWait.until(visibilityOf(neededElement));
            }

            cycle++;
        }

        return checkNotNull(neededElement, by + " element wasn't founded");
    }

    public List<WebElement> findElements(WebDriver driver, SearchContext searchContext,
                                         CustomAbstractAnnotations annotations, List<WebElement> cachedElementsOnFirstSearch) {

        boolean shouldCache = annotations.isLookupCached();
        By by = annotations.buildBy();

        @Nullable
        List<WebElement> neededElements = null;

        boolean founded = false;
        int cycle = 0;

        int retryQuantity = (annotations.isNotResearchable()) ? 1 : MAX_STALE_ELEMENT_RETRIES;

        while (!founded && cycle < retryQuantity) {
            try {
                if (cachedElementsOnFirstSearch != null && shouldCache) {
                    return cachedElementsOnFirstSearch;
                }

                neededElements = searchContext.findElements(by);

                if (shouldCache) {
                    cachedElementsOnFirstSearch = neededElements;
                }
                founded = true;
                break;
            } catch (StaleElementReferenceException e) {
                log.error(format("element is not attached to the DOM, performing search once again %s", e.getMessage()));

                for (WebElement element : neededElements) {
                    FluentWait<WebDriver> driverWait = getSearchingDriverWait(driver);
                    driverWait.until(not(stalenessOf(element)));
                }
            } catch (UndeclaredThrowableException e) {
                log.error(format("Elements are not present for this locator: %s", by.toString()));
            }

            cycle++;
        }

        return checkNotNull(neededElements, by + " elements were not found");
    }

    public WebElement retryElementSearch(WebDriver driver, ElementLocator locator, boolean isNotResearchable,
                                         Method method) {
        @Nullable
        WebElement element = null;

        boolean founded = false;
        int cycle = 0;

        int retryQuantity = isNotResearchable ? 1 : MAX_STALE_ELEMENT_RETRIES;

        while (!founded && cycle < retryQuantity) {
            try {
                ExecutionHelper.sleepInMilliSeconds(200);
                if (method.getName().equals("findElements") || method.getName().equals("findElement")) {
                    element = locator.findElement();
                } else {
                    try {
                        if (isNotResearchable) {
                            driver.manage().timeouts().implicitlyWait(1, SECONDS);
                        }
                        element = locator.findElement();
                    } finally {
                        driver.manage().timeouts().implicitlyWait(Execution.properties().getImplicitWait(), SECONDS);
                    }
                }

                founded = true;
                break;
            } catch (StaleElementReferenceException e) {
                log.error(format("Element is not attached to the DOM, waiting for staleness %s", e.getMessage()));
                FluentWait<WebDriver> driverWait = getSearchingDriverWait(driver);
                driverWait.until(not(stalenessOf(locator.findElement())));
                driverWait.until(visibilityOf(locator.findElement()));
            } catch (InvalidElementStateException e) {
                log.error("Element is not interactable...waiting for element to be interactable");
                FluentWait<WebDriver> driverWait = getSearchingDriverWait(driver);
                driverWait.until(visibilityOf(locator.findElement()));
            } catch (UndeclaredThrowableException e) {
                log.error(
                        "Element is not interactable (UndeclaredThrowableException)...waiting for element to be interactable");
                FluentWait<WebDriver> driverWait = getSearchingDriverWait(driver);
                driverWait.until(visibilityOf(locator.findElement()));
            }
            cycle++;
        }

        return checkNotNull(element, " element wasn't founded");
    }

    public static FluentWait<WebDriver> getSearchingDriverWait(WebDriver driver) {
        return new WebDriverWait(driver, Execution.properties().getImplicitWait())
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NullPointerException.class);
    }
}