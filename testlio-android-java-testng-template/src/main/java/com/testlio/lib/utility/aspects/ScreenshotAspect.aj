package com.testlio.lib.utility.aspects;

import io.qameta.allure.Step;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import com.testlio.lib.driver.DriverProvider;
import com.testlio.lib.properties.Execution;
import com.testlio.lib.utility.log.LogFactory;
import com.testlio.lib.utility.screenshot.DriverScreenshotUtility;

@Aspect
public class ScreenshotAspect {
    private static final Logger log = LogFactory.getLogger(ScreenshotAspect.class);

	@Before("execution(* *(..)) && @annotation(com.testlio.lib.utility.screenshot.annotations.MakeScreenshot)")
	public void screenBeforeMethod(JoinPoint joinPoint) {
		captureScreenOnStep(joinPoint, " [before step]");
	}

	@After("execution(* *(..)) && @annotation(com.testlio.lib.utility.screenshot.annotations.MakeScreenshot)")
	public void screenAfterMethod(JoinPoint joinPoint) { captureScreenOnStep(joinPoint, " [after step]"); }

	private void captureScreenOnStep(JoinPoint joinPoint, String textSuffix) {
		if (Execution.properties().isTakesScreenshotOnEachStep()) {
            //https://github.com/cbeust/testng/issues/914
            if (DriverProvider.wasDriverCreated()) {
			    String text = getScreenshotText(joinPoint) + textSuffix;
                DriverScreenshotUtility.getScreenshotAndAttachToReport(text, DriverProvider.getCreatedDriver());
            }
		}
	}

	private String getScreenshotText(JoinPoint joinPoint) {
		MethodSignature methodSignature = MethodSignature.class.cast(joinPoint.getSignature());
		Step stepClass = methodSignature.getMethod().getAnnotation(Step.class);
		if (stepClass != null) {
			return stepClass.value();
		} else
			return methodSignature.getMethod().getName();
	}
}