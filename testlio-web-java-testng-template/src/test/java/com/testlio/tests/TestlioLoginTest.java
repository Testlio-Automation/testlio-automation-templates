package com.testlio.tests;

import com.testlio.lib.BaseTest;
import com.testlio.pages.TestlioLoginWebPage;
import org.testng.annotations.Test;

import java.util.ResourceBundle;

import static com.testlio.constants.WebAppUrls.PLATFORM_TESTLIO_URL;

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
