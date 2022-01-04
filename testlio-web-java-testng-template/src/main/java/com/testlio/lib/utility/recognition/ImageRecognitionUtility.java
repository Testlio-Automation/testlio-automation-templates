package com.testlio.lib.utility.recognition;

import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.sikuli.basics.Settings;
import org.sikuli.script.Finder;
import org.sikuli.script.Match;

import javax.imageio.ImageIO;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Duration;

import static java.lang.String.format;
import static org.openqa.selenium.OutputType.FILE;
import static org.sikuli.basics.Settings.MinSimilarity;

@Slf4j
public class ImageRecognitionUtility {

	private static final double DEFAULT_MIN_SIMILARITY = 0.8;

	private ImageRecognitionUtility() {}

	static {
		Settings.OcrTextSearch = true;
		Settings.OcrTextRead = true;
	}

	/**
	 * Convenience method that returns true if the element is visible on the screen.
	 */
	public static boolean elementExists(BufferedImage baseImg, String targetImgPath, int findOrder) {
		Point2D coords = getCoords(baseImg, targetImgPath, findOrder);
		double foundXCoords = coords.getX();
		double foundYCoords = coords.getY();
		log.info(format("Found image: %s coordinate X: %s", targetImgPath, foundXCoords));
		log.info(format("Found image: %s coordinate Y: %s", targetImgPath, foundYCoords));

		return (foundXCoords >= 0) && (foundYCoords >= 0);
	}

	/**
	 * Convenience method that returns true if the element is visible on the screen
	 * with similarity coof..
	 */
	public static boolean elementExists(BufferedImage baseImg, String targetImgPath, double minSimilarityValue,
			int findOrder) {
		Point2D coords = getCoords(baseImg, targetImgPath, minSimilarityValue, findOrder);

		return (coords.getX() >= 0) && (coords.getY() >= 0);
	}

	public static BufferedImage getBufferedImage(AppiumDriver<WebElement> driver) {
		try {
			return ImageIO.read(driver.getScreenshotAs(FILE));
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	public static Point2D getCoords(AppiumDriver<WebElement> driver, String targetImgPath) {
		return getCoords(driver, targetImgPath, MinSimilarity, 1);
	}

	public static Point2D getCoords(AppiumDriver<WebElement> driver, String targetImgPath, double minSimilarityValue,
			int findOrder) {
		BufferedImage baseImg = getBufferedImage(driver);
		if (baseImg == null) {
			return getFalsePoint();
		}
		return getCoords(baseImg, targetImgPath, minSimilarityValue, findOrder);
	}

	public static Point2D getCoords(BufferedImage baseImg, String targetImgPath, double minSimilarityValue,
			int findOrder) {
		// set new minimum similarity
		MinSimilarity = minSimilarityValue;
		try {
			return getCoords(baseImg, targetImgPath, findOrder);
		} finally {
			// revert to default similarity
			MinSimilarity = DEFAULT_MIN_SIMILARITY;
		}
	}

	/**
	 * getCoords returns the coordinates of the FIRST element that matches the
	 * specified
	 *
	 * @param baseImg
	 *            is the screenshot of the device
	 * @param targetImgPath
	 *            is the image of the element that you want to find
	 * @return coordinates of the centre of the element found as Point2D
	 */
	public static Point2D getCoords(BufferedImage baseImg, String targetImgPath, int findOrder) {
		Finder f = new Finder(baseImg);
		Point2D coords = getFalsePoint();

		f.find(targetImgPath);
		int curentFindOrder = 0;
		while (f.hasNext()) {
			curentFindOrder++;
			Match m = f.next();
			if (curentFindOrder == findOrder) {
				coords.setLocation(m.getTarget().getX(), m.getTarget().getY());
				break;
			}
			// m = f.next();
		}

		double foundXCoords = coords.getX();
		double foundYCoords = coords.getY();
        log.info(format("Found image: %s coordinate X: %s", targetImgPath, foundXCoords));
        log.info(format("Found image: %s coordinate Y: %s", targetImgPath, foundYCoords));

		return coords;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void waitForImage(int secondsToWait, BufferedImage baseImg, String targetImgPath, int findOrder) {
		FluentWait wait = new FluentWait(new Object());
		wait.pollingEvery(Duration.ofSeconds(5)).withTimeout(Duration.ofSeconds(secondsToWait))
				.until(untilWait -> elementExists(baseImg, targetImgPath, findOrder));
	}

	public static Point2D getFalsePoint() {
		return new Point2D.Double(-1, -1);
	}

}