package com.testlio.lib.driver;

import org.openqa.selenium.WebDriver;

public interface IDriverProvidable {

	<T extends WebDriver> T createDriver();

	void killDriver();

	<T extends WebDriver> T getDriver();

}