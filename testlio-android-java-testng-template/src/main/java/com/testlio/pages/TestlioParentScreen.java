package com.testlio.pages;

import com.testlio.lib.pagefactory.MobileScreen;
import com.testlio.lib.utility.screenshot.annotations.MakeScreenshot;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;

import static com.testlio.lib.pagefactory.TestlioPageFactory.initMobileElements;

@Slf4j
public abstract class TestlioParentScreen extends MobileScreen {

    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text, 'Static Page')]")
    private MobileElement staticPageTab;

    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text, 'Login')]")
    private MobileElement loginTab;

    public TestlioParentScreen(AppiumDriver<WebElement> mobileDriver) {
        super(mobileDriver);
    }

    @Step("Navigate to the Static Page tab")
    @MakeScreenshot
    public TestlioStaticPageScreen goToStaticPageScreenTab() {
        clickOnElement(staticPageTab);
        return initMobileElements(getDriver(), TestlioStaticPageScreen.class);
    }

    @Step("Navigate to the Login tab")
    @MakeScreenshot
    public TestlioLoginScreen goToLoginScreenTab() {
        clickOnElement(loginTab);
        return initMobileElements(getDriver(), TestlioLoginScreen.class);
    }

}