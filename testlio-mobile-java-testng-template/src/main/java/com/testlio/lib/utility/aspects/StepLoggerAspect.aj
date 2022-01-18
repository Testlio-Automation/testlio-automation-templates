package com.testlio.lib.utility.aspects;

import io.qameta.allure.Step;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import com.testlio.lib.utility.log.LogFactory;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Aspect
public class StepLoggerAspect {

    private static final Logger log = LogFactory.getLogger(StepLoggerAspect.class);

	@Pointcut("execution(* com.testlio.lib.utility.screenshot.DriverScreenshotUtility.*(..))")
	public void logsNotToMatch() {
	}

	@Pointcut("execution(* *(..))")
	public void logsToMatch() {
	}

	@Before("logsToMatch() && ! logsNotToMatch() && @annotation(io.qameta.allure.Step)")
	public void logBeforeMethod(JoinPoint joinPoint) {
		String text = getLogText(joinPoint);
		log.info("Step: " + text);
	}

	private String getLogText(JoinPoint joinPoint) {
		MethodSignature methodSignature = MethodSignature.class.cast(joinPoint.getSignature());
		Step stepClass = methodSignature.getMethod().getAnnotation(Step.class);

		if (stepClass != null) {
			String params = "";
			Object[] args = joinPoint.getArgs();
			params = ArrayUtils.isEmpty(args) ? "" : " : " + Stream.of(args).map(arg -> {
				if (arg != null) {
					return arg.toString();
				} else
					return "null";
			}).collect(Collectors.joining(", "));

			return stepClass.value() + params;
		} else
			return methodSignature.getMethod().getName();
	}
}