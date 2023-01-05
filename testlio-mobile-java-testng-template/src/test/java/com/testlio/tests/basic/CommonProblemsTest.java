package com.testlio.tests.basic;

import com.testlio.lib.BaseTest;
import com.testlio.lib.models.TestConfig;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;

public class CommonProblemsTest extends BaseTest {
    private static class CommonProblemDesc {
        public String type;
        public String message;

        public CommonProblemDesc(String type, String message) {
            this.type = type;
            this.message = message;
        }
    }

    private static final CommonProblemDesc[] COMMON_PROBLEMS = {
        new CommonProblemDesc(
            "LOG_APPIUM_SERVER_NEVER_STARTED",
            "appium server never started in 5 seconds. Exiting"
        ),
        new CommonProblemDesc(
            "LOG_DEVICE_WAS_NOT_IN_THE_LIST",
            "An unknown server-side error occured while processing the command. Original error: Google Nexus was not in the list of connected devices"
        )
    };

    @DataProvider
    public static Object[][] commonProblemsDataProvider() {
        return Arrays.stream(COMMON_PROBLEMS).map(item -> {
            TestConfig testConfig = new TestConfig(
                String.format("%s common problem type should be detected", item.type)
            );
            testConfig.setMessage(item.message);
            return new TestConfig[]{ testConfig };
        }).toArray(TestConfig[][]::new);
    }

    @Test(dataProvider = "commonProblemsDataProvider")
    public void buildTest(TestConfig testConfig) throws Exception {
        super.buildTest(testConfig);
    }
}
