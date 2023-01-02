package com.testlio.tests.basic;

import com.testlio.lib.ExtendedBaseTest;
import com.testlio.models.TestConfig;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;

public class CommonProblemsTest extends ExtendedBaseTest {
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
            "LOG_MAXIMUM_SESSION_DURATION_REACHED_SELENIUM_JAVA",
            "org.openqa.selenium.TimeoutException: The session session_guid has timed out because it has reached maximum session duration of 10000 seconds. Please refer to our documentation for details on how to increase your session duration through a capability."
        ),
        new CommonProblemDesc(
            "LOG_INVALID_FILE_ARGUMENT_SELENIUM_JAVA",
            "invalid argument: File not found :"
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
    public void test(TestConfig testConfig) throws Exception {
        super.test(testConfig);
    }
}
