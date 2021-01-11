package com.advinow.mica.services;

import java.util.List;
import java.util.Set;

import com.advinow.mica.domain.queryresult.IllnessDataQueryResultEnitity;
import com.advinow.mica.model.IllnessModel;
import com.advinow.mica.model.IllnessSourceInfo;
import com.advinow.mica.model.PaginationModel;
import com.advinow.mica.model.IllnessStatusModel;
import com.advinow.mica.model.IllnessUserData;
import com.advinow.mica.model.SymptomSource;

public interface OtherSourceIllnessService {

	/**
	 * 	
	 * @param page
	 * @param size
	 * @param source
	 * @return
	 */
	public  PaginationModel getIllnessWithPaging(Integer  page,  Integer  size,String source,String status,String icd10code,String name);
	/**
	 * 
	 * @param userID
	 * @param source
	 * @return
	 */
	public IllnessUserData findByIllnessByUserAndSource(Integer userID,String source);

	/**
	 * 
	 * @param illnessStatusModel
	 * @return
	 */
	public String updateStatusReAssignUser(IllnessStatusModel illnessStatusModel,String source);
	
	
	/**
	 * 
	 * @param illnessModel
	 * @return
	 */
	public IllnessStatusModel saveIllnessData(IllnessModel illnessModel);
	
	/**
	 * 
	 * @param icd10Code
	 * @param state
	 * @return
	 */
	public IllnessUserData getIllnessByIcd10Code(String icd10Code, String state,String source);
	
	public Set <SymptomSource> getSymptomsWithSource(String icd10Code,	String state, String upperCase);
	
	public List<IllnessSourceInfo>  getSymptomsSourcesFromIllnesses(List<String> icd10Code,	String state, String upperCase);
	
	public List<IllnessDataQueryResultEnitity> getApprovedIllnesses(String source);
	
}
