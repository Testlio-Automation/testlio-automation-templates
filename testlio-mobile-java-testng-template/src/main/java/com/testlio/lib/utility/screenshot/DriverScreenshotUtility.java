package com.testlio.lib.utility.screenshot;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import static java.lang.String.format;

@Slf4j
public class DriverScreenshotUtility {

	private DriverScreenshotUtility() {}

	public static boolean takeScreenshot(final String name, WebDriver driver) {
		log.info("[aldom] Should take screenshot");
		String screenshotDirectory = System.getProperty("appium.screenshots.dir", System.getProperty("java.io.tmpdir", ""));
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		log.info("[aldom] Screenshot should be taken");
		return screenshot.renameTo(new File(screenshotDirectory, String.format("%s.png", name)));
	}

	@Step("---- Screenshot on step: {text}")
	public static void getScreenshotAndAttachToReport(String text, WebDriver driver) {
		Screenshot screenshot = getPageScreenshot(driver);
		attachScreenshotToReport(text, screenshot);
	}

	public static void attachScreenshotToReport(String text, Screenshot screenshot) {
		Allure.addAttachment(text, "image/png", getScreenshotByteIS(screenshot), "png");
	}

	public static void attachScreenshotToReport(String text, BufferedImage image) {
		Allure.addAttachment(text, "image/png", getScreenshotByteIS(image), "png");
	}

	public static Screenshot getPageScreenshot(WebDriver driver) {
		Screenshot screenImage = null;
		try {
			screenImage = // new AShot()
							// .shootingStrategy(ShootingStrategies.viewportPasting(150))
							// .takeScreenshot(driver);
					new Screenshot(ImageIO.read(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE)));
		} catch (IOException e) {
			log.error(e.getMessage());
		}

		return screenImage;
	}

	public static Screenshot getElementScreenshot(WebDriver driver, WebElement element) {
		return new AShot().takeScreenshot(driver, element);
	}

	@Step("Check whether images are different")
	public static boolean areImagesDifferentWithComparison(BufferedImage image1, BufferedImage image2,
                                                           String text1, String text2) {
		attachScreenshotToReport(text1, image1);
		attachScreenshotToReport(text2, image2);

		ImageDiff diff = new ImageDiffer().makeDiff(image1, image2);
		BufferedImage diffImage = diff.getMarkedImage();
		attachScreenshotToReport("Difference between " + text1 + " and " + text2, diffImage);

		int differenceRatio = (int) (image1.getHeight() * image1.getWidth() * 0.2);
		boolean hasDiff = diff.withDiffSizeTrigger(differenceRatio).hasDiff();

		log.info(format("Difference between images was detected: %b", hasDiff));
		log.info(format("Diff size is: %d", diff.getDiffSize()));

		return hasDiff;
	}

	@Step("Getting mobile native element image")
	public static BufferedImage getMobileNativeElementImage(WebDriver driver, WebElement element) {
	    @Nullable
		Screenshot scr1 = getPageScreenshot(driver);

		int heght = element.getSize().height;
		int width = element.getSize().width;
		log.info(format("Element Width is: %d ; element Height is: %d", width, heght));

		int xPosition = element.getLocation().x;
		int yPosition = element.getLocation().y;

		log.info(format("Element position X is: %d ; element Y position is: %d", xPosition, yPosition));
		return scr1.getImage().getSubimage(xPosition, yPosition, width, heght);
	}

	private static InputStream getScreenshotByteIS(Screenshot screenshot) {
		return getScreenshotByteIS(screenshot.getImage());
	}

	private static InputStream getScreenshotByteIS(BufferedImage image) {
		try {
			byte[] screeshotByteArray = null;

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				ImageIO.write(image, "png", baos);
			} catch (IOException e) {
				log.error(e.getMessage());
			}
			screeshotByteArray = baos.toByteArray();

			return new ByteArrayInputStream(screeshotByteArray);
		} catch (Exception e) {
			return getErrorTakingScreenshotInputStream();
		}
	}

	private static InputStream getErrorTakingScreenshotInputStream() {
		BufferedImage bufferedImage = new BufferedImage(300, 30, BufferedImage.TYPE_INT_RGB);
		Graphics graphics = bufferedImage.getGraphics();
		graphics.setFont(new Font("Arial Black", Font.PLAIN, 15));
		graphics.drawString("Exception while taking screenshot", 10, 20);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			ImageIO.write(bufferedImage, "png", baos);
		} catch (IOException e1) {
			log.error(e1.getMessage());
		}

		return new ByteArrayInputStream(baos.toByteArray());
	}

}