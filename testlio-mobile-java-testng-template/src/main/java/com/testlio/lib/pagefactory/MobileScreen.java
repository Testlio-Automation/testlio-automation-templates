package com.testlio.lib.pagefactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.ios.IOSTouchAction;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebElement;

import java.time.Duration;

import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.ElementOption.element;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.lang.String.format;
import static org.openqa.selenium.ScreenOrientation.LANDSCAPE;
import static org.openqa.selenium.ScreenOrientation.PORTRAIT;
import static com.testlio.lib.properties.Execution.properties;
import static com.testlio.lib.utility.TapOnElementUtil.tapOnElementCenterOnDeviceByExecuteScript;
import static com.testlio.lib.utility.TapOnElementUtil.tapOnElementOnDeviceByExecuteScript;
import static com.testlio.lib.utility.helpers.ExecutionHelper.sleepInSeconds;
import java.util.List;

@Slf4j
public abstract class MobileScreen extends Page {

    private final static String DOWN = "down";
    private final static String UP = "up";

    private TouchAction<AndroidTouchAction> androidTouchAction;
    private TouchAction<IOSTouchAction> iosTouchAction;

    @iOSXCUITFindBy(accessibility = "Done")
    private MobileElement done;

    @iOSXCUITFindBy(accessibility = "Back")
    private MobileElement back;
    @iOSXCUITFindBy(accessibility = "Done")
    private MobileElement doneReadingButton;

    public MobileScreen(AppiumDriver<WebElement> mobileDriver) {
        super(mobileDriver);
    }

    @SuppressWarnings("unchecked")
    @Override
    public AppiumDriver<WebElement> getDriver() {
        return (AppiumDriver<WebElement>) super.getDriver();
    }

    @Step("Navigate back on android device")
    public <V extends MobileScreen> V androidNavigateBack(Class<V> expectedScreenClazz) {
        ((AndroidDriver<WebElement>) getDriver()).pressKey(new KeyEvent(AndroidKey.BACK));

        return TestlioPageFactory.initMobileElements(getDriver(), expectedScreenClazz);
    }

    @Step("Tap on element: {onElement}")
    public void tapOnElement(MobileElement onElement) {
        getTouchAction().tap(element(onElement)).release().perform();
    }

    @Step("Tap on screen center")
    public void tapOnScreenCenter() {
        Dimension size = getDriver().manage().window().getSize();

        int x = (int) (size.getWidth() * 0.5);
        int y = (int) (size.getHeight() * 0.5);

        getTouchAction().press(point(x, y)).release().perform();
    }

    private void scrollVerticalView(String direction) {
        int startY;
        int endY;
        try {
            Dimension size = getDriver().manage().window().getSize();

            if (direction.equals("down")) {
                startY = (int) (size.getHeight() * 0.5);
                endY = (int) (size.getHeight() * 0.2);
            } else if (direction.equals("up")){
                startY = (int) (size.getHeight() * 0.2);
                endY = (int) (size.getHeight() * 0.5);
            } else {
                throw new IllegalStateException("Use only up/down direction");
            }

            int startX = (int) (size.getWidth() * 0.5);

            log.info(format("Swiping down from: (on X) %s and (on Y): %s to %s", startX, startY, endY));

            getTouchAction().press(point(startX, startY)).waitAction(waitOptions(Duration.ofSeconds(1)))
                    .moveTo(point(startX, endY)).release().perform();
        } catch (InvalidElementStateException e) {
            log.error("Swipe did not complete successfully");
        }
    }

    @Step("Scroll down scroll view")
    public void scrollDownScrollView() {
        scrollVerticalView(DOWN);
    }

    @Step("Scroll up scroll view")
    public void scrollUpScrollView() {
        scrollVerticalView(UP);
    }

