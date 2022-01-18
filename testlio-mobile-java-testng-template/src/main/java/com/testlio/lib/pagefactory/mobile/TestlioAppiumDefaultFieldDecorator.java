package com.testlio.lib.pagefactory.mobile;

import com.testlio.lib.pagefactory.annotations.NotResearchable;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static io.appium.java_client.internal.ElementMap.getElementClass;
import static io.appium.java_client.pagefactory.utils.ProxyFactory.getEnhancedProxy;

public class TestlioAppiumDefaultFieldDecorator extends DefaultFieldDecorator {

    private List<Class<? extends WebElement>> availableElementClasses;
    private final WebDriver webDriver;
    private String platform;
    private String automation;

    public TestlioAppiumDefaultFieldDecorator(ElementLocatorFactory factory, List<Class<? extends WebElement>> availableElementClasses,
                                              WebDriver webDriver, String platform, String automation) {
        super(factory);
        this.availableElementClasses = availableElementClasses;
        this.webDriver = webDriver;
        this.platform = platform;
        this.automation = automation;
    }

    @Override
    public Object decorate(ClassLoader loader, Field field) {
        if (!(WebElement.class.isAssignableFrom(field.getType()) || isDecoratableList(field))) {
            return null;
        }

        ElementLocator locator = factory.createLocator(field);
        if (locator == null) {
            return null;
        }
        if (WebElement.class.isAssignableFrom(field.getType())) {
            return proxyForElement(locator, isAnnotatedAsNotResearchable(field));
        } else if (List.class.isAssignableFrom(field.getType())) {
            return proxyForListLocator(loader, locator);
        } else {
            return null;
        }
    }

    @Override
    protected WebElement proxyForLocator(ClassLoader ignored, ElementLocator locator) {
        return proxyForElement(locator, false);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected List<WebElement> proxyForListLocator(ClassLoader ignored, ElementLocator locator) {
        TestlioElementListInterceptor elementInterceptor = new TestlioElementListInterceptor(locator,
                webDriver);
        return getEnhancedProxy(ArrayList.class, elementInterceptor);
    }

    @Override
    protected boolean isDecoratableList(Field field) {
        if (!List.class.isAssignableFrom(field.getType())) {
            return false;
        }

        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) {
            return false;
        }

        Type listType = ((ParameterizedType) genericType).getActualTypeArguments()[0];

        for (Class<? extends WebElement> webElementClass : availableElementClasses) {
            if (webElementClass.equals(listType)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAnnotatedAsNotResearchable(Field field) {
        return field.getAnnotation(NotResearchable.class) != null;
    }

    public WebElement proxyForElement(ElementLocator locator, boolean isNotResearchable) {
        TestlioElementInterceptor elementInterceptor = new TestlioElementInterceptor(locator, webDriver,
                isNotResearchable);
        return getEnhancedProxy(getElementClass(platform, automation), elementInterceptor);
    }

}