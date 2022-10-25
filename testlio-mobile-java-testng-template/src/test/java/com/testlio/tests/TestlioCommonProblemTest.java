package com.testlio.tests;

import com.testlio.lib.BaseTest;

import org.testng.annotations.Test;

/**
 * Before running/packaging this test, ensure,
 * that you created credentials.properties file
 * in directory src/test/resources with following content:
 *
 * user.username=testlio
 * user.password=lovetesting
 */
public class TestlioCommonProblemTest extends BaseTest {

    /**
     * Test case for login view example
     */
    @Test
    public void testlioCommonProblemTest() {

        System.out.println("appium server never started in 100 seconds. Exiting");
        System.out.println("An unknown server-side error occured while processing the command. Original error: Samsung Galaxy was not in the list of connected devices");

    }

}
