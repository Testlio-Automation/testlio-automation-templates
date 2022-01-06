package com.testlio.lib.pagefactory.web;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.Field;

public class CustomDefaultElementLocatorFactory implements ElementLocatorFactory {

	private final SearchContext searchContext;

	public CustomDefaultElementLocatorFactory(SearchContext searchContext) {
		this.searchContext = searchContext;
	}

	public ElementLocator createLocator(Field field) {
		return new CustomDefaultElementLocator(searchContext, field);
	}

}
