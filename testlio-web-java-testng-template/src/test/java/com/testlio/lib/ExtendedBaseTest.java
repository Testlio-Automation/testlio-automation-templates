package com.testlio.lib;

import com.testlio.models.TestConfig;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

public class ExtendedBaseTest extends BaseTest implements ITest {
    private final ThreadLocal<String> testName = new ThreadLocal<>();

    public void test(TestConfig testConfig) throws Exception {
        AllureLifecycle lifecycle = Allure.getLifecycle();
        lifecycle.updateTestCase(testResult -> testResult.setName(testConfig.getName()));
        TestBuilder builder = new TestBuilder();
        builder.build(testConfig);
    }

    @BeforeMethod
    public void BeforeMethod(Method method, Object[] testData){
        TestConfig testConfig = (TestConfig) testData[0];
        testName.set(testConfig.getName());
    }

    @Override
    public String getTestName() {
        return testName.get();
    }
}
