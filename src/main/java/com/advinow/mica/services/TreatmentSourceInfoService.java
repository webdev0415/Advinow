package com.advinow.mica.services;

import java.util.List;

import com.advinow.mica.domain.TreatmentSourceInfo;
/**
 * 
 * @author Govinda Reddy
 *
 */
public interface TreatmentSourceInfoService {
	
    /**
     * Returns all sources
     * 
     * @return 	List<TreatmentSourceInfo>
     */
	List<TreatmentSourceInfo> getSourceSymptoms();

	/**
	 * Create source.
	 * 
	 * @param symptomSource
	 * @return TreatmentSourceInfo
	 */
	TreatmentSourceInfo createSourceSymptom(TreatmentSourceInfo symptomSource);

	/**
	 * Search source.
	 * 
	 * @param source
	 * @return List<TreatmentSourceInfo> 
	 */
	List<TreatmentSourceInfo> searchSources(String source);
	
	void delete(Integer groupID);

}
