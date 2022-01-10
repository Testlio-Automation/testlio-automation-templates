package com.testlio.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.testlio.lib.pagefactory.MobileScreen;
import com.testlio.lib.utility.screenshot.annotations.MakeScreenshot;

import java.util.function.Function;

import static com.testlio.lib.pagefactory.TestlioPageFactory.initMobileElements;

@Slf4j
public class TestlioStaticPageScreen extends TestlioParentScreen {

    @AndroidFindBy(xpath = "//*[@resource-id='static-page-title']")
    private MobileElement title;

    public TestlioStaticPageScreen(AppiumDriver<WebElement> mobileDriver) {
        super(mobileDriver);
    }

    @Override
    public Function<WebDriver, Boolean> isPageLoaded() {
        return wait -> title.isDisplayed();
    }

}