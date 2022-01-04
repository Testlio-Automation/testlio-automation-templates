package com.testlio.lib.utility;

import static java.lang.String.valueOf;

public class FileNameUtil {

    private FileNameUtil() {}

	public static String makeValidFileOrDirectoryName(String input) {
		String result = valueOf(input).trim().toLowerCase()
				.replaceAll(Constant.REPLACEABLE_FILE_NAME_SYMBOLS, Constant.TARGET_FILE_NAME_SYMBOL)
				.replaceAll(Constant.DOUBLE_TARGET_FILE_NAME_SYMBOL, Constant.TARGET_FILE_NAME_SYMBOL)
				.replaceAll(Constant.DOUBLE_TARGET_FILE_NAME_SYMBOL, Constant.TARGET_FILE_NAME_SYMBOL)
				.replaceAll(Constant.DOUBLE_TARGET_FILE_NAME_SYMBOL, Constant.TARGET_FILE_NAME_SYMBOL);

		if (result.isEmpty()) {
			return Constant.SYSTEM_FILE_SEPARATOR;
		}

		if (result.lastIndexOf(Constant.TARGET_FILE_NAME_SYMBOL) == result.length() - 1) {
			result = result.substring(0, result.length() - 1);
		}

		if (result.substring(0, 1).equalsIgnoreCase(Constant.TARGET_FILE_NAME_SYMBOL)) {
			result = result.substring(1);
		}

		return result;
	}

	public static String makeValidShowDirectory(String input) {
		return Constant.IOS_SHOWS_DIRECTORY + input + Constant.SYSTEM_FILE_SEPARATOR;
	}

	public static String makeValidEpisodeDirectory(String input) {
		return input + Constant.IOS_EPISODES_FOLDER;
	}

	public static String makeValidImageFileName(String input) {
		return input + Constant.IMAGE_FILE_EXTENSION;
	}
}