    @Step("Scroll down view with parameters")
    public void scrollDownViewWithParameters(double start, double end) {
        try {
            Dimension size = getDriver().manage().window().getSize();

            int startY = (int) (size.getHeight() * start);
            int endY = (int) (size.getHeight() * end);
            int startX = (int) (size.getWidth() * 0.5);

            log.info(format("Swiping down from: (on X) %s and (on Y): %s to %s", startX, startY, endY));

            getTouchAction().press(point(startX, startY)).waitAction(waitOptions(Duration.ofSeconds(1)))
                    .moveTo(point(startX, endY)).release().perform();
        } catch (InvalidElementStateException e) {
            log.error("Swipe did not complete successfully");
        }
    }

    @Step("Scroll down scroll view multiple times")
    public void scrollDownScrollViewTimes(int times) {
        for (int scrollAttempt = 0; scrollAttempt < times - 1; scrollAttempt++) {
            scrollDownScrollView();
            log.info("Scroll down #" + scrollAttempt);
        }
    }

    @Step("Scroll Up scroll view multiple times")
    public void scrollUpScrollViewTimes(int times) {
        for (int i = 0; i < times; i++) {
            scrollUpScrollView();
            log.info("Scroll up #" + i);
        }
    }

    @Step("Scroll right scroll view")
    public void scrollRightScrollView() {
        Dimension size = getDriver().manage().window().getSize();

        int startX = (int) (size.getWidth() * 0.9);
        int startY = (int) (size.getHeight() * 0.7);

        int endX = (int) (size.getWidth() * 0.1);
        int endY = startY;

        String message = format("Swiping from start (X:%s; Y:%s) to (X:%s; Y:%s)", startX, startY, endX, endY);

        log.info(message);

        getTouchAction().press(point(startX, startY)).waitAction(waitOptions(Duration.ofSeconds(1)))
                .moveTo(point(endX, endY)).release().perform();
    }

    @Step("Scroll the element list horizontally")
    public void scrollElementListHorizontally(List<MobileElement> mobileElement, int increaseDistanceBy) {
        int startX = mobileElement.get(0).getCenter().getX() * increaseDistanceBy;
        int endX = (int) (startX * 0.2);

        int startY = mobileElement.get(0).getCenter().getY();
        int endY = startY;

        String message = format("Swiping from start (X:%s; Y:%s) to (X:%s; Y:%s)", startX, startY, endX, endY);

        log.info(message);

        getTouchAction().press(point(startX, startY)).waitAction(waitOptions(Duration.ofSeconds(1)))
                .moveTo(point(endX, endY)).release().perform();
    }

    @Step("Is element present with scroll attempts")
    public boolean isElementPresentWithScrollAttempts(MobileElement mobileElement, int scrollAttempts) {
        boolean isElementFound = isElementPresent(1, mobileElement);
        int numberOfScrolls = 0;
        while (!isElementFound && numberOfScrolls < scrollAttempts) {
            scrollDownScrollView();
            isElementFound = isElementPresent(1, mobileElement);
            numberOfScrolls++;
        }
        return isElementPresent(1, mobileElement);
    }

    public boolean isListElementPresentWithOneScrollAttempt(List<MobileElement> mobileElements) {
        boolean isElementsFound = mobileElements.size() > 0;
        int numberOfScrolls = 0;

        while (!isElementsFound && numberOfScrolls < 1) {
            scrollDownViewWithParameters(0.2, 0.1);
            isElementsFound = mobileElements.size() > 0;
            numberOfScrolls++;
        }
        return isElementPresent(1, mobileElements.get(0));
    }

    @Step("Create touch action")
    public TouchAction<? extends TouchAction> getTouchAction() {
        if (properties().isAndroidExecution()) {
            return getAndroidTouchAction();
        }
        return getIosTouchAction();
    }

    private TouchAction<AndroidTouchAction> getAndroidTouchAction() {
        if (androidTouchAction == null) {
            androidTouchAction = new TouchAction<>(getDriver());
        }
        return androidTouchAction;
    }

    private TouchAction<IOSTouchAction> getIosTouchAction() {
        if (iosTouchAction == null) {
            iosTouchAction = new TouchAction<>(getDriver());
        }
        return iosTouchAction;
    }

