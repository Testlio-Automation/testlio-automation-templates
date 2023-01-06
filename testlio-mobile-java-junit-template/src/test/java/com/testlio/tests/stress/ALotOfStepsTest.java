package com.testlio.tests.stress;

import com.testlio.lib.models.TestConfig;
import com.testlio.tests.JUnitBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static com.testlio.lib.builders.TestConfigBuilder.generateStepsCombination;

@RunWith(Parameterized.class)
public class ALotOfStepsTest extends JUnitBaseTest {
    @Parameters
    public static Collection<Object[]> data() {
        int STEPS_COUNT = 50;

        TestConfig testConfig = new TestConfig(String.format("Test with %d steps", STEPS_COUNT));
        testConfig.setSteps(generateStepsCombination(STEPS_COUNT));

        return Arrays.asList(new Object[][] { { testConfig } });
    }

    public ALotOfStepsTest(TestConfig testConfig) {
        super(testConfig);
    }

    @Test
    public void buildTest() throws Exception {
        super.buildTest();
    }
}
