package com.testlio.pages;

import com.testlio.lib.pagefactory.WebPage;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.function.Function;

@Slf4j
public class TestlioHomeWebPage extends WebPage {

    @FindBy(xpath = "//a[@id='testlio-header-logo']")
    private WebElement logo;

    public TestlioHomeWebPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public Function<WebDriver, ?> isPageLoaded() {
        return wait -> logo.isDisplayed();
    }

}