package com.testlio.lib.utility;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;

public class TapOnElementUtil {

    private static Map<String, Double> params = new HashMap<>();
    private static Map<String, Double> paramsForCenter = new HashMap<>();

    private static Map getParams(WebElement element) {
        params.put("x", (double) element.getLocation().getX() + 1 );
        params.put("y", (double) element.getLocation().getY() + 1);
        return params;
    }

    private static Map getParamsCenter(WebElement element) {
        double x = (double) (element.getLocation().getX() + element.getSize().getWidth())/2;
        double y = (double) (element.getLocation().getY() + element.getSize().getHeight())/2;
        params.put("x", x);
        params.put("y", y);
        return params;
    }

    private static void tapByElementCoordinates(AppiumDriver<WebElement> driver, Map params) {
        driver.executeScript("mobile: tap", params);
    }

    public static void tapOnElementOnDeviceByExecuteScript(AppiumDriver<WebElement> driver, WebElement element) {
        Map m = getParams(element);
        tapByElementCoordinates(driver, m);
    }

    public static void tapOnElementCenterOnDeviceByExecuteScript(AppiumDriver<WebElement> driver, WebElement element) {
        Map m = getParamsCenter(element);
        tapByElementCoordinates(driver, m);
    }
}