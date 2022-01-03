package com.testlio.lib.env;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public enum ProvidersTypes {

	LOCAL("local"), TESTLIO("testlio");

	private String name;

	private static final Set<String> providers = new HashSet<>();

	static {
		for (ProvidersTypes value : EnumSet.allOf(ProvidersTypes.class)) {
			providers.add(value.getName());
		}
	}

	public static Set<String> getProviders() {
		return providers;
	}

	ProvidersTypes(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

}