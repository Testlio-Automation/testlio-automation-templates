package com.testlio.lib.utility.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtility {

    private DateTimeUtility() {}

	public static String getCurrentDate() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MM yyyy - HH:mm:ss"));
	}

	public static String getCurrentDateForScreenshot() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss"));
	}

}
