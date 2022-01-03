package com.testlio.lib.pagefactory.mobile;

import io.appium.java_client.pagefactory.Widget;
import io.appium.java_client.pagefactory.bys.ContentType;
import io.appium.java_client.pagefactory.interceptors.InterceptorOfAListOfElements;
import io.appium.java_client.pagefactory.locator.CacheableLocator;
import io.appium.java_client.pagefactory.utils.ProxyFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.appium.java_client.pagefactory.utils.WebDriverUnpackUtility.getCurrentContentType;
import static java.util.Optional.ofNullable;
import static com.testlio.lib.pagefactory.mobile.TestlioThrowableUtil.extractReadableException;

public class TestlioWidgetListInterceptor extends InterceptorOfAListOfElements {

	private final Map<ContentType, Constructor<? extends Widget>> instantiationMap;
	private final List<Widget> cachedWidgets = new ArrayList<>();
	private final Class<? extends Widget> declaredType;
	private final Duration duration;
	private final WebDriver driver;
	private List<WebElement> cachedElements;

	TestlioWidgetListInterceptor(CacheableLocator locator, WebDriver driver,
			Map<ContentType, Constructor<? extends Widget>> instantiationMap, Class<? extends Widget> declaredType,
			Duration duration) {
		super(locator);
		this.instantiationMap = instantiationMap;
		this.declaredType = declaredType;
		this.duration = duration;
		this.driver = driver;
	}

	@Override
	protected Object getObject(List<WebElement> elements, Method method, Object[] args) throws Throwable {
		if (cachedElements == null || (locator != null && !((CacheableLocator) locator).isLookUpCached())) {
			cachedElements = elements;
			cachedWidgets.clear();

			ContentType type = null;
			for (WebElement element : cachedElements) {
				type = ofNullable(type).orElseGet(() -> getCurrentContentType(element));
				Class<?>[] params = new Class<?>[] { instantiationMap.get(type).getParameterTypes()[0] };
				cachedWidgets.add(ProxyFactory.getEnhancedProxy(declaredType, params, new Object[] { element },
						new TestlioWidgetInterceptor(null, driver, element, instantiationMap, duration)));
			}
		}
		try {
			return method.invoke(cachedWidgets, args);
		} catch (Throwable t) {
			throw extractReadableException(t);
		}
	}
}
