package com.testlio.lib.pagefactory.mobile;

import com.google.common.collect.ImmutableList;
import com.testlio.lib.properties.Execution;
import io.appium.java_client.HasSessionDetails;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.pagefactory.AppiumElementLocatorFactory;
import io.appium.java_client.pagefactory.Widget;
import io.appium.java_client.pagefactory.bys.ContentType;
import io.appium.java_client.pagefactory.locator.CacheableLocator;
import io.appium.java_client.windows.WindowsElement;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.appium.java_client.pagefactory.utils.ProxyFactory.getEnhancedProxy;
import static io.appium.java_client.pagefactory.utils.WebDriverUnpackUtility.unpackWebDriverFromSearchContext;
import static java.util.Optional.ofNullable;

public class TestlioAppiumFieldDecorator implements FieldDecorator {

	private static final List<Class<? extends WebElement>> availableElementClasses = ImmutableList.of(WebElement.class,
			RemoteWebElement.class, MobileElement.class, AndroidElement.class, IOSElement.class, WindowsElement.class);
	private static final long DEFAULT_TIMEOUT = 1;

	private final WebDriver webDriver;
	private final TestlioAppiumDefaultFieldDecorator testlioAppiumDefaultFieldDecorator;
	private final AppiumElementLocatorFactory widgetLocatorFactory;
	private String platform;
	private String automation;
	private final Duration duration;

	public TestlioAppiumFieldDecorator(SearchContext context, long timeout) {
		this(context, Duration.ofSeconds(timeout));
	}

	/**
	 * Creates field decorator based on {@link SearchContext} and timeout
	 * {@code duration}.
	 *
	 * @param context
	 *            is an instance of {@link SearchContext} It may be the instance of
	 *            {@link WebDriver} or {@link WebElement} or {@link Widget} or some
	 *            other user's extension/implementation.
	 * @param duration
	 *            is a desired duration of the waiting for an element presence.
	 */
	public TestlioAppiumFieldDecorator(SearchContext context, Duration duration) {
		this.webDriver = unpackWebDriverFromSearchContext(context);

		HasSessionDetails hasSessionDetails = ofNullable(this.webDriver).map(driver -> {
			if (!HasSessionDetails.class.isAssignableFrom(webDriver.getClass())) {
				return null;
			}
			return HasSessionDetails.class.cast(webDriver);
		}).orElse(null);

		if (hasSessionDetails == null) {
			platform = null;
			automation = null;
		} else {
			try {
				platform = hasSessionDetails.getPlatformName();
				automation = hasSessionDetails.getAutomationName();
			} catch (Exception e) {
				platform = Execution.properties().getMobileDevicePlatform();
				automation = Execution.properties().getAppiumAutomationName();
			}
		}

		this.duration = duration;

        testlioAppiumDefaultFieldDecorator = new TestlioAppiumDefaultFieldDecorator(
		        new AppiumElementLocatorFactory(context, duration, new TestlioDefaultElementByBuilder(platform, automation)),
                availableElementClasses, webDriver, platform, automation);

		widgetLocatorFactory = new AppiumElementLocatorFactory(context, duration,
				new TestlioWidgetByBuilder(platform, automation));
	}

	public TestlioAppiumFieldDecorator(SearchContext context) {
		this(context, DEFAULT_TIMEOUT);
	}

	/**
	 * Decorated page object {@code field}.
	 *
	 * @param ignored
	 *            class loader is ignored by current implementation
	 * @param field
	 *            is {@link Field} of page object which is supposed to be decorated.
	 * @return a field value or null.
	 */
	public Object decorate(ClassLoader ignored, Field field) {
		Object result = testlioAppiumDefaultFieldDecorator.decorate(ignored, field);
		if (result != null) {
			return result;
		}

		return decorateWidget(field);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object decorateWidget(Field field) {
		Class<?> type = field.getType();
		if (!Widget.class.isAssignableFrom(type) && !List.class.isAssignableFrom(type)) {
			return null;
		}

		Class<? extends Widget> widgetType;
		boolean isAlist = false;
		if (List.class.isAssignableFrom(type)) {
			isAlist = true;
			Type genericType = field.getGenericType();
			if (!(genericType instanceof ParameterizedType)) {
				return null;
			}

			Type listType = ((ParameterizedType) genericType).getActualTypeArguments()[0];

			if (ParameterizedType.class.isAssignableFrom(listType.getClass())) {
				listType = ((ParameterizedType) listType).getRawType();
			}

			if (listType instanceof Class) {
				if (!Widget.class.isAssignableFrom((Class) listType)) {
					return null;
				}
				widgetType = Class.class.cast(listType);
			} else {
				return null;
			}

		} else {
			widgetType = (Class<? extends Widget>) field.getType();
		}

		CacheableLocator locator = widgetLocatorFactory.createLocator(field);
		Map<ContentType, Constructor<? extends Widget>> map = TestlioOverrideWidgetReader.read(widgetType, field, platform);

		if (isAlist) {
			return getEnhancedProxy(ArrayList.class,
					new TestlioWidgetListInterceptor(locator, webDriver, map, widgetType, duration));
		}

		Constructor<? extends Widget> constructor = TestlioWidgetConstructorUtil.findConvenientConstructor(widgetType);
		return getEnhancedProxy(widgetType, new Class[] { constructor.getParameterTypes()[0] },
				new Object[] { testlioAppiumDefaultFieldDecorator.proxyForElement(locator, false) },
				new TestlioWidgetInterceptor(locator, webDriver, null, map, duration));
	}

}
