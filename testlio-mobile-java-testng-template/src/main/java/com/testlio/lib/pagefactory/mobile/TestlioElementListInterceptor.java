package com.testlio.lib.pagefactory.mobile;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Method;
import java.util.List;

import static com.testlio.lib.pagefactory.mobile.TestlioThrowableUtil.extractReadableException;

public class TestlioElementListInterceptor extends TestlioInterceptorOfAListOfElements {

	TestlioElementListInterceptor(ElementLocator locator, WebDriver driver) {
		super(locator, driver);
	}

	@Override
	protected Object getObject(List<WebElement> elements, Method method, Object[] args) throws Throwable {
		try {
			return method.invoke(elements, args);
		} catch (Throwable t) {
			throw extractReadableException(t);
		}
	}

}
