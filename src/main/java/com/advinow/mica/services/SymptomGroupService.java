package com.advinow.mica.services;

import java.util.List;

import com.advinow.mica.model.LogicalSymptomGroupsModel;
/**
 * 
 * @author Govinda Reddy
 *
 */
public interface SymptomGroupService {
	
    /**
     * Returns all groups
     * 
     * @return 	List<LogicalSymptomGroupsModel>
     */
	List<LogicalSymptomGroupsModel> getSymptomGroups(String LanguageCode);

	/**
	 * Create Group.
	 * 
	 * @param symptomGroup
	 * @return LogicalSymptomGroupsModel
	 */
	LogicalSymptomGroupsModel createSymptomGroup(LogicalSymptomGroupsModel symptomGroup);

	/**
	 * Search GroupName.
	 * 
	 * @param groupName
	 * @return List<LogicalSymptomGroupsModel> 
	 */
	/*List<LogicalSymptomGroupsModel> searchGroupsByName(String groupName);*/
	
	String delete(Integer groupID);

}
