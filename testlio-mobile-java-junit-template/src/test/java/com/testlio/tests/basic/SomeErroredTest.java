package com.testlio.tests.basic;

import com.testlio.lib.models.ResultType;
import com.testlio.lib.models.TestConfig;
import com.testlio.tests.JUnitBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.testlio.constants.FakeTestConstants.*;
import static com.testlio.lib.builders.TestConfigBuilder.generateStepsCombination;

@RunWith(Parameterized.class)
public class SomeErroredTest extends JUnitBaseTest {
    @Parameters
    public static Collection<Object[]> data() {
        int PASSED_TESTS_COUNT = 2;
        int ERRORED_TESTS_COUNT = 1;

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

        testConfigs.addAll(IntStream.rangeClosed(1, ERRORED_TESTS_COUNT).mapToObj(val -> {
            TestConfig testConfig = new TestConfig(String.format("Test should be errored #%d", val), ResultType.ERRORED);
            testConfig.setScreenshotsCount(TEST_SCREENSHOTS_COUNT);
            testConfig.setVideosCount(TEST_VIDEOS_COUNT);
            testConfig.setLabelsCount(TEST_LABELS_COUNT);
            testConfig.setArgumentsCount(TEST_ARGUMENTS_COUNT);
            testConfig.setSteps(generateStepsCombination(TEST_STEPS_COUNT, STEP_SCREENSHOTS_COUNT, STEP_ARGUMENTS_COUNT, ResultType.ERRORED));
            return testConfig;
        }).collect(Collectors.toList()));

        return testConfigs.stream().map(testConfig -> new TestConfig[]{ testConfig }).collect(Collectors.toList());
    }

    public SomeErroredTest(TestConfig testConfig) {
        super(testConfig);
        System.out.println(testConfig.getName());
    }

    @Test
    public void buildTest() throws Exception {
        super.buildTest();
    }
}
