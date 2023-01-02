package com.testlio.tests.basic;

import com.testlio.models.TestConfig;
import com.testlio.lib.ExtendedBaseTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.stream.IntStream;

import static com.testlio.lib.TestConfigBuilder.*;
import static com.testlio.constants.FakeTestConstants.*;

public class AllPassedTest extends ExtendedBaseTest {
    @DataProvider
    public static Object[][] allPassedDataProvider() {
        int PASSED_TESTS_COUNT = 3;

        return IntStream.rangeClosed(1, PASSED_TESTS_COUNT).mapToObj(val -> {
            TestConfig testConfig = new TestConfig(String.format("Test should pass #%d", val));
            testConfig.setScreenshotsCount(TEST_SCREENSHOTS_COUNT);
            testConfig.setVideosCount(TEST_VIDEOS_COUNT);
            testConfig.setLabelsCount(TEST_LABELS_COUNT);
            testConfig.setArgumentsCount(TEST_ARGUMENTS_COUNT);
            testConfig.setSteps(generateStepsCombination(TEST_STEPS_COUNT, STEP_SCREENSHOTS_COUNT, STEP_ARGUMENTS_COUNT));
            return new TestConfig[]{ testConfig };
        }).toArray(TestConfig[][]::new);
    }

    @Test(dataProvider = "allPassedDataProvider")
    public void test(TestConfig testConfig) throws Exception {
        super.test(testConfig);
    }
}
