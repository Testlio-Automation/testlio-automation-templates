package com.testlio.tests;

import com.testlio.lib.BaseTest;
import com.testlio.pages.TestlioLoginWebPage;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Label;
import org.testng.annotations.Test;

import java.util.ResourceBundle;

import static com.testlio.constants.WebAppUrls.PLATFORM_TESTLIO_URL;
import static io.qameta.allure.Allure.getLifecycle;

/**
 * Before running/packaging this test, ensure,
 * that you created credentials.properties file
 * in directory src/test/resources with following content:
 *
 * user.email=<put your testlio account email>
 * user.password=<put your testlio account password>
 */
public class TestlioLoginTest extends BaseTest {

    /**
     * Test case, verifying, that Testlio user
     * is able to login to the Testlio platform
     */
    @Test
    public void testlioLoginTest() {
        Label label = new Label();
        label.setName("testlioManualTestID");
        label.setValue("636aad07-dbdc-419e-92d6-0d432c07895c");
        getLifecycle().updateTestCase(testResult -> testResult.getLabels().add(label));

        // Read credentials from properties file
        ResourceBundle credentials = ResourceBundle.getBundle("credentials");
        String userEmail = credentials.getString("user.email");
        String userPassword = credentials.getString("user.password");

        // Navigate to the initial web application page - TestlioLoginWebPage
        TestlioLoginWebPage testlioLoginWebPage =
                (new TestlioLoginWebPage(getWebDriver()))
                        .navigateTo(PLATFORM_TESTLIO_URL, TestlioLoginWebPage.class);

        // Input user's email address
        testlioLoginWebPage.inputEmail(userEmail);

        // Input user's password
        testlioLoginWebPage.inputPassword(userPassword);

        // Click on login button
        testlioLoginWebPage.clickLogin();
    }

}
