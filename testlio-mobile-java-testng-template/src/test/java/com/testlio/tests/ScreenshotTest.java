package com.testlio.tests;

import com.testlio.lib.driver.DriverProvider;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

import org.testng.annotations.Test;

import static com.testlio.lib.driver.DriverFactoryProducer.getDriverProvider;

public class ScreenshotTest {

    private void attachScreenshotToAllure(String name, File screenshot) throws IOException {
        // Convert screenshot to InputStream
        BufferedImage bufferedImage = ImageIO.read(screenshot);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        byte[] screenshotByteArray = baos.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(screenshotByteArray);

        // Attach screenshot
        Allure.addAttachment(
                name,
                "image/png",
                inputStream,
                "png"
        );
    }

    @Test
    public void testCaseWithScreenshot() throws IOException {
        // Get the driver instance for current testing session
        DriverProvider driverProvider = getDriverProvider();
        WebDriver driver = driverProvider.createDriver();

        // Take screenshot
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // Attach screenshot to Allure report
        attachScreenshotToAllure("Screenshot", screenshot);
    }
}
