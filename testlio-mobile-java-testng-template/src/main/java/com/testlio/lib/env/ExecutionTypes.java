package com.testlio.lib.env;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public enum ExecutionTypes {

	WEB("web"), MOBILE_NATIVE("mobile-native"), MOBILE_WEB("mobile-web");

	private String name;

	private static Set<String> executionTypesSet = new HashSet<>();

	static {
		for (ExecutionTypes value : EnumSet.allOf(ExecutionTypes.class)) {
			executionTypesSet.add(value.getName());
		}
	}

	public static Set<String> getMobileTypes() {
		return executionTypesSet;
	}

	ExecutionTypes(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}