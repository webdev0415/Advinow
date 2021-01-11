package com.advinow.mica.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 
 * @author Govinda Reddy
 *
 */
public enum Units {

	PILLS("Pills"),G("G"), MG("MG"),ML("ML"),MCG("MCG"),MGML("MG/ML");
	
	private String text;

	private Units(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	@JsonCreator
	public static Units fromText(String text) {
		for (Units r : Units.values()) {
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
