package com.testlio.lib.utility.recognition;

import com.testlio.lib.properties.Execution;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;

import java.awt.geom.Point2D;

import static io.appium.java_client.touch.offset.PointOption.point;
import static java.lang.String.format;

@Slf4j
public class TapUtil {

	private static final int IOS_PIXEL_RATIO;

	private static TouchAction touchAction;

	static {
		IOS_PIXEL_RATIO = Execution.properties().getIosPixelRatio();
	}

	private TapUtil() {}

	public static void clickByCoords(AppiumDriver<WebElement> driver, Point2D point) {
		if (point == null) {
			return;
		}
		if (point.equals(ImageRecognitionUtility.getFalsePoint())) {
			return;
		}
		int x = (int) point.getX() / IOS_PIXEL_RATIO;
		int y = (int) point.getY() / IOS_PIXEL_RATIO;

		initTouchActionIfNeeded(driver);

		log.info(format("Click on coordinates X:[ %s ]; Y[ %s ]", x, y));
		touchAction.tap(point(x, y)).perform();
	}

	public static void clickByCoords(AppiumDriver<WebElement> driver, int x, int y) {

		x = x / IOS_PIXEL_RATIO;
		y = y / IOS_PIXEL_RATIO;

		initTouchActionIfNeeded(driver);

		log.info(format("Click on coordinates X:[ %s ]; Y[ %s ]", x, y));
		touchAction.tap(point(x, y)).perform();
	}

	private static void initTouchActionIfNeeded(AppiumDriver<WebElement> driver) {
		if (touchAction == null) {
			touchAction = new TouchAction(driver);
		}
	}
}
