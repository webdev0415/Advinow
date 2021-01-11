package com.advinow.mica.model.dataframe;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.advinow.mica.domain.queryresult.DataframeQueryResultEntity;
import com.advinow.mica.model.JSonModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
@EqualsAndHashCode(callSuper=false)
public  class DataFrameModel extends JSonModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonInclude(Include.NON_EMPTY)
	private Map<String,Integer> icd10Codes = new Hashtable<String,Integer>();
	@JsonInclude(Include.NON_EMPTY)
	private Set<String> symptoms = new HashSet<String>();
	@JsonInclude(Include.NON_EMPTY)
	private Map<String,Float> priors = new Hashtable<String,Float>();
	@JsonInclude(Include.NON_EMPTY)
	private List<DataframeQueryResultEntity> symptomDcls =  new ArrayList<DataframeQueryResultEntity>();		
	@JsonInclude(Include.NON_EMPTY)
	private Map<String,Integer> clusters = new Hashtable<String,Integer>();
	

}
