package com.testlio.lib.utility.recognition;

import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import org.openqa.selenium.WebElement;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static java.awt.Color.green;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.util.Objects.requireNonNull;
import static javax.imageio.ImageIO.read;
import static javax.imageio.ImageIO.write;
import static net.itarray.automotion.tools.helpers.TextFinder.textIsFound;
import static org.openqa.selenium.OutputType.FILE;

@Slf4j
public class OCRReader {

    private OCRReader() {}

	static {
		CLibrary.INSTANCE.setlocale(CLibrary.LC_ALL, "C");
		CLibrary.INSTANCE.setlocale(CLibrary.LC_NUMERIC, "C");
		CLibrary.INSTANCE.setlocale(CLibrary.LC_CTYPE, "C");
	}

	public static String readTextFromImage(URL fileImage) {
		try {
			log.info("Perform OCR");
			BufferedImage imageToBeOCRed = read(requireNonNull(convertImageToGrayScale(fileImage)));
			Tesseract ocr = new Tesseract();
			ocr.setPageSegMode(2);
			return ocr.doOCR(imageToBeOCRed);
		} catch (Exception e) {
			log.warn(e.getLocalizedMessage());
		}
		return null;
	}

	public static String readTextFromImage(AppiumDriver<WebElement> driver) {
		try {
			URL screenshot = driver.getScreenshotAs(FILE).toURI().toURL();
			return readTextFromImage(screenshot);
		} catch (MalformedURLException e) {
			log.warn(e.getLocalizedMessage());
		}
		return "";
	}

	public static boolean isTextFound(URL screenshot, String textToFind) {
		String source = readTextFromImage(screenshot);
		log.info(format("Recognized text:%n  %s", source));

		if (source != null && !source.trim().isEmpty()) {
			return textIsFound(textToFind, source);
		} else {
			return false;
		}
	}

	public static boolean isTextFoundOnTheScreen(AppiumDriver<WebElement> driver, String textToFind) {
		try {
			URL screenshot = driver.getScreenshotAs(FILE).toURI().toURL();
			return isTextFound(screenshot, textToFind);
		} catch (Exception e) {
			log.warn(e.getLocalizedMessage());
		}

		return false;
	}

	private static URL convertImageToGrayScale(URL file) {
		try {
			BufferedImage image = read(file);

			BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), TYPE_INT_RGB);

			Graphics2D graphic = result.createGraphics();
			graphic.drawImage(image, 0, 0, green, null);

			for (int i = 0; i < result.getHeight(); i++) {
				for (int j = 0; j < result.getWidth(); j++) {
					Color c = new Color(result.getRGB(j, i));
					int red = c.getRed();
					int green = c.getGreen();
					int blue = c.getBlue();
					int avg = (red + green + blue) / 3;
					if (avg >= 0 && avg < 50) {
						Color newColor = new Color(0, 0, 0);
						result.setRGB(j, i, newColor.getRGB());
					} else if (avg >= 50 && avg <= 200) {
						Color newColor = new Color(65, 65, 65);
						result.setRGB(j, i, newColor.getRGB());
					} else {
						Color newColor = new Color(255, 255, 255);
						result.setRGB(j, i, newColor.getRGB());
					}

				}
			}

			File output = new File("grayscale-tmp-" + currentTimeMillis() + ".jpg");
			write(result, "jpg", output);
			return output.toURI().toURL();

		} catch (IOException e) {
			log.warn("Could not convert image to gray scale");
		}
		return null;
	}

}
