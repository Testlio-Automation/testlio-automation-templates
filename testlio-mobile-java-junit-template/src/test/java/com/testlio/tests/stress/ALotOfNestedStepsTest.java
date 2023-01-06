package com.testlio.tests.stress;

import com.testlio.lib.models.TestConfig;
import com.testlio.tests.JUnitBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static com.testlio.lib.builders.TestConfigBuilder.generateStepWithNestedSteps;

@RunWith(Parameterized.class)
public class ALotOfNestedStepsTest extends JUnitBaseTest {
    @Parameters
    public static Collection<Object[]> data() {
        int STEP_NESTING_DEPTH = 7;

        TestConfig testConfig = new TestConfig(String.format("Test with %d nested steps", STEP_NESTING_DEPTH));
        testConfig.setSteps(generateStepWithNestedSteps(STEP_NESTING_DEPTH));

        return Arrays.asList(new Object[][] { { testConfig } });
    }

    public ALotOfNestedStepsTest(TestConfig testConfig) {
        super(testConfig);
    }

    @Test
    public void buildTest() throws Exception {
        super.buildTest();
    }
}
