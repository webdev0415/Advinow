package com.advinow.mica.model;

import java.io.Serializable;

public class RxNorm implements Serializable {
	
/**
	 * 
	 */
private static final long serialVersionUID = 1L;

String	rxcui;
String ingredientRxcui;
String ndc;

public String getRxcui() {
	return rxcui;
}
public void setRxcui(String rxcui) {
	this.rxcui = rxcui;
}
public String getIngredientRxcui() {
	return ingredientRxcui;
}
public void setIngredientRxcui(String ingredientRxcui) {
	this.ingredientRxcui = ingredientRxcui;
}
public String getNdc() {
	return ndc;
}
public void setNdc(String ndc) {
	this.ndc = ndc;
}

}
