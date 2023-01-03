package com.testlio.tests.basic;

import com.testlio.lib.BaseTest;
import com.testlio.lib.models.TestConfig;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.testlio.constants.FakeTestConstants.*;

public class WithManualTestIdTest extends BaseTest {
    @DataProvider
    public static Object[][] withManualTestIdDataProvider() {
        TestConfig testConfig = new TestConfig("Test with Testlio manual test ID");
        testConfig.setTestlioManualTestID(MANUAL_TEST_GUID);
        return new Object[][] { { testConfig } };
    }

    @Test(dataProvider = "withManualTestIdDataProvider")
    public void buildTest(TestConfig testConfig) throws Exception {
        super.buildTest(testConfig);
    }
}
