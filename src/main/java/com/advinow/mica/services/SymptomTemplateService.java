package com.advinow.mica.services;

import java.util.List;

import com.advinow.mica.model.SymptomType;

/**
 * 
 * @author Govinda Reddy
 *
 */
public interface SymptomTemplateService {
	
	/**
	 * 
	 * @param groupId
	 * @return
	 */

	List<SymptomType> getSymptomTemplates(String groupId);
	
	List<SymptomType> getSymptomsTemplatesByGroup(String groupId);

	List<SymptomType> getPatientBioSymptoms();

	List<SymptomType> getFamilySocialHistorySmptoms();

}
