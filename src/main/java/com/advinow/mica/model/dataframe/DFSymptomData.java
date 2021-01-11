package com.advinow.mica.model.dataframe;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

import org.springframework.data.neo4j.annotation.QueryResult;

/**
 * 
 * @author Govinda Reddy
 *
 */

@SuppressWarnings("serial")
@QueryResult
public @Data class DFSymptomData implements Serializable {


	public String symptomID;
	public	List<String>  bodyParts;
	public List<String> multiplier;
	public String timeFrame;
	public String icd10Code;
	public Float likelihood;
	public List<String> relatedIc10Codes= null;
	public Boolean bias;
	public String categoryID; 
	public Boolean mdc;
	public Boolean must;
	public Boolean ruleOut;
	public Long  dsId;
	
}
