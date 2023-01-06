package com.testlio.tests.stress;

import com.testlio.lib.models.StepConfig;
import com.testlio.lib.models.TestConfig;
import com.testlio.tests.JUnitBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ALotOfStepArgumentsTest extends JUnitBaseTest {
    @Parameters
    public static Collection<Object[]> data() {
        int TEST_STEP_ARGUMENTS_COUNT = 50;

        StepConfig stepConfig = new StepConfig(String.format("Test step with %d arguments", TEST_STEP_ARGUMENTS_COUNT));
        stepConfig.setArgumentsCount(TEST_STEP_ARGUMENTS_COUNT);

        TestConfig testConfig = new TestConfig(String.format("Test with step, having %d arguments", TEST_STEP_ARGUMENTS_COUNT));
        testConfig.setSteps(new StepConfig[]{ stepConfig });

        return Arrays.asList(new Object[][] { { testConfig } });
    }

    public ALotOfStepArgumentsTest(TestConfig testConfig) {
        super(testConfig);
    }

    @Test
    public void buildTest() throws Exception {
        super.buildTest();
    }
}
