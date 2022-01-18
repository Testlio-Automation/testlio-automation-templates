package com.testlio.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.testlio.lib.pagefactory.MobileScreen;

import java.util.function.Function;

@Slf4j
public class AlertPopup extends MobileScreen {

    @AndroidFindBy(id = "android:id/alertTitle")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeAlert")
    private MobileElement title;

    public AlertPopup(AppiumDriver<WebElement> mobileDriver) {
        super(mobileDriver);
    }

    @Override
    public Function<WebDriver, Boolean> isPageLoaded() {
        return wait -> title.isDisplayed();
    }

    public String getTitleString() {
        return title.getText();
    }

}
