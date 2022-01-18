package com.testlio.lib.utility.recognition;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public interface CLibrary extends Library {

	CLibrary INSTANCE = Native.load((Platform.isWindows() ? "msvcrt" : "c"), CLibrary.class);

	int LC_CTYPE = 0;
	int LC_NUMERIC = 1;
	int LC_ALL = 6;

    /**
     * char *setlocale(int category, const char *locale);
     * @param category
     * @param locale
     */
	void setlocale(int category, String locale);
}
