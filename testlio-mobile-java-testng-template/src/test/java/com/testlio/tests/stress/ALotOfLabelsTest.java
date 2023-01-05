package com.testlio.tests.stress;

import com.testlio.lib.BaseTest;
import com.testlio.lib.models.TestConfig;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ALotOfLabelsTest extends BaseTest {
    @DataProvider
    public static Object[][] aLotOfLabelsDataProvider() {
        int TEST_LABELS_COUNT = 50;

        TestConfig testConfig = new TestConfig(String.format("Test with %d labels", TEST_LABELS_COUNT));
        testConfig.setLabelsCount(TEST_LABELS_COUNT);

        return new TestConfig[][]{ { testConfig } };
    }

    @Test(dataProvider = "aLotOfLabelsDataProvider")
    public void buildTest(TestConfig testConfig) throws Exception {
        super.buildTest(testConfig);
    }
}
