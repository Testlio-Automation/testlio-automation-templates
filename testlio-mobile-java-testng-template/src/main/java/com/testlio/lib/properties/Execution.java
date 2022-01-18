package com.testlio.lib.properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Execution {

	private static ThreadLocal<AutomationProperties> properties = new ThreadLocal<>();

	private Execution() {}

	public static AutomationProperties properties() {
		if (properties.get() == null) {
			setProperties();
		}

		return properties.get();
	}

    public static void resetProperties() {
        log.info("Resetting Automation Properties");
        properties.remove();
    }

	private static void setProperties() {
		log.info("Creating Automation Properties");
		properties.set(new AutomationProperties());
	}

}