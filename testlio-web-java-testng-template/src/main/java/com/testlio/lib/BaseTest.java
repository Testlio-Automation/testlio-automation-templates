package com.testlio.lib;

import com.testlio.lib.driver.DriverProvider;
import com.testlio.lib.utility.log.LogFactory;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

import static com.testlio.lib.driver.DriverFactoryProducer.getDriverProvider;
import static com.testlio.lib.properties.Execution.properties;
import static com.testlio.lib.utility.helpers.ExecutionHelper.sleepInSeconds;

public abstract class BaseTest {

	private static final Logger LOG = LogFactory.getLogger(BaseTest.class);

	private DriverProvider driverProvider;
	protected WebDriver webDriver;

	@BeforeMethod(alwaysRun = true)
	public void setUp(ITestContext context, Method method) {
		driverProvider = getDriverProvider();
		webDriver = driverProvider.createDriver();
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		LOG.info("Killing driver instance");
		driverProvider.killDriver();
	}

	@BeforeMethod(alwaysRun = true)
	public void setupBaseTest(ITestContext context, Method method) {
		LOG.info("Starting setup before test method");
		LOG.info("Setting test method into execution params");
		properties().setTestMethod(method.getName());
		properties().setTestBuildName(context.getCurrentXmlTest().getSuite().getName());
		LOG.info("Test method executed is: " + properties().getTestMethod());

		if (properties().isTimeoutBetweenTests()) {
			waitBetweenTests();
		}
	}

	@AfterMethod(alwaysRun = true)
	public void tearDownBaseTest() {
		LOG.info("Starting tear down after test method");
	}

	@Step("Wait before test execution")
	private void waitBetweenTests() {
		LOG.info("Timeout between tests set");
		properties().incrementTestMethodOrderNumberInThread();
		int testNumberInOrder = properties().getTestMethodOrderNumberInThread().get();
		LOG.info("Test number in order: " + testNumberInOrder);
		if (testNumberInOrder != 1) {
		    int waitTime = properties().getTimeoutBetweenTestsValue();
			LOG.info("Waiting beetween tests: " + waitTime);
            sleepInSeconds(waitTime);
		}
	}

}