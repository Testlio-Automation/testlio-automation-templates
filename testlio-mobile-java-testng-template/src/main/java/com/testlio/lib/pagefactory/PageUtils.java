package com.testlio.lib.pagefactory;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.StaleElementReferenceException;

import java.util.Arrays;

public class PageUtils {

    private PageUtils() {}

	public static boolean isElementStaleReferenceExceptionThrown(Exception e) {
		return e instanceof StaleElementReferenceException
				|| ExceptionUtils.getStackTrace(e).contains("StaleElementReferenceException")
				|| ExceptionUtils.getStackTrace(e).contains("StaleObjectException")
				|| Arrays.asList(ExceptionUtils.getRootCauseStackTrace(e)).stream()
						.anyMatch(trace -> trace.contains("StaleObjectException"))
				|| Arrays.asList(ExceptionUtils.getRootCauseStackTrace(e)).stream()
						.anyMatch(trace -> trace.contains("StaleReferenceException"));
	}

	/**
	 * Returns true if obj is instance of any given class type
	 */
	public static boolean isInstanceOf(Object obj, Class first, Class... classes) {
		if (first.isInstance(obj)) {
			return true;
		}
		for (Class clazz : classes) {
			if (clazz.isInstance(obj)) {
				return true;
			}
		}
		return false;
	}

}
