package com.advinow.mica.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 
 * @author Govinda Reddy
 *
 */
public enum RuleOptions {

	NONE("NONE"),INCLUDE("INCLUDE"), EXCLUDE("EXCLUDE");

	private String text;

	private RuleOptions(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	@JsonCreator
	public static RuleOptions fromText(String text) {
		for (RuleOptions r : RuleOptions.values()) {
			if (r.getText().equals(text)) {
				return r;
			}
		}
		throw new IllegalArgumentException();
	}

	@Override
	public String toString() {
		return text;

	}

}
