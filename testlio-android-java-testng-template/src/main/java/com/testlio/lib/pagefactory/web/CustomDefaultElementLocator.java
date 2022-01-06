package com.testlio.lib.pagefactory.web;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Field;
import java.util.List;

public class CustomDefaultElementLocator implements ElementLocator {

	private ElementSearcher elementSearcher = new ElementSearcher();

	private final SearchContext searchContext;
	private Field field;
	private WebDriver driver;

	private WebElement cachedElement;
	private List<WebElement> cachedElementList;

	private CustomAbstractAnnotations elementAnnotations;

	/**
	 * Creates a new element locator.
	 *
	 * @param searchContext
	 *            The context to use when finding the element
	 * @param field
	 *            The field on the Page Object that will hold the located value
	 */
	public CustomDefaultElementLocator(SearchContext searchContext, Field field) {
		this(searchContext, new CustomAnnotations(field));
		this.driver = (WebDriver) searchContext;
		this.field = field;
	}

	/**
	 * Use this constructor in order to process custom annotations.
	 *
	 * @param searchContext
	 *            The context to use when finding the element
	 * @param annotations
	 *            AbstractAnnotations class implementation
	 */
	public CustomDefaultElementLocator(SearchContext searchContext, CustomAbstractAnnotations annotations) {
		this.searchContext = searchContext;
		this.elementAnnotations = annotations;
	}

	public WebElement findElement() {
		return elementSearcher.findElement(driver, searchContext, elementAnnotations, cachedElement);
	}

	public List<WebElement> findElements() {
		return elementSearcher.findElements(driver, searchContext, elementAnnotations, cachedElementList);
	}

	@Override
	public String toString() {
		return field.getName() + " with locator " + elementAnnotations.buildBy().toString();
	}

}