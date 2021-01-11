package com.advinow.mica.services;

import java.util.List;

import com.advinow.mica.domain.SymptomSourceInfo;
/**
 * 
 * @author Govinda Reddy
 *
 */
public interface SymptomSourceInfoService {
	
    /**
     * Returns all sources
     * 
     * @return 	List<SymptomSourceInfo>
     */
	List<SymptomSourceInfo> getSourceSymptoms();

	/**
	 * Create source.
	 * 
	 * @param symptomSource
	 * @return SymptomSourceInfo
	 */
	SymptomSourceInfo createSourceSymptom(SymptomSourceInfo symptomSource);

	/**
	 * Search source.
	 * 
	 * @param source
	 * @return List<SymptomSourceInfo> 
	 */
	List<SymptomSourceInfo> searchSources(String source);
	
	void delete(Integer groupID);

}
