package com.testlio.tests.stress;

import com.testlio.lib.models.TestConfig;
import com.testlio.tests.JUnitBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ALotOfLabelsTest extends JUnitBaseTest {
    @Parameters
    public static Collection<Object[]> data() {
        int TEST_LABELS_COUNT = 50;

        TestConfig testConfig = new TestConfig(String.format("Test with %d labels", TEST_LABELS_COUNT));
        testConfig.setLabelsCount(TEST_LABELS_COUNT);

        return Arrays.asList(new Object[][] { { testConfig } });
    }

    public ALotOfLabelsTest(TestConfig testConfig) {
        super(testConfig);
    }

    @Test
    public void buildTest() throws Exception {
        super.buildTest();
    }
}
