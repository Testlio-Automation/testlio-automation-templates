package com.testlio.lib.pagefactory.mobile;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Method;

public abstract class TestlioInterceptorOfASingleElement implements MethodInterceptor {

	protected final ElementLocator locator;
	protected final WebDriver driver;
	protected final Boolean isNotResearchable;

	public TestlioInterceptorOfASingleElement(ElementLocator locator, WebDriver driver, Boolean isNotResearchable) {
		this.locator = locator;
		this.driver = driver;
		this.isNotResearchable = isNotResearchable;
	}

	protected abstract Object getObject(ElementLocator locator, Method method, Object[] args, boolean isNotResearchable)
			throws Throwable;

	/**
	 * Look at
	 * {@link MethodInterceptor#intercept(Object, Method, Object[], MethodProxy)}.
	 */
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

		if (method.getName().equals("toString") && args.length == 0) {
			return locator.toString();
		}

		if (Object.class.equals(method.getDeclaringClass())) {
			return proxy.invokeSuper(obj, args);
		}

		//deprecated usage
		if (WrapsDriver.class.isAssignableFrom(method.getDeclaringClass())
				&& method.getName().equals("getWrappedDriver")) {
			return driver;
		}

		return getObject(locator, method, args, isNotResearchable);
	}

}
