package com.advinow.mica.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 
 * @author Govinda Reddy
 *
 */
public enum TimeType {

	STARTED("STARTED"), HISTORY("HISTORY");

	private String text;

	private TimeType(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	@JsonCreator
	public static TimeType fromText(String text) {
	
		for (TimeType r : TimeType.values()) {
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
