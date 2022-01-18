package com.testlio.lib.pagefactory.mobile;

import io.appium.java_client.pagefactory.OverrideWidget;
import io.appium.java_client.pagefactory.Widget;
import io.appium.java_client.pagefactory.bys.ContentType;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class TestlioOverrideWidgetReader {

    private static final Class<? extends Widget> EMPTY = Widget.class;
    private static final String HTML = "html";
    private static final String ANDROID_UI_AUTOMATOR = "androidUIAutomator";
    private static final String IOS_XCUIT_AUTOMATION = "iOSXCUITAutomation";
    private static final String WINDOWS_AUTOMATION = "windowsAutomation";

    TestlioOverrideWidgetReader() {
    }

    private static Class<? extends Widget> getConvenientClass(Class<? extends Widget> declaredClass, AnnotatedElement annotatedElement, String method) {
        OverrideWidget overrideWidget = (OverrideWidget)annotatedElement.getAnnotation(OverrideWidget.class);

        Class convenientClass;
        try {
            if (overrideWidget == null || (convenientClass = (Class)OverrideWidget.class.getDeclaredMethod(method).invoke(overrideWidget)).equals(EMPTY)) {
                convenientClass = declaredClass;
            }
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException var6) {
            throw new RuntimeException(var6);
        }

        if (!declaredClass.isAssignableFrom(convenientClass)) {
            throw new IllegalArgumentException(new InstantiationException(declaredClass.getName() + " is not assignable from " + convenientClass.getName()));
        } else {
            return convenientClass;
        }
    }

    static Class<? extends Widget> getDefaultOrHTMLWidgetClass(Class<? extends Widget> declaredClass, AnnotatedElement annotatedElement) {
        return getConvenientClass(declaredClass, annotatedElement, "html");
    }

    static Class<? extends Widget> getMobileNativeWidgetClass(Class<? extends Widget> declaredClass, AnnotatedElement annotatedElement, String platform) {
        String transformedPlatform = String.valueOf(platform).toUpperCase().trim();
        if ("Android".equalsIgnoreCase(transformedPlatform)) {
            return getConvenientClass(declaredClass, annotatedElement, "androidUIAutomator");
        } else if ("iOS".equalsIgnoreCase(transformedPlatform)) {
            return getConvenientClass(declaredClass, annotatedElement, "iOSXCUITAutomation");
        } else {
            return "Windows".equalsIgnoreCase(transformedPlatform) ? getConvenientClass(declaredClass, annotatedElement, "windowsAutomation") : getDefaultOrHTMLWidgetClass(declaredClass, annotatedElement);
        }
    }

    private static Constructor<? extends Widget> getConstructorOfADefaultOrHTMLWidget(Class<? extends Widget> declaredClass, AnnotatedElement annotatedElement) {
        Class<? extends Widget> clazz = getDefaultOrHTMLWidgetClass(declaredClass, annotatedElement);
        return TestlioWidgetConstructorUtil.findConvenientConstructor(clazz);
    }

    private static Constructor<? extends Widget> getConstructorOfAMobileNativeWidgets(Class<? extends Widget> declaredClass, AnnotatedElement annotatedElement, String platform) {
        Class<? extends Widget> clazz = getMobileNativeWidgetClass(declaredClass, annotatedElement, platform);
        return TestlioWidgetConstructorUtil.findConvenientConstructor(clazz);
    }

    protected static Map<ContentType, Constructor<? extends Widget>> read(Class<? extends Widget> declaredClass, AnnotatedElement annotatedElement, String platform) {
        Map<ContentType, Constructor<? extends Widget>> result = new HashMap();
        result.put(ContentType.HTML_OR_DEFAULT, getConstructorOfADefaultOrHTMLWidget(declaredClass, annotatedElement));
        result.put(ContentType.NATIVE_MOBILE_SPECIFIC, getConstructorOfAMobileNativeWidgets(declaredClass, annotatedElement, platform));
        return result;
    }
}
