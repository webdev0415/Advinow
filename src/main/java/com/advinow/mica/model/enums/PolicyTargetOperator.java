package com.advinow.mica.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 
 * @author Govinda Reddy
 *
 */
public enum PolicyTargetOperator {

	AND("AND"), OR("OR"),NONE("NONE");

	private String text;

	private PolicyTargetOperator(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	@JsonCreator
	public static PolicyTargetOperator fromText(String text) {
		for (PolicyTargetOperator r : PolicyTargetOperator.values()) {
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
