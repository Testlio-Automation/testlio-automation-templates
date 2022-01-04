package com.testlio.lib.pagefactory;

import org.openqa.selenium.WebDriver;

import java.util.function.Function;

public interface IPageLoadedCriteria {

	Function<WebDriver, ?> isPageLoaded();

}