package com.testlio.lib.pagefactory;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Set;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static com.testlio.lib.properties.Execution.properties;
import static com.testlio.lib.utility.helpers.ExecutionHelper.sleepInSeconds;

@Slf4j
public abstract class WebPage extends Page {

    private final UrlValidator urlValidator = new UrlValidator();

    public WebPage(WebDriver driver) {
        super(driver);
    }

    @Step("Navigating to: {url}")
    public <V extends WebPage> V navigateTo(String url, Class<V> expectedClazz) {
        try {
            log.info("Navigating to: " + url);
            if (urlValidator.isValid(url)) {
                getDriver().get(url);
                return TestlioPageFactory.initWebElements(getDriver(), expectedClazz);
            } else {
                String text = format("Could not load {%s} because it's not a valid url", url);
                log.error(text);
                throw new IllegalArgumentException(text);
            }
        } catch (Throwable e) {
            log.error(format("Could navigate to {%s} and load {%s} due to exception {%s}",
                    url, expectedClazz, e.getLocalizedMessage()));
            e.printStackTrace();
            throw e;
        }
    }

    @Step("JS click on web element")
    public void jsClickOnWebElement(WebElement webElement) {
        log.info(format("Clicking on web element %s", webElement.toString()));
        getJSExecutor().executeScript("arguments[0].scrollIntoView();", webElement);
        getJSExecutor().executeScript("arguments[0].click();", webElement);
    }

    @Step("Click on web element by xpath")
    public void clickElementByXPath(By xpath) {
        clickOnElement(getDriver().findElement(xpath));
    }

    @Step("JS get element text")
    public String jsWebElementText(WebElement webElement) {
        return (String) getJSExecutor().executeScript("return arguments[0].textContent", webElement);
    }

    @Step("JS get attribute text")
    public String jsWebElementAttribute(WebElement webElement, String attribute) {
        return (String) getJSExecutor().executeScript("return arguments[0].getAttribute('" + attribute + "')",
                webElement);
    }

    @Step("JS Check page is loaded")
    public boolean jsIsPageLoaded() {
        try {
            getWebDriverWait(properties().getImplicitWait())
                    .until(wait -> getJSExecutor().executeScript("return document.readyState").equals("complete"));
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

    @Step("Scroll to web element")
    public void scrollToWebElement(WebElement webElement) {
        log.info(format("Scrolling to web element %s", webElement.toString()));
        getJSExecutor().executeScript("arguments[0].scrollIntoView(true);", webElement);
    }

    @Step(value = "Switch to newly opened window")
    public <V extends WebPage> V switchToWindow(Class<V> clazz) {
        getWebDriverWait(properties().getImplicitWait()).until(wait -> getOpenedWindowsQuanity() > 1);
        switchToOpenedWindow();
        return TestlioPageFactory.initWebElements(getDriver(), clazz);
    }

    @Step(value = "Switch to window")
    public void switchToOpenedWindow() {
        String currentWindowSession = getDriver().getWindowHandle();
        log.info("Current window session: " + currentWindowSession);
        Set<String> windowsSessions = getWindowHadles();
        log.info("Windows sessions available: " + windowsSessions);
        for (String winHandle : windowsSessions) {
            if (!winHandle.equals(currentWindowSession)) {
                switchToWindow(winHandle);
            }
        }
    }

    private void switchToWindow(String winHandle) {
        log.info("Switching to window session: " + winHandle);
        try {
            getDriver().switchTo().window(String.valueOf(winHandle));
        } catch (Exception e) {
            log.info("Exception occurred during switching to new window: " + e.getLocalizedMessage());
        }
        String newCurrentWindowSession = getDriver().getWindowHandle();
        log.info("New current window session after switch: " + newCurrentWindowSession);
    }

    @Step(value = "Switch to new window if exists")
    public void switchToNewlyOpenedWindowIfItWasOpened(Set<String> currentOpenedWindows) {
        String currentWindow = getDriver().getWindowHandle();
        try {
            log.info("Waiting for new window to be opened");
            getWebDriverWait(3).until(wait -> getOpenedWindowsQuanity() > currentOpenedWindows.size());
            log.info("New window was opened. Will switch to it");
            Set<String> currentOpenedWindowsAfterAction = getDriver().getWindowHandles();
            currentOpenedWindowsAfterAction.removeAll(currentOpenedWindows);
            for (String winHandle : currentOpenedWindowsAfterAction) {
                if (!winHandle.equals(currentWindow)) {
                    getDriver().switchTo().window(winHandle);
                }
            }
        } catch (Exception e) {
            log.info("Now new window detected. Stay on the same opened window");
        }
    }

    @Step(value = "Close opened window")
    public <V extends WebPage> V closeCurrentOpenedWindow(Class<V> clazz) {
        if (getOpenedWindowsQuanity() > 1) {
            String currentWindow = getDriver().getWindowHandle();
            for (String winHandle : getWindowHadles()) {
                if (!winHandle.equals(currentWindow)) {
                    getDriver().close();
                    getDriver().switchTo().window(winHandle);
                }
            }
        }
        return TestlioPageFactory.initWebElements(getDriver(), clazz);
    }

    @Step(value = "Navigate back")
    public <V extends WebPage> V navigateBack(Class<V> expectedClazz) {
        getDriver().navigate().back();
        return TestlioPageFactory.initWebElements(getDriver(), expectedClazz);
    }

    @Step(value = "Navigate back {timesToNavigateBack} times")
    public <V extends WebPage> V navigateBackSeveralTimes(Class<V> expectedClazz, int timesToNavigateBack) {
        IntStream.range(0, timesToNavigateBack).forEach(navigate -> {
            sleepInSeconds(3);
            getDriver().navigate().back();
        });
        return TestlioPageFactory.initWebElements(getDriver(), expectedClazz);
    }

    public Set<String> getWindowHadles() {
        return getDriver().getWindowHandles();
    }

    public int getOpenedWindowsQuanity() {
        int size = getWindowHadles().size();
        log.info(format("Windows opened: %d", size));
        return size;
    }

    public JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) getDriver();
    }

    @Step("Close mobile banner")
    public <V extends WebPage> V closeMobileBanner(WebElement bannerAppFrame, Class<V> clazz) {
        if (isElementPresent(10, bannerAppFrame)) {
            try {
                getJSExecutor().executeScript(
                        "document.getElementById('branch-banner-iframe').contentDocument.getElementById('branch-banner-close1').click()");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return TestlioPageFactory.initWebElements(getDriver(), clazz);
    }

}