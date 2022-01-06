package com.testlio.lib.utility;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.math.NumberUtils.isParsable;

public class TimeUtil {

    private TimeUtil() {}

	public static int timeStringToSeconds(String timeString) {
		timeString = valueOf(timeString).trim();

		int secondsFromMinutes = 0;
		int seconds = 0;
		String[] array = timeString.split(":");
		if (array.length == 2) {
			String secondsFromMinutesString = array[0];
			String secondsString = array[1];

			if (isParsable(secondsFromMinutesString)) {
				secondsFromMinutes = parseInt(secondsFromMinutesString) * 60;
			}

			if (isParsable(secondsString)) {
				seconds = parseInt(secondsString);
			}
		}
		return secondsFromMinutes + seconds;
	}
}
