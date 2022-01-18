package com.testlio.lib.pagefactory.mobile;

import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.StaleElementReferenceException;

import java.lang.reflect.InvocationTargetException;

public class TestlioThrowableUtil {

	private static final String INVALID_SELECTOR_PATTERN = "Invalid locator strategy:";

	private TestlioThrowableUtil() {}

	protected static boolean isInvalidSelectorRootCause(Throwable e) {
		if (e == null) {
			return false;
		}

		if (InvalidSelectorException.class.isAssignableFrom(e.getClass())) {
			return true;
		}

		if (String.valueOf(e.getMessage()).contains(INVALID_SELECTOR_PATTERN)
				|| String.valueOf(e.getMessage()).contains("Locator Strategy \\w+ is not supported")) {
			return true;
		}

		return isInvalidSelectorRootCause(e.getCause());
	}

	protected static boolean isStaleElementReferenceException(Throwable e) {
		if (e == null) {
			return false;
		}

		if (StaleElementReferenceException.class.isAssignableFrom(e.getClass())) {
			return true;
		}

		return isStaleElementReferenceException(e.getCause());
	}

	protected static Throwable extractReadableException(Throwable e) {
		if (!RuntimeException.class.equals(e.getClass()) && !InvocationTargetException.class.equals(e.getClass())) {
			return e;
		}

		return extractReadableException(e.getCause());
	}
}
