package com.testlio.tests.stress;

import com.testlio.lib.ExtendedBaseTest;
import com.testlio.models.TestConfig;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.testlio.lib.TestConfigBuilder.generateStepWithNestedSteps;

public class ALotOfNestedStepsTest extends ExtendedBaseTest {
    @DataProvider
    public static Object[][] aLotOfNestedStepsDataProvider() {
        int STEP_NESTING_DEPTH = 7;

        TestConfig testConfig = new TestConfig(String.format("Test with %d nested steps", STEP_NESTING_DEPTH));
        testConfig.setSteps(generateStepWithNestedSteps(STEP_NESTING_DEPTH));

        return new TestConfig[][]{ { testConfig } };
    }

    @Test(dataProvider = "aLotOfNestedStepsDataProvider")
    public void test(TestConfig testConfig) throws Exception {
        super.test(testConfig);
    }
}
