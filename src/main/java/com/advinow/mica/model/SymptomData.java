package com.advinow.mica.model;

import java.util.ArrayList;
import java.util.List;

public class SymptomData extends JSonModel {

	private List<String> symptoms= new ArrayList<String>();

	public List<String> getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(List<String> symptoms) {
		this.symptoms = symptoms;
	}
}
