package com.testlio.lib.pagefactory.mobile;

import io.appium.java_client.pagefactory.AndroidFindAll;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AndroidFindBys;
import io.appium.java_client.pagefactory.HowToUseLocators;
import io.appium.java_client.pagefactory.LocatorGroupStrategy;
import io.appium.java_client.pagefactory.WindowsFindAll;
import io.appium.java_client.pagefactory.WindowsFindBy;
import io.appium.java_client.pagefactory.WindowsFindBys;
import io.appium.java_client.pagefactory.bys.ContentMappedBy;
import io.appium.java_client.pagefactory.bys.ContentType;
import io.appium.java_client.pagefactory.bys.builder.AppiumByBuilder;
import io.appium.java_client.pagefactory.bys.builder.ByAll;
import io.appium.java_client.pagefactory.bys.builder.ByChained;
import io.appium.java_client.pagefactory.iOSXCUITFindAll;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBys;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.appium.java_client.pagefactory.bys.builder.HowToUseSelectors.BUILD_CHAINED;
import static io.appium.java_client.pagefactory.bys.builder.HowToUseSelectors.USE_ANY;
import static io.appium.java_client.pagefactory.bys.builder.HowToUseSelectors.USE_ONE;
import static java.lang.String.format;
import static java.util.Arrays.sort;
import static java.util.Optional.ofNullable;

public class TestlioDefaultElementByBuilder extends AppiumByBuilder {

    private static final String PRIORITY = "priority";
    private static final String VALUE = "value";
    private static final Class[] ANNOTATION_ARGUMENTS = new Class[0];
    private static final Object[] ANNOTATION_PARAMETERS = new Object[0];

    public TestlioDefaultElementByBuilder(String platform, String automation) {
        super(platform, automation);
    }

    private static void checkDisallowedAnnotationPairs(Annotation a1, Annotation a2) throws IllegalArgumentException {
        if (a1 != null && a2 != null) {
            throw new IllegalArgumentException("If you use a '@" + a1.getClass().getSimpleName() + "' annotation, " + "you must not also use a '@" + a2.getClass().getSimpleName() + "' annotation");
        }
    }

    private static By buildMobileBy(LocatorGroupStrategy locatorGroupStrategy, By[] bys) {
        if (bys.length == 0) {
            return null;
        } else {
            LocatorGroupStrategy strategy = (LocatorGroupStrategy) ofNullable(locatorGroupStrategy).orElse(LocatorGroupStrategy.CHAIN);
            return (By)(strategy.equals(LocatorGroupStrategy.ALL_POSSIBLE) ? new ByAll(bys) : new ByChained(bys));
        }
    }

    protected void assertValidAnnotations() {
        AnnotatedElement annotatedElement = this.annotatedElementContainer.getAnnotated();
        FindBy findBy = (FindBy)annotatedElement.getAnnotation(FindBy.class);
        FindBys findBys = (FindBys)annotatedElement.getAnnotation(FindBys.class);
        checkDisallowedAnnotationPairs(findBy, findBys);
        FindAll findAll = (FindAll)annotatedElement.getAnnotation(FindAll.class);
        checkDisallowedAnnotationPairs(findBy, findAll);
        checkDisallowedAnnotationPairs(findBys, findAll);
    }

    protected By buildDefaultBy() {
        AnnotatedElement annotatedElement = this.annotatedElementContainer.getAnnotated();
        By defaultBy = null;
        FindBy findBy = (FindBy)annotatedElement.getAnnotation(FindBy.class);
        if (findBy != null) {
            defaultBy = (new FindBy.FindByBuilder()).buildIt(findBy, (Field)annotatedElement);
        }

        if (defaultBy == null) {
            FindBys findBys = (FindBys)annotatedElement.getAnnotation(FindBys.class);
            if (findBys != null) {
                defaultBy = (new org.openqa.selenium.support.FindBys.FindByBuilder()).buildIt(findBys, (Field)annotatedElement);
            }
        }

        if (defaultBy == null) {
            FindAll findAll = (FindAll)annotatedElement.getAnnotation(FindAll.class);
            if (findAll != null) {
                defaultBy = (new org.openqa.selenium.support.FindAll.FindByBuilder()).buildIt(findAll, (Field)annotatedElement);
            }
        }

        return defaultBy;
    }

    private By[] getBys(Class<? extends Annotation> singleLocator, Class<? extends Annotation> chainedLocator, Class<? extends Annotation> allLocator) {
        TestlioDefaultElementByBuilder.AnnotationComparator comparator = new TestlioDefaultElementByBuilder.AnnotationComparator();
        AnnotatedElement annotatedElement = this.annotatedElementContainer.getAnnotated();
        List<Annotation> annotations = new ArrayList(Arrays.asList(annotatedElement.getAnnotationsByType(singleLocator)));

        List<By> result = new ArrayList();

        Iterator var9;
        annotations.addAll(Arrays.asList(annotatedElement.getAnnotationsByType(chainedLocator)));
        annotations.addAll(Arrays.asList(annotatedElement.getAnnotationsByType(allLocator)));
        annotations.sort(comparator);

        var9 = annotations.iterator();

        while (var9.hasNext()) {
            Annotation a = (Annotation) var9.next();
            Class<?> annotationClass = a.annotationType();
            if (singleLocator.equals(annotationClass)) {
                result.add(createBy(new Annotation[]{a}, USE_ONE));
            } else {
                Annotation[] subLocators;
                try {
                    Method value = annotationClass.getMethod("value", ANNOTATION_ARGUMENTS);
                    subLocators = (Annotation[]) value.invoke(a, ANNOTATION_PARAMETERS);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException var13) {
                    throw new ClassCastException(format("The annotation '%s' has no convenient '%s' method which returns array of annotations", annotationClass.getName(), "value"));
                }

                sort(subLocators, comparator);
                if (chainedLocator.equals(annotationClass)) {
                    result.add(createBy(subLocators, BUILD_CHAINED));
                } else if (allLocator.equals(annotationClass)) {
                    result.add(createBy(subLocators, USE_ANY));
                }
            }
        }

        return (By[])result.toArray(new By[result.size()]);
    }

