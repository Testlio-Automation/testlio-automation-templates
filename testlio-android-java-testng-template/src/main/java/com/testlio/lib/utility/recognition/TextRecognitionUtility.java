package com.testlio.lib.utility.recognition;

import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Word;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;

import static java.lang.String.format;
import static org.openqa.selenium.OutputType.FILE;

@Slf4j
public class TextRecognitionUtility {

    static boolean matchFound;
    static {
        CLibrary.INSTANCE.setlocale(CLibrary.LC_ALL, "C");
        CLibrary.INSTANCE.setlocale(CLibrary.LC_NUMERIC, "C");
        CLibrary.INSTANCE.setlocale(CLibrary.LC_CTYPE, "C");
    }

    public static Point2D getCenterCoordinatesOfText(AppiumDriver<WebElement> driver, String toBeMatched, String... toBeExcluded){
        Point2D result = new Point2D.Double(-1, -1);
        File srcImage = driver.getScreenshotAs(FILE);

        log.info(format("String to be matched exactly: %s", toBeMatched));

        try {
            BufferedImage bufferedImage = ImageIO.read(srcImage);
            ITesseract textReader = new Tesseract();
            textReader.setLanguage("eng");

            for (Word word : textReader.getWords(bufferedImage, ITessAPI.TessPageIteratorLevel.RIL_TEXTLINE)) {
                String textLine = word.getText();
                log.info(format("Current text line: %s", textLine));
                if(toBeExcluded.length == 0) {
                    if(textLine.contains(toBeMatched)) {
                        result = returnCoordinatesOfMatchedWord(word);
                    }
                } else {
                    if(textLine.contains(toBeMatched) && !textLine.contains(toBeExcluded[0]) && !textLine.contains(toBeExcluded[1])) {
                        result = returnCoordinatesOfMatchedWord(word);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
        if (!matchFound) {
            throw new IllegalStateException("Exact match not found");
        }
        return result;
    }

    public static Point2D returnCoordinatesOfMatchedWord(Word word) {
        Point2D result = new Point2D.Double(-1, -1);
        int pointX = word.getBoundingBox().x + word.getBoundingBox().width/2;
        int pointY = word.getBoundingBox().y + word.getBoundingBox().height/2;
        log.info(format("Center of matched string: (x: %s, y: %s)", pointX, pointY));
        result.setLocation(pointX, pointY);
        matchFound = true;
        return  result;
    }
}
