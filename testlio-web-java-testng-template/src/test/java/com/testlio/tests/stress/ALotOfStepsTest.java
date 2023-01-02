package com.testlio.tests.stress;

import com.testlio.lib.ExtendedBaseTest;
import com.testlio.models.TestConfig;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.testlio.lib.TestConfigBuilder.generateStepsCombination;

public class ALotOfStepsTest extends ExtendedBaseTest {
    @DataProvider
    public static Object[][] aLotOfStepsDataProvider() {
        int STEPS_COUNT = 50;

        TestConfig testConfig = new TestConfig(String.format("Test with %d steps", STEPS_COUNT));
        testConfig.setSteps(generateStepsCombination(STEPS_COUNT));

        return new TestConfig[][]{ { testConfig } };
    }

    @Test(dataProvider = "aLotOfStepsDataProvider")
    public void test(TestConfig testConfig) throws Exception {
        super.test(testConfig);
    }
}
