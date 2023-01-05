package com.testlio.tests.stress;

import com.testlio.lib.BaseTest;
import com.testlio.lib.models.TestConfig;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.testlio.lib.builders.TestConfigBuilder.*;

public class ALotOfNestedStepsTest extends BaseTest {
    @DataProvider
    public static Object[][] aLotOfNestedStepsDataProvider() {
        int STEP_NESTING_DEPTH = 7;

        TestConfig testConfig = new TestConfig(String.format("Test with %d nested steps", STEP_NESTING_DEPTH));
        testConfig.setSteps(generateStepWithNestedSteps(STEP_NESTING_DEPTH));

        return new TestConfig[][]{ { testConfig } };
    }

    @Test(dataProvider = "aLotOfNestedStepsDataProvider")
    public void buildTest(TestConfig testConfig) throws Exception {
        super.buildTest(testConfig);
    }
}
