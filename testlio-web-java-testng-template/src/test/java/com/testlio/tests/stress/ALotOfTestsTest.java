package com.testlio.tests.stress;

import com.testlio.lib.ExtendedBaseTest;
import com.testlio.models.ResultType;
import com.testlio.models.TestConfig;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.stream.IntStream;

import static com.testlio.lib.TestConfigBuilder.generateStepsCombination;

public class ALotOfTestsTest extends ExtendedBaseTest {
    @DataProvider
    public static Object[][] aLotOfTestsDataProvider() {
        int TESTS_COUNT = 50;

        return IntStream.rangeClosed(1, TESTS_COUNT).mapToObj(val -> {
            TestConfig testConfig = new TestConfig(String.format("Test #%d", val), ResultType.getRandom());
            testConfig.setSteps(generateStepsCombination(3));
            return new TestConfig[]{ testConfig };
        }).toArray(TestConfig[][]::new);
    }

    @Test(dataProvider = "aLotOfTestsDataProvider")
    public void test(TestConfig testConfig) throws Exception {
        super.test(testConfig);
    }
}
