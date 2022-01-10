package com.testlio.lib.pagefactory.mobile;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.Widget;
import io.appium.java_client.pagefactory.bys.ContentType;
import io.appium.java_client.pagefactory.interceptors.InterceptorOfASingleElement;
import io.appium.java_client.pagefactory.locator.CacheableLocator;
import net.sf.cglib.proxy.MethodProxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static io.appium.java_client.pagefactory.utils.WebDriverUnpackUtility.getCurrentContentType;
import static com.testlio.lib.pagefactory.mobile.TestlioThrowableUtil.extractReadableException;

public class TestlioWidgetInterceptor extends InterceptorOfASingleElement {

	private final Map<ContentType, Constructor<? extends Widget>> instantiationMap;
	private final Map<ContentType, Widget> cachedInstances = new HashMap<>();
	private final Duration duration;
	private WebElement cachedElement;

	TestlioWidgetInterceptor(CacheableLocator locator, WebDriver driver, WebElement cachedElement,
			Map<ContentType, Constructor<? extends Widget>> instantiationMap, Duration duration) {
		super(locator, driver);
		this.cachedElement = cachedElement;
		this.instantiationMap = instantiationMap;
		this.duration = duration;
	}

	@Override
	protected Object getObject(WebElement element, Method method, Object[] args) throws Throwable {
		ContentType type = getCurrentContentType(element);
		if (cachedElement == null || (locator != null && !((CacheableLocator) locator).isLookUpCached())
				|| cachedInstances.size() == 0) {
			cachedElement = element;

			Constructor<? extends Widget> constructor = instantiationMap.get(type);
			Class<? extends Widget> clazz = constructor.getDeclaringClass();

			int modifiers = clazz.getModifiers();
			if (Modifier.isAbstract(modifiers)) {
				throw new InstantiationException(clazz.getName() + " is abstract so " + "it can't be instantiated");
			}

			Widget widget = constructor.newInstance(cachedElement);
			cachedInstances.put(type, widget);
			PageFactory.initElements(new AppiumFieldDecorator(widget, duration), widget);
		}
		try {
			method.setAccessible(true);
			return method.invoke(cachedInstances.get(type), args);
		} catch (Throwable t) {
			throw extractReadableException(t);
		}
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		if (locator != null) {
			return super.intercept(obj, method, args, proxy);
		}
		return getObject(cachedElement, method, args);
	}
}
