package com.advinow.mica.services.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.resilience4j.retry.annotation.Retry;

import com.advinow.mica.domain.LinkEnd;
import com.advinow.mica.domain.LinkListItem;
import com.advinow.mica.domain.LinkStart;
import com.advinow.mica.domain.SymptomRule;
import com.advinow.mica.domain.SymptomTemplate;
import com.advinow.mica.domain.queryresult.SymptomRuleModel;
import com.advinow.mica.exception.DataNotFoundException;
import com.advinow.mica.repositories.LinkEndRepository;
import com.advinow.mica.repositories.LinkStartRepository;
import com.advinow.mica.repositories.SymptomRuleRepository;
import com.advinow.mica.repositories.SymptomTemplateRepository;
import com.advinow.mica.services.SymptomRuleService;

@Service
@Retry(name = "neo4j")
public class SymptomRuleServiceImpl implements SymptomRuleService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired	
	SymptomRuleRepository symptomRuleRepository;
	
	@Autowired	
	SymptomTemplateRepository symptomTemplateRepository;
	
	@Autowired	
	LinkEndRepository linkEndRepository;
	
	@Autowired	
	LinkStartRepository linkStartRepository;

	@Override
	public List<SymptomRuleModel> getAllRules() {
		List<SymptomRuleModel> rules = symptomRuleRepository.getAllRules();
		List<SymptomRuleModel> sortedList = new ArrayList<SymptomRuleModel>();

		if(rules != null ) {
			sortedList = rules.stream()
					.sorted(Comparator.comparingLong(SymptomRuleModel::getLastUpdated)
							.reversed())
							.collect(Collectors.toList());
		}

		return sortedList;

	}

	@Override
	public SymptomRule createSymptomRules(SymptomRuleModel symptomRuleModel) {		
		SymptomRule dbRule = null;
		
		
		if(symptomRuleModel != null  &&  symptomRuleModel.getRuleID() != null ) {
			   dbRule = symptomRuleRepository.findByRuleID(symptomRuleModel.getRuleID());
			   logger.info("Existing Rule::" + dbRule);
		}
		
		if(dbRule != null) {
			   deleteSymptomLinks(dbRule.getRuleID());
		} else{
			   dbRule = new SymptomRule();
				 Integer   roleID = symptomRuleRepository.getMaxRoleID();
				 if(roleID==null) {
					 roleID =0;
				 }
				    dbRule.setRuleID(roleID + 1);
				    
		}
		
		
		dbRule.setStartID(updateStartLink(symptomRuleModel));
		
		dbRule.setEndID(updateEndLink(symptomRuleModel));
		
     processSymptomRule(dbRule,symptomRuleModel);	
		
     SymptomRule dbFinalRule=  symptomRuleRepository.save(dbRule);
		
	 return dbFinalRule;
	}



	private Long updateEndLink(SymptomRuleModel symptomRuleModel) {
		
		SymptomTemplate symptomTeplate = symptomTemplateRepository.findByCode(symptomRuleModel.getT_symptom_id(), 1);
		
		if(symptomTeplate==null) {
			
			throw new DataNotFoundException(symptomRuleModel.getT_symptom_id() + " not found in the system.");
		}

		
		List<String> itemCodes = symptomRuleModel.getS_list_items();
		Set<LinkListItem>  listItems = new HashSet<LinkListItem>();
		if(itemCodes != null) {
			for (String itemCode : itemCodes) {
				LinkListItem item = new LinkListItem();
				item.setCode(itemCode);
				listItems.add(item);
				
			}
			
			LinkEnd linkEnd = new LinkEnd ();
			linkEnd.setListItems(listItems);
			LinkEnd dbLlinkEnd = linkEndRepository.save(linkEnd, 2);
			symptomTeplate.getEndLinks().add(dbLlinkEnd);
			symptomTemplateRepository.save(symptomTeplate);
			
			
			return dbLlinkEnd.getId();
		}
		
		
			return null;
		
		
		
	}

	private Long updateStartLink(SymptomRuleModel symptomRuleModel) {
		
	SymptomTemplate symptomTeplate = symptomTemplateRepository.findByCode(symptomRuleModel.getS_symptom_id(), 1);
	
	if(symptomTeplate==null) {
		
		throw new DataNotFoundException(symptomRuleModel.getS_symptom_id() + " not found in the system.");
	}
	
	
	List<String> itemCodes = symptomRuleModel.getS_list_items();
	Set<LinkListItem>  listItems = new HashSet<LinkListItem>();
	if(itemCodes != null) {
		for (String itemCode : itemCodes) {
			LinkListItem item = new LinkListItem();
			item.setCode(itemCode);
			listItems.add(item);
			
		}
		
		LinkStart linkStart = new LinkStart ();
		linkStart.setListItems(listItems);
		linkStart.setSelection(symptomRuleModel.getS_rule());
		linkStart.setSimpleTrigger(symptomRuleModel.getS_trigger().getText());
		LinkStart dbLlinkStart = linkStartRepository.save(linkStart, 2);
		symptomTeplate.getStartLinks().add(dbLlinkStart);
		symptomTemplateRepository.save(symptomTeplate);
		
		
		return dbLlinkStart.getId();
	}
	
	
		return null;
	}

	private void processSymptomRule(SymptomRule dbRule,
			SymptomRuleModel symptomRuleModel) {
		dbRule.setDirection(symptomRuleModel.getDirection());
	//	dbRule.setEndID(endID);
		dbRule.setLastUpdated(new Date().getTime());
		dbRule.setName(symptomRuleModel.getTag());
		dbRule.setRelation(symptomRuleModel.getRelation_s_to_t());
	//	dbRule.setStartID(startID);
		
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Integer ruleID) {
		symptomRuleRepository.deleteByRuleID(ruleID);
	}
	
	
	private void deleteSymptomLinks(Integer ruleID) {
		symptomRuleRepository.deleteSymptomLinks(ruleID);
	}
	

	@Override
	public SymptomRuleModel getRoleByID(Integer ruleID) {
	return	symptomRuleRepository.getSymptomRuleByRuleID(ruleID);
	}

	
	
}
