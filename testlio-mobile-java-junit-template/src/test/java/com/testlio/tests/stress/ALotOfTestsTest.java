package com.testlio.tests.stress;

import com.testlio.lib.models.ResultType;
import com.testlio.lib.models.TestConfig;
import com.testlio.tests.JUnitBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.testlio.lib.builders.TestConfigBuilder.generateStepsCombination;

@RunWith(Parameterized.class)
public class ALotOfTestsTest extends JUnitBaseTest {
    @Parameters
    public static Collection<Object[]> data() {
        int TESTS_COUNT = 50;

        return IntStream.rangeClosed(1, TESTS_COUNT).mapToObj(val -> {
            TestConfig testConfig = new TestConfig(String.format("Test #%d", val), ResultType.getRandom());
            testConfig.setSteps(generateStepsCombination(3));
            return new TestConfig[]{ testConfig };
        }).collect(Collectors.toList());
    }

    public ALotOfTestsTest(TestConfig testConfig) {
        super(testConfig);
    }

    @Test
    public void buildTest() throws Exception {
        super.buildTest();
    }
}
