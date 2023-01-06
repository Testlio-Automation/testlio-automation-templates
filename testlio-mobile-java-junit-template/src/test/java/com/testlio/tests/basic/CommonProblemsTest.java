package com.testlio.tests.basic;

import com.testlio.lib.models.TestConfig;
import com.testlio.tests.JUnitBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@RunWith(Parameterized.class)
public class CommonProblemsTest extends JUnitBaseTest {
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

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.stream(COMMON_PROBLEMS).map(item -> {
            TestConfig testConfig = new TestConfig(
                String.format("%s common problem type should be detected", item.type)
            );
            testConfig.setMessage(item.message);
            return new TestConfig[]{ testConfig };
        }).collect(Collectors.toList());
    }

    public CommonProblemsTest(TestConfig testConfig) {
        super(testConfig);
    }

    @Test
    public void buildTest() throws Exception {
        super.buildTest();
    }
}
