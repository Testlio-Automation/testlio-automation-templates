package com.testlio.pages;

import com.testlio.lib.pagefactory.WebPage;
import com.testlio.lib.utility.screenshot.annotations.MakeScreenshot;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.function.Function;

import static com.testlio.lib.pagefactory.TestlioPageFactory.initWebElements;

/**
 * This class represents Testlio platform login page
 */
@Slf4j
public class TestlioLoginWebPage extends WebPage {

    // Attribute describing Email input
    @FindBy(xpath = "//input[@placeholder='Email']")
    private WebElement emailInput;

    // Attribute describing Password input
    @FindBy(xpath = "//input[@placeholder='Password']")
    private WebElement passwordInput;

    // Attribute describing Login button
    @FindBy(xpath = "//button/span[contains(text(), 'Log in')]")
    private WebElement loginButton;

    public TestlioLoginWebPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public Function<WebDriver, ?> isPageLoaded() {
        // We consider that page is loaded as far as the login button displayed
        return wait -> loginButton.isDisplayed();
    }

    /**
     * Action allowing to input any specific text
     * into the Email field on the login page
     * @param email
     */
    @Step("Input email") // Step annotation needed to mention this action in the test report
    @MakeScreenshot // This annotation points that you want to make a screenshot on this step
    public void inputEmail(String email) {
        typeTextToElement(emailInput, email);
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
     * @return TestlioHomeWebPage
     */
    @Step("Click login button")
    @MakeScreenshot
    public TestlioHomeWebPage clickLogin() {
        scrollToWebElement(loginButton);
        clickOnElement(loginButton);
        return initWebElements(getDriver(), TestlioHomeWebPage.class);
    }
}