    protected By buildMobileNativeBy() {
        AnnotatedElement annotatedElement = this.annotatedElementContainer.getAnnotated();
        HowToUseLocators howToUseLocators = (HowToUseLocators)annotatedElement.getAnnotation(HowToUseLocators.class);
        Optional<HowToUseLocators> howToUseLocatorsOptional = ofNullable(howToUseLocators);
        if (this.isAndroid()) {
            return buildMobileBy((LocatorGroupStrategy)howToUseLocatorsOptional.map(HowToUseLocators::androidAutomation).orElse(null), this.getBys(AndroidFindBy.class, AndroidFindBys.class, AndroidFindAll.class));
        } else if (!this.isIOSXcuit() && !this.isIOS()) {
            return this.isWindows() ? buildMobileBy((LocatorGroupStrategy)howToUseLocatorsOptional.map(HowToUseLocators::windowsAutomation).orElse(null), this.getBys(WindowsFindBy.class, WindowsFindBys.class, WindowsFindAll.class)) : null;
        } else {
            return buildMobileBy((LocatorGroupStrategy)howToUseLocatorsOptional.map(HowToUseLocators::iOSXCUITAutomation).orElse(null), this.getBys(iOSXCUITFindBy.class, iOSXCUITFindBys.class, iOSXCUITFindAll.class));
        }
    }

    public boolean isLookupCached() {
        AnnotatedElement annotatedElement = this.annotatedElementContainer.getAnnotated();
        return annotatedElement.getAnnotation(CacheLookup.class) != null;
    }

    private By returnMappedBy(By byDefault, By nativeAppBy) {
        Map<ContentType, By> contentMap = new HashMap();
        contentMap.put(ContentType.HTML_OR_DEFAULT, byDefault);
        contentMap.put(ContentType.NATIVE_MOBILE_SPECIFIC, nativeAppBy);
        return new ContentMappedBy(contentMap);
    }

    public By buildBy() {
        this.assertValidAnnotations();
        By defaultBy = this.buildDefaultBy();
        By mobileNativeBy = this.buildMobileNativeBy();
        String idOrName = ((Field)this.annotatedElementContainer.getAnnotated()).getName();
        //ByIdOrName defaultBy;
        if (defaultBy == null && mobileNativeBy == null) {
            defaultBy = new ByIdOrName(((Field)this.annotatedElementContainer.getAnnotated()).getName());
            //By
            mobileNativeBy = new By.ById(idOrName);
            return this.returnMappedBy(defaultBy, mobileNativeBy);
        } else if (defaultBy == null) {
            defaultBy = new ByIdOrName(((Field)this.annotatedElementContainer.getAnnotated()).getName());
            return this.returnMappedBy(defaultBy, mobileNativeBy);
        } else {
            return mobileNativeBy == null ? this.returnMappedBy(defaultBy, defaultBy) : this.returnMappedBy(defaultBy, mobileNativeBy);
        }
    }

    private static class AnnotationComparator implements Comparator<Annotation> {
        private AnnotationComparator() {
        }

        private static Method getPriorityMethod(Class<? extends Annotation> clazz) {
            try {
                return clazz.getMethod("priority", TestlioDefaultElementByBuilder.ANNOTATION_ARGUMENTS);
            } catch (NoSuchMethodException var1) {
                throw new ClassCastException(format("Class %s has no '%s' method", clazz.getName(), "priority"));
            }
        }

        private static int getPriorityValue(Method priorityMethod, Annotation annotation, Class<? extends Annotation> clazz) {
            try {
                return (Integer)priorityMethod.invoke(annotation, TestlioDefaultElementByBuilder.ANNOTATION_PARAMETERS);
            } catch (InvocationTargetException | IllegalAccessException var4) {
                throw new IllegalArgumentException(format("It is impossible to get priority. Annotation class: %s", clazz.toString()), var4);
            }
        }

        public int compare(Annotation o1, Annotation o2) {
            Class<? extends Annotation> c1 = o1.annotationType();
            Class<? extends Annotation> c2 = o2.annotationType();
            Method priority1 = getPriorityMethod(c1);
            Method priority2 = getPriorityMethod(c2);
            int p1 = getPriorityValue(priority1, o1, c1);
            int p2 = getPriorityValue(priority2, o2, c2);
            return Integer.signum(p1 - p2);
        }
    }
}
