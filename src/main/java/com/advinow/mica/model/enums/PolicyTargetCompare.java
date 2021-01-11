package com.advinow.mica.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 
 * @author Govinda Reddy
 *
 */
public enum PolicyTargetCompare {

	NONE("NONE"), EQUALS("EQUALS"),LESSTHAN("LESSTHAN"),LTEEQUAL("LTEEQUAL"),GTEEQUAL("GTEEQUAL"),GREATERTHAN("GREATERTHAN");
	
	private String text;

	private PolicyTargetCompare(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	@JsonCreator
	public static PolicyTargetCompare fromText(String text) {
		for (PolicyTargetCompare r : PolicyTargetCompare.values()) {
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
