package com.advinow.mica.model.dataframe;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * 
 * @author Govinda Reddy
 *
 */
@SuppressWarnings("serial")
public @Data class DataFrameJsonModel implements Serializable {
	
	private String version = "2.1";
  /*private List<String> masterTimeBuckets ;*/
	
	private   List<DFSymptomKeys> symptoms;
	
	//private Map<String, Float> priors;
	
	private Map<String, Map<String, Float>> likelihoods;
	
	//private Map<String, Map<String,IllnessLikelihood>> likelihoods;
	
	private Map<String, Map<String,SymptomSettings>> negative_bias;
		
	Map<String, List<String>> positive_mdc = new Hashtable<String, List<String>>();
	
	Map<String, List<String>> negative_mdc = new Hashtable<String, List<String>>();
	
	Map<String, List<String>> must = new Hashtable<String, List<String>>();
	
	Map<String, List<String>> ruleOut = new Hashtable<String, List<String>>();
	
	//private Map<String, Float> dcls;
	
	private List<DFIllness> diseases;
	
	private CodingRules coding_rules = null;
	
	private Map<String, List<String>>  diseasesTracker; 
	

}
