package com.testlio.lib.pagefactory.mobile;

import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import com.testlio.lib.pagefactory.MobileScreen;
import com.testlio.lib.pagefactory.annotations.Widget;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static java.lang.String.format;

@Slf4j
public class MobilePageFactory extends PageFactory {

	public static <T extends MobileScreen> T initElements(AppiumDriver<WebElement> driver, Class<T> pageClassToProxy) {
		T page = instantiatePage(driver, pageClassToProxy);
		initElements(driver, page);
		return page;
	}

	private static void initElements(AppiumDriver<WebElement> driver, Object page) {
		initElements(getAppiumDecorator(driver), page);
		initElementsForWidget(driver, page);
	}

	private static void initElementsForWidget(AppiumDriver<WebElement> driver, Object page) {
		Arrays.stream(page.getClass().getDeclaredFields()).forEach(field -> {
			Annotation annotation = field.getAnnotation(Widget.class);
			if (annotation != null) {
				log.info(format("Found Widget chunk annotation for field: %s", field.getName()));
				field.setAccessible(true);
				Class<?> widgetClazz = field.getType();
				log.info(format("Widget found is: %s", widgetClazz.getName()));
				log.info("Initializing widget field with page object");
				Object widget = instantiatePage(driver, widgetClazz);
				log.info(format("Page object for widget created: %s", widget));
				initElements(getAppiumDecorator(driver), widget);
				initElementsForWidget(driver, widget);

				try {
					field.set(page, widget);
				} catch (IllegalArgumentException e1) {
					log.error(e1.getMessage());
				} catch (IllegalAccessException e2) {
                    log.error(e2.getMessage());
				}
			}
		});
	}

	private static <T> T instantiatePage(AppiumDriver<WebElement> driver, Class<T> pageClassToProxy) {
		try {
			try {
				Constructor<T> constructor = pageClassToProxy.getConstructor(AppiumDriver.class);
				return constructor.newInstance(driver);
			} catch (NoSuchMethodException e) {
				return pageClassToProxy.newInstance();
			}
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new IllegalStateException(e);
		}
	}

	private static TestlioAppiumFieldDecorator getAppiumDecorator(AppiumDriver<WebElement> driver) {
		return new TestlioAppiumFieldDecorator(driver);
	}
}