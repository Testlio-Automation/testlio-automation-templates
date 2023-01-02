package com.testlio.tests.basic;

import com.testlio.models.ResultType;
import com.testlio.models.TestConfig;
import com.testlio.lib.ExtendedBaseTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.testlio.lib.TestConfigBuilder.generateStepsCombination;

import static com.testlio.constants.FakeTestConstants.*;

public class SomeFailedTest extends ExtendedBaseTest {
    @DataProvider
    public static Object[][] someFailedDataProvider() {
        int PASSED_TESTS_COUNT = 2;
        int FAILED_TESTS_COUNT = 1;

        List<TestConfig> testConfigs = new ArrayList<>();

        testConfigs.addAll(IntStream.rangeClosed(1, PASSED_TESTS_COUNT).mapToObj(val -> {
            TestConfig testConfig = new TestConfig(String.format("Test should pass #%d", val));
            testConfig.setScreenshotsCount(TEST_SCREENSHOTS_COUNT);
            testConfig.setVideosCount(TEST_VIDEOS_COUNT);
            testConfig.setLabelsCount(TEST_LABELS_COUNT);
            testConfig.setArgumentsCount(TEST_ARGUMENTS_COUNT);
            testConfig.setSteps(generateStepsCombination(TEST_STEPS_COUNT, STEP_SCREENSHOTS_COUNT, STEP_ARGUMENTS_COUNT));
            return testConfig;
        }).collect(Collectors.toList()));

        testConfigs.addAll(IntStream.rangeClosed(1, FAILED_TESTS_COUNT).mapToObj(val -> {
            TestConfig testConfig = new TestConfig(String.format("Test should fail #%d", val), ResultType.FAILED);
            testConfig.setScreenshotsCount(TEST_SCREENSHOTS_COUNT);
            testConfig.setVideosCount(TEST_VIDEOS_COUNT);
            testConfig.setLabelsCount(TEST_LABELS_COUNT);
            testConfig.setArgumentsCount(TEST_ARGUMENTS_COUNT);
            testConfig.setSteps(generateStepsCombination(TEST_STEPS_COUNT, STEP_SCREENSHOTS_COUNT, STEP_ARGUMENTS_COUNT, ResultType.FAILED));
            return testConfig;
        }).collect(Collectors.toList()));

        return testConfigs.stream().map(testConfig -> new TestConfig[]{ testConfig }).toArray(TestConfig[][]::new);
    }

    @Test(dataProvider = "someFailedDataProvider")
    public void test(TestConfig testConfig) throws Exception {
        super.test(testConfig);
    }
}
