package com.testlio.lib.utility.helpers;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class ExecutionHelper {

    private ExecutionHelper() {}

    @Step("Wait {minutes} minutes")
    public static void sleepInMinutes(int minutes) {
        sleep(MINUTES, minutes);
    }

	@Step("Wait {seconds} seconds")
	public static void sleepInSeconds(int seconds) {
		sleep(SECONDS, seconds);
	}

    @Step("Wait {milliseconds} milliseconds")
    public static void sleepInMilliSeconds(int milliseconds) {
        sleep(MILLISECONDS, milliseconds);
    }

    private static void sleep(TimeUnit timeUnit, int delay) {
        try {
            timeUnit.sleep(delay);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

}
