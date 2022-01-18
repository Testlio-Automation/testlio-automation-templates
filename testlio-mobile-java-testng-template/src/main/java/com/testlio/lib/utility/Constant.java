package com.testlio.lib.utility;

import com.testlio.lib.env.MobileTypes;

import static java.nio.file.FileSystems.getDefault;

public class Constant {

    private Constant() {}

	public static final String SYSTEM_FILE_SEPARATOR;
    public static final String SHOWS_FOLDER;
	public static final String IOS_EPISODES_FOLDER;
	public static final String IOS_SHOWS_DIRECTORY;
	public static final String XPATH_TRANSLATE_ALPHABET = "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'";
	public static final int DEFAULT_IOS_PIXEL_RATIO = 3;
	public static final String REPLACEABLE_FILE_NAME_SYMBOLS = "[^a-zA-Z0-9]";
	public static final String TARGET_FILE_NAME_SYMBOL = "-";
	public static final String DOUBLE_TARGET_FILE_NAME_SYMBOL = TARGET_FILE_NAME_SYMBOL + TARGET_FILE_NAME_SYMBOL;
	public static final String IMAGE_FILE_EXTENSION = ".png";
	public static final String DEFAULT_NAME = "n/a";
	public static final String XPATH_ELEMENT_TYPE_STATIC_TEXT = "//XCUIElementTypeStaticText";
    public static final String ELEMENT_NODE_VALUE_ATTRIBUTE_PARAM_NAME = "value";
    public static final String ELEMENT_NODE_TEXT_ATTRIBUTE_PARAM_NAME = "text";

    static {
		SYSTEM_FILE_SEPARATOR = getDefault().getSeparator();
		SHOWS_FOLDER = SYSTEM_FILE_SEPARATOR + "shows" + SYSTEM_FILE_SEPARATOR;
		IOS_EPISODES_FOLDER = "episodes" + SYSTEM_FILE_SEPARATOR;
		IOS_SHOWS_DIRECTORY = SYSTEM_FILE_SEPARATOR + MobileTypes.IOS.getName() + SHOWS_FOLDER;
	}

}
