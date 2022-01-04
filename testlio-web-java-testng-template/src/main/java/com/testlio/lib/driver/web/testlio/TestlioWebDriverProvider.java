package com.testlio.lib.driver.web.testlio;

import java.net.URL;

import com.testlio.lib.properties.Execution;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

import org.openqa.selenium.WebDriver;
import com.testlio.lib.driver.web.WebDriverProvider;

@Slf4j
public class TestlioWebDriverProvider extends WebDriverProvider {

    @SuppressWarnings("unchecked")
    @Override
    @Step("Open browser window")
    public WebDriver createDriver() {
        String browserName = Execution.properties().getBrowserName();
        String browserVersion = Execution.properties().getBrowserVersion();
        String sessionUrl = Execution.properties().getSessionUrl();

        DesiredCapabilities desired_capabilities = new DesiredCapabilities();
        desired_capabilities.setCapability("browserName", browserName);
        desired_capabilities.setCapability("browserVersion", browserVersion);

        try{
            URL _sessionUrl = new URL(sessionUrl);
            driver.set(new RemoteWebDriver(_sessionUrl, desired_capabilities));
        }catch(Exception ex){
            log.error(ex.getMessage());
        }

        return getCreatedDriver();
    }

    @Override
    @Step("Kill AWS driver instance")
    public void killDriver() {
        if (wasDriverCreated()) {
            deleteCookies();
            getCreatedDriver().quit();
        }
        driver.remove();
    }
}