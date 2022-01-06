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
public class AlertPopup extends MobileScreen {

    @AndroidFindBy(id = "android:id/alertTitle")
    private MobileElement title;

    @AndroidFindBy(id = "android:id/button1")
    private MobileElement okButton;

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

    @Step("Click OK button")
    public void clickOk() {
        clickOnElement(okButton);
    }

}
