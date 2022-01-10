package com.testlio.lib.pagefactory.mobile;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class TestlioInterceptorOfAListOfElements implements MethodInterceptor {

	protected final ElementLocator locator;
	protected final WebDriver driver;

	public TestlioInterceptorOfAListOfElements(ElementLocator locator, WebDriver driver) {
		this.locator = locator;
		this.driver = driver;
	}

	protected abstract Object getObject(List<WebElement> elements, Method method, Object[] args)
			throws Throwable;

	/**
	 * Look at
	 * {@link MethodInterceptor#intercept(Object, Method, Object[], MethodProxy)}.
	 */
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		if (Object.class.equals(method.getDeclaringClass())) {
			return proxy.invokeSuper(obj, args);
		}

		List<WebElement> realElements = new ArrayList<>(locator.findElements());
		return getObject(realElements, method, args);
	}
}
