package com.testlio.tests;

import com.testlio.lib.BaseTest;
import com.testlio.lib.builders.TestBuilder;
import com.testlio.lib.driver.DriverProvider;
import com.testlio.lib.models.TestConfig;
import com.testlio.lib.utility.log.LogFactory;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import lombok.Getter;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;

import static com.testlio.lib.driver.DriverFactoryProducer.getDriverProvider;
import static com.testlio.lib.properties.Execution.properties;

public class JUnitBaseTest {
    private static final Logger LOG = LogFactory.getLogger(BaseTest.class);

    private DriverProvider driverProvider;

    @Getter
    protected AppiumDriver<WebElement> mobileDriver;

    @Getter
    protected WebDriver webDriver;

    private TestConfig testConfig;

    public JUnitBaseTest() {}

    public JUnitBaseTest(TestConfig testConfig) {
        this.testConfig = testConfig;
    }

    @Before
    public void setUp() {
        driverProvider = getDriverProvider();
        if (properties().isMobileExecution()) {
            mobileDriver = driverProvider.createDriver();
        }
        if (properties().isWebExecution()) {
            webDriver = driverProvider.createDriver();
        }
    }

    @After
    public void tearDown() {
        LOG.info("Killing driver instance");
        driverProvider.killDriver();
    }

    public void buildTest() throws Exception {
        if (testConfig == null) return;
        AllureLifecycle lifecycle = Allure.getLifecycle();
        lifecycle.updateTestCase(testResult -> testResult.setName(testConfig.getName()));
        TestBuilder builder = new TestBuilder();
        builder.build(testConfig);
    }
}
