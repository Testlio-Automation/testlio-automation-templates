package com.testlio.tests.stress;

import com.testlio.lib.BaseTest;
import com.testlio.lib.models.ResultType;
import com.testlio.lib.models.TestConfig;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.stream.IntStream;

import static com.testlio.lib.builders.TestConfigBuilder.generateStepsCombination;

public class ALotOfTestsTest extends BaseTest {
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
    public void buildTest(TestConfig testConfig) throws Exception {
        super.buildTest(testConfig);
    }
}
