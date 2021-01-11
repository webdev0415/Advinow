package com.advinow.mica.model.dataframe;

import java.util.List;

import lombok.Data;


/**
 * 
 * @author Govinda Reddy
 *
 */
public @Data class SymptomSettings {
	
	 Float value ;
	 
	 List<String> related_icd10= null;
	 
	 private transient  Boolean bias;
	 
	 private transient  Boolean mdc;
	 
	 private transient Boolean must;
	 
	 private transient Boolean ruleOut;


}
