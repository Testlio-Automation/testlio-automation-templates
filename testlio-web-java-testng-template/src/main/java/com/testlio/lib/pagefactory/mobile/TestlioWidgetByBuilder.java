package com.testlio.lib.pagefactory.mobile;

import org.openqa.selenium.By;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import static java.util.Optional.ofNullable;

public class TestlioWidgetByBuilder extends TestlioDefaultElementByBuilder {

    public TestlioWidgetByBuilder(String platform, String automation) {
        super(platform, automation);
    }

    private static Class<?> getClassFromAListField(Field field) {
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) {
            return null;
        } else {
            Type listType = ((ParameterizedType)genericType).getActualTypeArguments()[0];
            if (ParameterizedType.class.isAssignableFrom(listType.getClass())) {
                listType = ((ParameterizedType)listType).getRawType();
            }

            return (Class)listType;
        }
    }

    private By getByFromDeclaredClass(TestlioWidgetByBuilder.WhatIsNeeded whatIsNeeded) {
        AnnotatedElement annotatedElement = this.annotatedElementContainer.getAnnotated();
        Field field = (Field)Field.class.cast(annotatedElement);
        By result = null;

        try {
            Class declaredClass;
            if (List.class.isAssignableFrom(field.getType())) {
                declaredClass = getClassFromAListField(field);
            } else {
                declaredClass = field.getType();
            }

            Class convenientClass;
            if (whatIsNeeded.equals(TestlioWidgetByBuilder.WhatIsNeeded.DEFAULT_OR_HTML)) {
                convenientClass = TestlioOverrideWidgetReader.getDefaultOrHTMLWidgetClass(declaredClass, field);
            } else {
                convenientClass = TestlioOverrideWidgetReader.getMobileNativeWidgetClass(declaredClass, field, this.platform);
            }

            for(; result == null && !convenientClass.equals(Object.class); convenientClass = convenientClass.getSuperclass()) {
                this.setAnnotated(convenientClass);
                if (whatIsNeeded.equals(TestlioWidgetByBuilder.WhatIsNeeded.DEFAULT_OR_HTML)) {
                    result = super.buildDefaultBy();
                } else {
                    result = super.buildMobileNativeBy();
                }
            }

            By var8 = result;
            return var8;
        } finally {
            if (field != null) {
                this.setAnnotated(field);
            }

        }
    }

    protected By buildDefaultBy() {
        return (By) ofNullable(super.buildDefaultBy()).orElseGet(() -> {
            return this.getByFromDeclaredClass(TestlioWidgetByBuilder.WhatIsNeeded.DEFAULT_OR_HTML);
        });
    }

    protected By buildMobileNativeBy() {
        return (By) ofNullable(super.buildMobileNativeBy()).orElseGet(() -> {
            return this.getByFromDeclaredClass(TestlioWidgetByBuilder.WhatIsNeeded.MOBILE_NATIVE);
        });
    }

    private static enum WhatIsNeeded {
        DEFAULT_OR_HTML,
        MOBILE_NATIVE;

        private WhatIsNeeded() {
        }
    }

}
