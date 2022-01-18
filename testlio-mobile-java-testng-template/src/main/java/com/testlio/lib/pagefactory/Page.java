package com.testlio.lib.pagefactory;

import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;
import static com.testlio.lib.pagefactory.PageUtils.isElementStaleReferenceExceptionThrown;
import static com.testlio.lib.properties.Execution.properties;
import static com.testlio.lib.utility.recognition.TapUtil.clickByCoords;

@Slf4j
public abstract class Page implements IPageLoadedCriteria {

	private WebDriver driver;

	public Page(WebDriver driver) {
		this.driver = driver;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public String getTitle() {
		return getDriver().getTitle();
	}

	public String getValueAttribute(WebElement element) {
	    return element.getAttribute("value");
    }

	@Step("Click on element: {element}")
	public void clickOnElement(WebElement element) {
		element.click();
	}

	@Step("Click on element: {element} by its coordinates")
	public void clickOnElementByCoordinates(WebElement element) {
		int x = element.getSize().getWidth();
		int y = element.getSize().getHeight();
		clickByCoords((AppiumDriver<WebElement>) driver, x, y);
	}

	@Step("Click on element: {element} and catch error")
	public void clickOnElementAndCatchError(WebElement element) {
		try {
			element.click();
		} catch (Exception e) {
			log.error(format("Fail to click on %s", element.toString()));
		}
	}

	@Step("Type text {text} to element: {element}")
	public void typeTextToElement(WebElement element, CharSequence... text) {
		try {
			element.clear();
		} catch (Exception e) {
			log.error("Error while clearing text field");
		}
		element.sendKeys(text);
	}

	@Step("Select {option} from dropdown element: {element}")
	public void selectOptionFromDropdownElement(WebElement element, String option) {
		try {
			Select select = new Select(element);
			select.selectByVisibleText(option);
		} catch (Exception e) {
			log.error("Error while selecting dropdown option");
		}
	}

	@Step("Create web driver wait with wait timeout {seconds} seconds")
	public WebDriverWait getWebDriverWait(int seconds) {
		return new WebDriverWait(driver, seconds);
	}

	@Step("Check element is present")
	public boolean isElementPresent(WebElement element) {
		try {
			element.isEnabled();
		} catch (Exception e) {
			if (e.getMessage().contains("NoSuchElementError") || e.getMessage().contains("Unable to locate element")
					|| e.getMessage().contains("maybe element is not interactable")) {
				return false;
			}
			if (isElementStaleReferenceExceptionThrown(e)) {
				return false;
			}
			if (e instanceof NoSuchElementException) {
				return false;
			}
			if (e instanceof TimeoutException) {
				log.error("Element is not available");
				return false;
			}
			throw e;
		}
		return true;
	}

	@Step("Check element is present")
	public boolean isElementPresent(int seconds, WebElement webElement) {
		log.info(format("Seconds to wait: %d", seconds));
		setImplicitWait(seconds);
		try {
			getWebDriverWait(seconds).until(wait -> webElement.isEnabled());
			return true;
		} catch (Exception e) {
			if (e.getMessage().contains("NoSuchElementError")
                || e.getMessage().contains("Unable to locate element")
				|| e.getMessage().contains("maybe element is not interactable")) {
				return false;
			}
			if (isElementStaleReferenceExceptionThrown(e)) {
				return false;
			}
			if (e instanceof NoSuchElementException) {
				return false;
			}
			if (e instanceof TimeoutException) {
				log.error("Element is not available");
				return false;
			}
			throw e;
		} finally {
			resetImplicitWaitToDefault();
		}
	}

	public void setImplicitWait(int seconds) {
		properties().setImplicitWait(seconds);
		getDriver().manage().timeouts().implicitlyWait(seconds, SECONDS);
	}

	public void resetImplicitWaitToDefault() {
		properties().resetImplicitWait();
		getDriver().manage().timeouts().implicitlyWait(properties().getImplicitWait(), SECONDS);
	}
}