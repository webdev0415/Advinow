package com.advinow.mica.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class IllnessStatusModel extends JSonModel {
	
	@JsonInclude(Include.NON_NULL)
	private Integer userID;
	
	@JsonInclude(Include.NON_NULL)
	private String fromState;
		
	@JsonInclude(Include.NON_NULL)
	private String toState;
	
	@JsonInclude(Include.NON_NULL)
	private String rejectionReason;
	
	@JsonInclude(Include.NON_NULL)
	private String status;
	
	@JsonInclude(Include.NON_NULL)
	private List<String> state;
	
	@JsonInclude(Include.NON_NULL)
	private Integer count;
	
	@JsonInclude(Include.NON_NULL)
	private Integer version;
	
	@JsonInclude(Include.NON_NULL)
	private String icd10Code;
	
	@JsonInclude(Include.NON_EMPTY)
	private List<Long>  ids= new ArrayList<Long>();
	
	@JsonInclude(Include.NON_EMPTY)
	private Set<String> icd10CodesStatus= new HashSet<String>();
	
	@JsonInclude(Include.NON_EMPTY)
	private List<String> icd10Codes= new ArrayList<String>();
	
	@JsonInclude(Include.NON_EMPTY)
	private Map<String, List<Integer>> icd10CodesVersion= new Hashtable<String, List<Integer>>();
	
	
	/**/

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}


	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	
	public String getFromState() {
		return fromState;
	}

	public void setFromState(String fromState) {
		this.fromState = fromState;
	}

	public String getToState() {
		return toState;
	}

	public void setToState(String toState) {
		this.toState = toState;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<String> getState() {
		return state;
	}

	public void setState(List<String> state) {
		this.state = state;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getIcd10Code() {
		return icd10Code;
	}

	public void setIcd10Code(String icd10Code) {
		this.icd10Code = icd10Code;
	}

	
	public List<String> getIcd10Codes() {
		return icd10Codes;
	}

	public void setIcd10Codes(List<String> icd10Codes) {
		this.icd10Codes = icd10Codes;
	}

	public Set<String> getIcd10CodesStatus() {
		return icd10CodesStatus;
	}

	public void setIcd10CodesStatus(Set<String> icd10CodesStatus) {
		this.icd10CodesStatus = icd10CodesStatus;
	}

	public Map<String, List<Integer>> getIcd10CodesVersion() {
		return icd10CodesVersion;
	}

	public void setIcd10CodesVersion(Map<String, List<Integer>> icd10CodesVersion) {
		this.icd10CodesVersion = icd10CodesVersion;
	}

	
	
	

}