    public String getMobileElementXpath(MobileElement element) {
        String xpath = element.toString();
        log.info(xpath);
        String episodeXpathLocator = "";
        if (xpath.contains("-> xpath: ")) {
            episodeXpathLocator = StringUtils.substring(xpath, xpath.lastIndexOf("-> xpath: ") + 10,
                    xpath.lastIndexOf("]"));
        } else {
            episodeXpathLocator = StringUtils.substring(xpath, xpath.lastIndexOf("({By.xpath: ") + 12,
                    xpath.lastIndexOf("})"));
        }
        log.info(format("Element xpath locator is: %s", episodeXpathLocator));
        return episodeXpathLocator;
    }

    @Step("Switch wifi mode android")
    public void switchWifiOnAndroid(boolean switchModeOn) {
        if (switchModeOn) {
            log.info("Turning WI FI ON");
            if (!((AndroidDriver<WebElement>) getDriver()).getConnection().isWiFiEnabled()) {
                log.info("WI FI is NOT enabled");
                ((AndroidDriver<WebElement>) getDriver()).toggleWifi();
                log.info("WI FI is turned ON");
            }
        } else {
            log.info("Turning WI FI OFF");
            if (((AndroidDriver<WebElement>) getDriver()).getConnection().isWiFiEnabled()) {
                log.info("WI FI is enabled");
                ((AndroidDriver<WebElement>) getDriver()).toggleWifi();
                log.info("WI FI is turned OFF");
            }
        }
    }

    @Step("Rotate Portrait Android")
    public void rotatePortraitAndroid() {
        getDriver().rotate(PORTRAIT);
    }

    @Step("Rotate Landscape Android")
    public void rotateLandscapeAndroid() {
        getDriver().rotate(LANDSCAPE);
    }

    @Step("Close web view")
    public void closeWebView() {
        tapOnElement(done);
    }

    @Step("Back to the previous screen")
    public void navigateBack() {
        if (properties().isIOSExecution()) {
            tapOnElement(back);
        } else {
            getDriver().navigate().back();
        }
    }

    @Step("Back to the previous screen")
    public <V extends MobileScreen> V navigateBack(Class<V> expectedScreen) {
        if (properties().isIOSExecution()) {
            tapOnElement(back);
        } else {
            getDriver().navigate().back();
        }
        return TestlioPageFactory.initMobileElements(getDriver(), expectedScreen);
    }

    @Step("Click on {element} by its location")
    public void tapOnElementByLocation(WebElement element) {
        int x = element.getLocation().getX();
        int y = element.getLocation().getY();
        log.info(format("Click on %s, %s", x, y));
        getTouchAction().tap(point(x, y)).perform();
    }

    @Step("Click on {x}, {y} by its location")
    public void tapOnElementByLocation(int x, int y) {
        log.info(format("Click on %s, %s", x, y));
        getTouchAction().tap(point(x, y)).perform();
    }

    @Step("Click on {element} on device")
    public void tapOnElementOnDevice(WebElement element) {
        tapOnElementOnDeviceByExecuteScript(getDriver(), element);
    }

    @Step("Click on center of {element} on device")
    public void tapOnElementCenterOnDevice(WebElement element) {
        tapOnElementCenterOnDeviceByExecuteScript(getDriver(), element);
    }

    @Step("Close app")
    public void closeApp() {
        try {
            getDriver().closeApp();
        } catch (Exception e) {
            log.error("Exception occured when closing app: " + e.getMessage());
        }
    }

    @Step("Relaunch Android application")
    public void relaunchAndroidApp() {
        if (properties().isAndroidExecution()) {
            getDriver().closeApp();
            sleepInSeconds(5);
            //getDriver().launchApp();
            try {
                ((AndroidDriver) getDriver()).startActivity(
                    new Activity(
                        properties().getMobilePackageId(),
                        properties().getMobileActivity()
                    )
                );
            } catch (Exception e) {
                log.error("Exception occurred during start of activity: " + e.getMessage());
            }
        }
    }
}
