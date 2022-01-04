package com.testlio.lib.env;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public enum MobileTypes {

	ANDROID("android"), IOS("ios");

	private String name;

	private static Set<String> mobileTypesSet = new HashSet<>();

	static {
		for (MobileTypes value : EnumSet.allOf(MobileTypes.class)) {
			mobileTypesSet.add(value.getName());
		}
	}

	public static Set<String> getMobileTypesSet() {
		return mobileTypesSet;
	}

	MobileTypes(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

}