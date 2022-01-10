package com.testlio.lib.driver.web;

import com.testlio.lib.driver.DriverProvider;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class WebDriverProvider extends DriverProvider {

	@Step("Delete cookies")
	protected void deleteCookies() {
	    log.info("Deleting browser cookies");
	    getDriver().manage().deleteAllCookies();
	}

	@Step("Maximize browser screen")
	protected void maximizeBrowserScreen() {
        log.info("Maximizing browser screen");
	    getCreatedDriver().manage().window().maximize();
	}

    @Step("Full screen browser screen")
    protected void fullScreenBrowserScreen() {
        log.info("Full screen browser screen");
        getCreatedDriver().manage().window().fullscreen();
    }

}