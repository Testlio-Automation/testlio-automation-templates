package com.testlio.lib.pagefactory;

import com.testlio.lib.pagefactory.mobile.MobilePageFactory;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.testlio.lib.pagefactory.web.CriteriaWebPageFactory;

import static java.util.Optional.ofNullable;
import static com.testlio.lib.properties.Execution.properties;

public class TestlioPageFactory {

    private TestlioPageFactory() {}

	@SuppressWarnings("unchecked")
	public static <T extends WebPage> T initWebElements(WebDriver driver, Class<T> clazz) {
		WebPage page = CriteriaWebPageFactory.criteriaInitWebElements(driver, clazz);

		return (T) ofNullable(page)
				.orElseThrow(() -> new IllegalStateException("Page Object was not initialised and is null"));
	}

	@SuppressWarnings("unchecked")
	public static <T extends MobileScreen> T initMobileElements(AppiumDriver<WebElement> driver, Class<T> clazz) {
		WebDriverWait wait = new WebDriverWait(driver, properties().getImplicitWait());
		MobileScreen screen = MobilePageFactory.initElements(driver, clazz);
		wait.until(screen.isPageLoaded());

		return (T) ofNullable(screen)
				.orElseThrow(() -> new IllegalStateException("Page Object was not initialised and is null"));
	}

}