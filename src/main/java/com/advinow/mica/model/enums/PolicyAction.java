package com.advinow.mica.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 
 * @author Govinda Reddy
 *
 */
public enum PolicyAction {

	NONE("NONE"),INCLUDE("INCLUDE"), EXCLUDE("EXCLUDE");

	private String text;

	private PolicyAction(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	@JsonCreator
	public static PolicyAction fromText(String text) {
		for (PolicyAction r : PolicyAction.values()) {
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
