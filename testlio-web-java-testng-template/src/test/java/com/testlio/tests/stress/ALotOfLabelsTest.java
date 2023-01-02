package com.testlio.tests.stress;

import com.testlio.lib.ExtendedBaseTest;
import com.testlio.models.TestConfig;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ALotOfLabelsTest extends ExtendedBaseTest {
    @DataProvider
    public static Object[][] aLotOfLabelsDataProvider() {
        int TEST_LABELS_COUNT = 50;

        TestConfig testConfig = new TestConfig(String.format("Test with %d labels", TEST_LABELS_COUNT));
        testConfig.setLabelsCount(TEST_LABELS_COUNT);

        return new TestConfig[][]{ { testConfig } };
    }

    @Test(dataProvider = "aLotOfLabelsDataProvider")
    public void test(TestConfig testConfig) throws Exception {
        super.test(testConfig);
    }
}
