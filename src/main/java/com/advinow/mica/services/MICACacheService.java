package com.advinow.mica.services;

import java.util.Map;

import com.advinow.mica.model.SymptomGroups;
import com.advinow.mica.model.cache.CacheGroupsTime;


public interface MICACacheService {
/*
	Map<String,Long> getGroupsChanged(Map<String,Long> dbGroups,String lCode);
*/
	void createSymptomCache(SymptomGroups symptomGroups,String groupName,String lCode);
	
	void createDrAppSymptomCache(SymptomGroups symptomGroups,String groupName,String drName) ;

	void createGroupTimeMap(CacheGroupsTime dbGroupsTime,String lCode);
	
	void createDrAppGroupTimeMap(CacheGroupsTime dbGroupsTime,String drName);

	SymptomGroups  loadSymptomsFromCache(String groupName,String lCode);

	CacheGroupsTime  readGroupTimeMap(String lCode);

	Map<String,Long> getChachedGroups(String lCode);	
	
	Map<String,Long> getDrAppChachedGroups(String drName);

	SymptomGroups  loadDrAppSymptomsFromCache(String groupName,String drName);
}
