package com.testlio.lib.pagefactory.web;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;
import static org.openqa.selenium.By.partialLinkText;
import static org.openqa.selenium.By.tagName;
import static org.openqa.selenium.By.xpath;

public abstract class CustomAbstractAnnotations extends AbstractAnnotations {

	/**
	 * Defines how to transform given object (field, class, etc) into
	 * {@link org.openqa.selenium.By} class used by webdriver to locate elements.
	 *
	 * @return By object
	 */
	public abstract By buildBy();

	/**
	 * Defines whether or not given element should be returned from cache on further
	 * calls.
	 *
	 * @return boolean if lookup cached
	 */
	public abstract boolean isLookupCached();

	/**
	 * Defines whether element is not research-able by element searcher
	 */
	public abstract boolean isNotResearchable();

	protected By buildByFromFindBys(FindBys findBys) {
		assertValidFindBys(findBys);

		FindBy[] findByArray = findBys.value();
		By[] byArray = new By[findByArray.length];
		for (int i = 0; i < findByArray.length; i++) {
			byArray[i] = buildByFromFindBy(findByArray[i]);
		}

		return new ByChained(byArray);
	}

	protected By buildBysFromFindByOneOf(FindAll findBys) {
		assertValidFindAll(findBys);

		FindBy[] findByArray = findBys.value();
		By[] byArray = new By[findByArray.length];
		for (int i = 0; i < findByArray.length; i++) {
			byArray[i] = buildByFromFindBy(findByArray[i]);
		}

		return new ByAll(byArray);
	}

	protected By buildByFromFindBy(FindBy findBy) {
		assertValidFindBy(findBy);

		By ans = buildByFromShortFindBy(findBy);
		if (ans == null) {
			ans = buildByFromLongFindBy(findBy);
		}

		return ans;
	}

	protected By buildByFromLongFindBy(FindBy findBy) {
		How how = findBy.how();
		String using = findBy.using();

		switch (how) {
		case CLASS_NAME:
			return className(using);

		case CSS:
			return cssSelector(using);

		case ID:
		case UNSET:
			return id(using);

		case ID_OR_NAME:
			return new ByIdOrName(using);

		case LINK_TEXT:
			return linkText(using);

		case NAME:
			return By.name(using);

		case PARTIAL_LINK_TEXT:
			return partialLinkText(using);

		case TAG_NAME:
			return tagName(using);

		case XPATH:
			return xpath(using);

		default:
			// Note that this shouldn't happen (eg, the above matches all
			// possible values for the How enum)
			throw new IllegalArgumentException("Cannot determine how to locate element ");
		}
	}

	protected By buildByFromShortFindBy(FindBy findBy) {
		if (isNotEmpty(findBy.className()))
			return className(findBy.className());

		if (isNotEmpty(findBy.css()))
			return cssSelector(findBy.css());

		if (isNotEmpty(findBy.id()))
			return id(findBy.id());

		if (isNotEmpty(findBy.linkText()))
			return linkText(findBy.linkText());

		if (isNotEmpty(findBy.name()))
			return By.name(findBy.name());

		if (isNotEmpty(findBy.partialLinkText()))
			return partialLinkText(findBy.partialLinkText());

		if (isNotEmpty(findBy.tagName()))
			return tagName(findBy.tagName());

		if (isNotEmpty(findBy.xpath()))
			return xpath(findBy.xpath());

		// Fall through
		return null;
	}

	private void assertValidFindBys(FindBys findBys) {
		for (FindBy findBy : findBys.value()) {
			assertValidFindBy(findBy);
		}
	}

	private void assertValidFindAll(FindAll findBys) {
		for (FindBy findBy : findBys.value()) {
			assertValidFindBy(findBy);
		}
	}

	private void assertValidFindBy(FindBy findBy) {
		if (findBy.how() != null && findBy.using() == null) {
		    throw new IllegalArgumentException("If you set the 'how' property, you must also set 'using'");
		}

		Set<String> finders = new HashSet<>();
		if (isNotEmpty(findBy.using()))
			finders.add("how: " + findBy.using());
		if (isNotEmpty(findBy.className()))
			finders.add("class name:" + findBy.className());
		if (isNotEmpty(findBy.css()))
			finders.add("css:" + findBy.css());
		if (isNotEmpty(findBy.id()))
			finders.add("id: " + findBy.id());
		if (isNotEmpty(findBy.linkText()))
			finders.add("link text: " + findBy.linkText());
		if (isNotEmpty(findBy.name()))
			finders.add("name: " + findBy.name());
		if (isNotEmpty(findBy.partialLinkText()))
			finders.add("partial link text: " + findBy.partialLinkText());
		if (isNotEmpty(findBy.tagName()))
			finders.add("tag name: " + findBy.tagName());
		if (isNotEmpty(findBy.xpath()))
			finders.add("xpath: " + findBy.xpath());

		// A zero count is okay: it means to look by name or id.
		if (finders.size() > 1) {
			throw new IllegalArgumentException(
					format("You must specify at most one location strategy. Number found: %d (%s)",
							finders.size(), finders.toString()));
		}
	}

}
