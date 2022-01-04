package com.testlio.lib.utility.log;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;

@Slf4j
public class AllureStepLogger {
    private final static DateFormat FORMATTER;

    static {
        String pattern = "HH:mm:ss.SSS z (d MMM yyyy)";
        FORMATTER = new SimpleDateFormat(pattern);
        FORMATTER.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public static void logMessage(boolean shouldLog, String message) {
        if (shouldLog) {
            logMessage(message);
        }
    }

    public static void logMessage(String message) {
        String time = FORMATTER.format(currentTimeMillis());
        String logMessage = format("%s - %s", time, message);
        logTimestampedMessage(logMessage);
    }

    @Step("{message}")
    private static void logTimestampedMessage(String message) {
        log.info(message);
    }

    public static void logError(boolean shouldLog, String message) {
        if (shouldLog) {
            logError(message);
        }
    }

    public static void logError(String message) {
        String time = FORMATTER.format(currentTimeMillis());
        String logMessage = format("%s - %s", time, message);
        logTimestampedError(logMessage);
    }

    @Step("{message}")
    public static void logTimestampedError(String message) {
        log.error(message);
    }
}

