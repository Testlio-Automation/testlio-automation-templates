package com.testlio.lib.pagefactory.mobile;

import io.appium.java_client.pagefactory.Widget;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

public class TestlioWidgetConstructorUtil {

	private TestlioWidgetConstructorUtil() {
		super();
	}

	@SuppressWarnings("unchecked")
	static Constructor<? extends Widget> findConvenientConstructor(Class<? extends Widget> clazz) {
		Constructor<?>[] constructors = clazz.getDeclaredConstructors();
		for (Constructor<?> constructor : constructors) {
			Class<?>[] params = constructor.getParameterTypes();
			if (constructor.getParameterTypes().length != 1) {
				continue;
			}

			Class<?> param = params[0];
			if (WebElement.class.isAssignableFrom(param)) {
				constructor.setAccessible(true);
				return (Constructor<? extends Widget>) constructor;
			}
		}
		List<Constructor<?>> declared = Arrays.asList(clazz.getDeclaredConstructors());
		throw new NoSuchMethodError(
				clazz.getName() + " has no convenient constructor which could pass a " + WebElement.class.getName()
						+ " instance as a parameter. The actual list of constructors: " + declared.toString());
	}

}
