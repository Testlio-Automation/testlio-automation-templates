package com.testlio.lib.env;

import java.util.HashMap;
import java.util.Map;

public enum BrowserTypes {

	/**
	 * chrome
	 * firefox
	 * MicrosoftEdge
	 * internet explorer
	 */

	FIREFOX("firefox", ExecutionTypes.WEB),
	CHROME("chrome", ExecutionTypes.WEB),
	EDGE("MicrosoftEdge", ExecutionTypes.WEB),
	IE("internet explorer", ExecutionTypes.WEB);

	private String name;
	private ExecutionTypes type;

	private static Map<String, BrowserTypes> browserMap = new HashMap<>();

	static {
		for (BrowserTypes bt : BrowserTypes.values()) {
			browserMap.put(bt.getName(), bt);
		}
	}

	public static Map<String, BrowserTypes> getBrowsers() {
		return browserMap;
	}

	BrowserTypes(String name, ExecutionTypes type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public ExecutionTypes getType() {
		return this.type;
	}

}