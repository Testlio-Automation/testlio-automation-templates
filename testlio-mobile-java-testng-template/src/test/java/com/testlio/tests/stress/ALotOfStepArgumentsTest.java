package com.testlio.tests.stress;

import com.testlio.lib.BaseTest;
import com.testlio.lib.models.StepConfig;
import com.testlio.lib.models.TestConfig;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ALotOfStepArgumentsTest extends BaseTest {
    @DataProvider
    public static Object[][] aLotOfStepArgumentsDataProvider() {
        int TEST_STEP_ARGUMENTS_COUNT = 50;

        StepConfig stepConfig = new StepConfig(String.format("Test step with %d arguments", TEST_STEP_ARGUMENTS_COUNT));
        stepConfig.setArgumentsCount(TEST_STEP_ARGUMENTS_COUNT);

        TestConfig testConfig = new TestConfig(String.format("Test with step, having %d arguments", TEST_STEP_ARGUMENTS_COUNT));
        testConfig.setSteps(new StepConfig[]{ stepConfig });

        return new TestConfig[][]{ { testConfig } };
    }

    @Test(dataProvider = "aLotOfStepArgumentsDataProvider")
    public void buildTest(TestConfig testConfig) throws Exception {
        super.buildTest(testConfig);
    }
}
