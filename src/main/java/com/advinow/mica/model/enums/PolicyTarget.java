package com.advinow.mica.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 
 * @author Govinda Reddy
 *
 */
public enum PolicyTarget {

	NONE("NONE"),ILLNESS("ILLNESS"), SYMPTOM("SYMPTOM"),PROPERTY("PROPERTY");

	private String text;

	private PolicyTarget(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	@JsonCreator
	public static PolicyTarget fromText(String text) {
		for (PolicyTarget r : PolicyTarget.values()) {
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
