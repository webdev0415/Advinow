package com.advinow.mica.model.dataframe;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

import org.springframework.data.neo4j.annotation.QueryResult;

import com.google.gson.annotations.SerializedName;


/**
 * 
 * @author Govinda Reddy
 *
 */
@SuppressWarnings("serial")
@QueryResult
public @Data class DFSymptomKeys implements Serializable {

	
	private transient Integer key;
	
	private transient String dfKey;
	
	@SerializedName("id")
	private String symptomID;
	
	@SerializedName("name")
    private	String symptomName;
			
	@SerializedName("group_name")
	private String groupName = null;
	
	private transient String descriptor =null;

	private transient String timeFrame =null;
	
	//private transient List<String> range;
	
//	private transient  Long start;
	
	//private transient  Long stop;
	
	private transient Float likelihood = 0.001f;

	private Boolean basic=false;
	
	private Boolean  chiefComplaint=false;
	
	@SerializedName("tier_name")
	private String tierName = null;
	
	//private  Boolean timeUsed= false;
    private transient String timeBucket =null;

	private transient List<String> selectedBodyParts=null;
    
    @SerializedName("locations")
  	private List<String> locations =null;
    
	private List<String> descriptors =null;

    @SerializedName("time_buckets")
	private List<String> timeBuckets =null;
	
	private List<String> de_system_groups= null;
	
	private List<String> ros_system_groups= null;

	
	
	

}
