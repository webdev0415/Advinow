package com.advinow.mica.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.resilience4j.retry.annotation.Retry;

import com.advinow.mica.domain.LogicalSymptomGroupsRef;
import com.advinow.mica.exception.DataInvalidException;
import com.advinow.mica.exception.DataNotFoundException;
import com.advinow.mica.model.LogicalSymptomGroupsModel;
import com.advinow.mica.repositories.LogicalSymptomGroupsRefRepositoty;
import com.advinow.mica.repositories.SymptomGroupRepository;
import com.advinow.mica.repositories.SymptomTemplateRepository;
import com.advinow.mica.services.SymptomGroupService;
import com.advinow.mica.util.MICAConstants;

/**
 * 
 * @author Govinda Reddy
 *
 */
@Service
@Retry(name = "neo4j")
public class SymptomGroupServiceImpl implements SymptomGroupService {

	@Autowired
	LogicalSymptomGroupsRefRepositoty logicalSymptomGroupsRefRepositoty;
	
	@Autowired
	SymptomGroupRepository   symptomGroupRepository;

	@Autowired
	SymptomTemplateRepository symptomTemplateRepository;
	
	@Override
	public List<LogicalSymptomGroupsModel> getSymptomGroups(String LanguageCode) {
		// TODO Auto-generated method stub
		List<LogicalSymptomGroupsRef> dbParentGroups = null;
		List<LogicalSymptomGroupsModel> finalGroups = new ArrayList<LogicalSymptomGroupsModel>();
		List<LogicalSymptomGroupsRef> dbGroups = logicalSymptomGroupsRefRepositoty.findROSGroups();
		if(dbGroups != null ) {
			dbParentGroups = dbGroups.stream().filter(s->s.getParentID()==null).collect(Collectors.toList());
		}
		for (int i = 0; i < dbParentGroups.size(); i++) {
			LogicalSymptomGroupsRef dbParentGroup = dbParentGroups.get(i);
			LogicalSymptomGroupsModel parentGroupModel = null;
			if(dbParentGroup != null ) {
				parentGroupModel = new LogicalSymptomGroupsModel();
				if(MICAConstants.SPANISH.equals(LanguageCode)) {    			 
					parentGroupModel.setEs_name(dbParentGroup.getEs_name());
	    		 }
				parentGroupModel.setName(dbParentGroup.getName());
				parentGroupModel.setGroupID(dbParentGroup.getGroupID());
				parentGroupModel.setType(dbParentGroup.getType());
				parentGroupModel.setParentID(dbParentGroup.getParentID());			
				parentGroupModel.setSubGroups(prepareSubGroups(dbGroups,dbParentGroup.getGroupID(),LanguageCode));
			}
			
			finalGroups.add(parentGroupModel);
		}
	 	return finalGroups;
     	
     	
	}

	private List<LogicalSymptomGroupsModel> prepareSubGroups(
			List<LogicalSymptomGroupsRef> dbGroups, Integer classificationID,
			String LanguageCode) {
		List<LogicalSymptomGroupsModel> subGroups = new ArrayList<LogicalSymptomGroupsModel>();
		List<LogicalSymptomGroupsRef> dbSubGroups =	dbGroups.stream().filter(s->s.getParentID() != null && s.getParentID().equals(classificationID)).collect(Collectors.toList());
		if(dbSubGroups != null && ! dbSubGroups.isEmpty()){			
			for (int i = 0; i < dbSubGroups.size(); i++) {
				LogicalSymptomGroupsRef dbGroup =	dbSubGroups.get(i);
				if(dbGroup != null ) {
					LogicalSymptomGroupsModel  groupModel = new LogicalSymptomGroupsModel();
					  if(MICAConstants.SPANISH.equals(LanguageCode)) {    			 
						  groupModel.setEs_name(dbGroup.getEs_name());
		    			 
		    		 }
					  groupModel.setName(dbGroup.getName());
					  groupModel.setGroupID(dbGroup.getGroupID());
					  groupModel.setParentID(dbGroup.getParentID());
		    		  if(dbGroup.getParentID() !=null) {
		    			  groupModel.setSubGroups(prepareSubGroups(dbGroups,dbGroup.getGroupID(),LanguageCode));
		    		  }
		    		  subGroups.add(groupModel);
				}
				
			}
			
		}
		return subGroups;
	}

	@Override
	//@Transactional
	public LogicalSymptomGroupsModel createSymptomGroup(
			LogicalSymptomGroupsModel symptomGroupModel) {		
		LogicalSymptomGroupsRef dbGroup = null;
		LogicalSymptomGroupsModel	finalDBGroup = null;
		
		String type ="LOGICAL";
		
		if(symptomGroupModel.getGroupID() != null) {
			dbGroup = logicalSymptomGroupsRefRepositoty.findByGroupID(symptomGroupModel.getGroupID());
		}
		
		String logicalGroupName = null;
		
	     String groupName = symptomGroupModel.getName();
		
		if(symptomGroupModel != null  && groupName != null) {
		
			logicalGroupName = groupName;
			
			if(groupName.toUpperCase().contains("ROS")) {
			logicalGroupName =groupName.trim().replaceAll("ROS-", "").replaceAll("ROS -", "").replaceAll("ROS - ", "").trim();
			type ="ROS";
			}  
			
			if(groupName.length() >  20){
				throw new DataInvalidException("Group name should be limited to 20 characters.");
			}
			if(dbGroup != null) {
				 dbGroup.setName(symptomGroupModel.getName());
			} else {
	  	    	dbGroup = logicalSymptomGroupsRefRepositoty.findByName(groupName.toUpperCase());
			}	
		   
		   if(dbGroup==null) {
			   dbGroup = new LogicalSymptomGroupsRef();
			 Integer   groupID = logicalSymptomGroupsRefRepositoty.getMaxGroupID();
			 if(groupID==null) {
				 groupID =0;
			 }
		    dbGroup.setGroupID(groupID + 1);
		    dbGroup.setName(symptomGroupModel.getName());	
		    dbGroup.setLogicalGroupName(logicalGroupName);
		    dbGroup.setType(type);
		    dbGroup = logicalSymptomGroupsRefRepositoty.save(dbGroup);
		   }
		}
			
		
		   if(dbGroup != null) {
			   finalDBGroup = new LogicalSymptomGroupsModel();
				finalDBGroup.setGroupID(dbGroup.getGroupID());
				finalDBGroup.setName(dbGroup.getName());
				finalDBGroup.setType(dbGroup.getType());	
			
				
			}
		   
		return   finalDBGroup;
	
	}

	@Override
	public String delete(Integer groupID) {
		 LogicalSymptomGroupsRef dbGroup = logicalSymptomGroupsRefRepositoty.findByGroupID(groupID);
		 if(dbGroup != null ){
			 List<String> restSymptoms = symptomTemplateRepository.deleteLogicalSymptomGroups(dbGroup.getGroupID());
            if(restSymptoms != null && ! restSymptoms.isEmpty()) {
					 // update timestamp for group changed symptoms
				 symptomGroupRepository.updateSymptomGroupTime(restSymptoms);
             }
			 
			 logicalSymptomGroupsRefRepositoty.delete(groupID);
			 return "Group deleted Successfully";
		 } else {
			  throw new DataNotFoundException("No matching group found.");
			 
		 }
		
	}



}
