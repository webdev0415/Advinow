package com.advinow.mica.domain.queryresult;



import java.util.ArrayList;
import java.util.List;

import org.springframework.data.neo4j.annotation.QueryResult;

import lombok.Data;









import com.advinow.mica.model.enums.SimpleTrigger;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@QueryResult
public @Data class SymptomRuleModel  {

	
	@JsonInclude(Include.NON_NULL)
	private Integer ruleID;
	
	@JsonInclude(Include.NON_NULL)
	private String tag;
	
		
	@JsonInclude(Include.NON_NULL)
	private String s_symptom_id;
	
	@JsonInclude(Include.NON_NULL)
	private SimpleTrigger  s_trigger;
	
	
	@JsonInclude(Include.NON_NULL)
	private List<String> s_list_items = new ArrayList<String>();
		
	// all/list/any
	@JsonInclude(Include.NON_NULL)
	private String s_rule;
		
	
	@JsonInclude(Include.NON_NULL)
	private String t_symptom_id;
	
	@JsonInclude(Include.NON_NULL)
	private List<String> t_list_items = new ArrayList<String>();
	
		
	@JsonInclude(Include.NON_NULL)
	private String relation_s_to_t;
	
	@JsonInclude(Include.NON_NULL)
	private String direction;
	
	
	@JsonInclude(Include.NON_NULL)
	private Long lastUpdated; 
	
		
}
