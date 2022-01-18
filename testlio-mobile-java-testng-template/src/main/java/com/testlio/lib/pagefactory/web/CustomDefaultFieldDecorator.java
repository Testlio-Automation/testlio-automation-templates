package com.testlio.lib.pagefactory.web;

import com.testlio.lib.pagefactory.annotations.Chunk;
import com.testlio.lib.pagefactory.annotations.NotResearchable;
import com.testlio.lib.pagefactory.WebPage;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;

@Slf4j
public class CustomDefaultFieldDecorator extends DefaultFieldDecorator {

    private WebDriver driver;
    private Field field;

    public CustomDefaultFieldDecorator(ElementLocatorFactory factory, WebDriver driver) {
        super(factory);
        this.driver = driver;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Object decorate(ClassLoader loader, Field field) {
        // TODO
        this.field = field;

        if (isAnnotatedAsChunk(field)) {
            return CriteriaWebPageFactory.initElements(driver, (Class<WebPage>) field.getType());
        }

        return super.decorate(loader, field);
    }

    private boolean isAnnotatedAsChunk(Field field) {
        return field.getAnnotation(Chunk.class) != null;
    }

    private boolean isAnnotatedAsNotResearchable(Field field) {
        return field.getAnnotation(NotResearchable.class) != null;
    }

    @Override
    protected WebElement proxyForLocator(ClassLoader loader, ElementLocator locator) {
        InvocationHandler handler = new CustomLocatingElementHandler(driver, locator,
                isAnnotatedAsNotResearchable(field));

        WebElement proxy;
        proxy = (WebElement) Proxy.newProxyInstance(loader,
                new Class[]{WebElement.class, WrapsElement.class, Locatable.class}, handler);
        return proxy;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected List<WebElement> proxyForListLocator(ClassLoader loader, ElementLocator locator) {
        InvocationHandler handler = new CustomLocatingElementListHandler(driver, locator,
                isAnnotatedAsNotResearchable(field));

        List<WebElement> proxy;
        proxy = (List<WebElement>) Proxy.newProxyInstance(loader, new Class[]{List.class}, handler);
        return proxy;
    }
}
