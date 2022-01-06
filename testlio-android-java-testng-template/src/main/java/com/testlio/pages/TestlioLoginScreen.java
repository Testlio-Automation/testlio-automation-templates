package com.testlio.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.testlio.lib.utility.screenshot.annotations.MakeScreenshot;

import java.util.function.Function;

import static com.testlio.lib.pagefactory.TestlioPageFactory.initMobileElements;

@Slf4j
public class TestlioLoginScreen extends TestlioParentScreen {

    @AndroidFindBy(xpath = "//*[@resource-id='login-title']")
    private MobileElement title;

    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@text, 'Username')]")
    private MobileElement usernameInput;

    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@text, 'Password')]")
    private MobileElement passwordInput;

    @AndroidFindBy(xpath = "//*[@resource-id='login-button']")
    private MobileElement loginButton;

    public TestlioLoginScreen(AppiumDriver<WebElement> mobileDriver) {
        super(mobileDriver);
    }

    @Override
    public Function<WebDriver, Boolean> isPageLoaded() {
        return wait -> title.isDisplayed();
    }

    /**
     * Action allowing to input any specific text
     * into the Email field on the login page
     * @param username
     */
    @Step("Input username") // Step annotation needed to mention this action in the test report
    @MakeScreenshot // This annotation points that you want to make a screenshot on this step
    public void inputUsername(String username) {
        typeTextToElement(usernameInput, username);
    }

    /**
     * Action allowing to input any specific text
     * into the Password field on the login page
     * @param password
     */
    @Step("Input password")
    @MakeScreenshot
    public void inputPassword(String password) {
        typeTextToElement(passwordInput, password);
    }

    /**
     * Action allowing to click on Login
     * button on the login page
     */
    @Step("Click login button")
    @MakeScreenshot
    public AlertPopup clickLogin() {
        clickOnElement(loginButton);
        return initMobileElements(getDriver(), AlertPopup.class);
    }

}