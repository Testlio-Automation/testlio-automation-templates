package com.testlio.tests.basic;

import com.testlio.lib.models.TestConfig;
import com.testlio.tests.JUnitBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static com.testlio.constants.FakeTestConstants.*;

@RunWith(Parameterized.class)
public class WithManualTestIdTest extends JUnitBaseTest {
    @Parameters
    public static Collection<Object[]> data() {
        TestConfig testConfig = new TestConfig("Test with Testlio manual test ID");
        testConfig.setTestlioManualTestID(MANUAL_TEST_GUID);
        return Arrays.asList(new Object[][] { { testConfig } });
    }

    public WithManualTestIdTest(TestConfig testConfig) {
        super(testConfig);
    }

    @Test
    public void buildTest() throws Exception {
        super.buildTest();
    }
}
