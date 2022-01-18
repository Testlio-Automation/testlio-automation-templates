package com.testlio.lib.listeners;

import com.testlio.lib.driver.DriverProvider;
import com.testlio.lib.utility.data.DateTimeUtility;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import ru.yandex.qatools.ashot.Screenshot;

import java.io.File;

import static io.qameta.allure.Allure.addAttachment;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static com.testlio.lib.utility.screenshot.DriverScreenshotUtility.attachScreenshotToReport;
import static com.testlio.lib.utility.screenshot.DriverScreenshotUtility.getPageScreenshot;

@Slf4j
public class TestResultListener implements IInvokedMethodListener {

	protected boolean takeScreenshot(final String name, WebDriver driver) {
		String screenshotDirectory = System.getProperty("appium.screenshots.dir", System.getProperty("java.io.tmpdir", ""));
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		return screenshot.renameTo(new File(screenshotDirectory, String.format("%s.png", name)));
	}

	public static String getTestResult(ITestResult result) {
		String status = "";
		switch (result.getStatus()) {
		case ITestResult.SUCCESS:
			status = "PASSED";
			break;
		case ITestResult.FAILURE:
			status = "FAILED";
			break;
		case ITestResult.SKIP:
			status = "SKIPPED";
			break;
		default:
			status = "SKIPPED";
			break;
		}

		return status;
	}

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        log.info("Before fixture/test invocation");
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		if (method.isTestMethod()) {
			printTestResults(testResult);
			ofNullable(testResult.getThrowable()).ifPresent(fail -> {
				if (DriverProvider.wasDriverCreated()) {
					Screenshot screenshot = getPageScreenshot(DriverProvider.getCreatedDriver());
					log.info("Taking screenshot for failed test");
					takeScreenshot("Screenshot on TEST FAILURE", DriverProvider.getCreatedDriver());
					try {
                        attachScreenshotToReport("Screenshot on TEST FAILURE", screenshot);
                        addAttachment("Failed page source", "text/plain", getFailedPageSource(), "txt");
                        Allure.addAttachment("Failure time: " + DateTimeUtility.getCurrentDate(), "text/plain", fail.getMessage(), "txt");
                    } catch (Exception e) {
					    log.error(e.getMessage());
					    log.error("Error occurred while adding test failure attachments to Allure report");
                    }
				}
			});
		}
	}

	/**
	 * Prints the test results to report.
	 * 
	 * @param result
	 *            the result
	 */
	private void printTestResults(ITestResult result) {
		log.info(format("Test status is: %s", getTestResult(result)));
	}

	@SuppressWarnings("not used")
	private String getFailedPageSource() {
		try {
			log.info("Getting page sources");
			return DriverProvider.getCreatedDriver().getPageSource();
		} catch (Exception e) {
			log.info("Error getting page sources");
			return "Error getting page sources";
		}
	}
}