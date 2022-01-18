package com.testlio.tests;

import com.testlio.lib.BaseTest;
import com.testlio.pages.AlertPopup;
import com.testlio.pages.TestlioLoginScreen;
import com.testlio.pages.TestlioStaticPageScreen;
import org.testng.annotations.Test;

import java.util.ResourceBundle;

import static com.testlio.constants.MobileAppStrings.SUCCESSFUL_LOGIN_ALERT_TITLE;
import static com.testlio.lib.pagefactory.TestlioPageFactory.initMobileElements;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Before running/packaging this test, ensure,
 * that you created credentials.properties file
 * in directory src/test/resources with following content:
 *
 * user.username=testlio
 * user.password=lovetesting
 */
public class TestlioLoginTest extends BaseTest {

    /**
     * Test case for login view example
     */
    @Test
    public void testlioLoginTest() {

        // Read credentials from properties file
        ResourceBundle credentials = ResourceBundle.getBundle("credentials");
        String userUsername = credentials.getString("user.username");
        String userPassword = credentials.getString("user.password");

        // Initialize the initial application page - TestlioStaticPageScreen
        TestlioStaticPageScreen testlioStaticPageScreen = initMobileElements(getMobileDriver(), TestlioStaticPageScreen.class);

        // Navigate to the login screen - TestlioLoginScreen
        TestlioLoginScreen testlioLoginScreen = testlioStaticPageScreen.goToLoginScreenTab();

        // Input user's username
        testlioLoginScreen.inputUsername(userUsername);

        // Input user's password
        testlioLoginScreen.inputPassword(userPassword);

        // Click on login button and expect appearing the alert
        AlertPopup alertPopup = testlioLoginScreen.clickLogin();

        // Check that login is successful
        assertThat(alertPopup.getTitleString()).isEqualTo(SUCCESSFUL_LOGIN_ALERT_TITLE);

    }

}
