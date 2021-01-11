package com.advinow.mica.services;

import java.util.List;

import com.advinow.mica.domain.SymptomRule;
import com.advinow.mica.domain.queryresult.SymptomRuleModel;
/**
 * 
 * @author Govinda Reddy
 *
 */
public interface SymptomRuleService {
	
    /**
     *    
     * @return
     */
	List<SymptomRuleModel> getAllRules();

	/**
	 * 
	 * @param ymptomRuleModel
	 * @return
	 */
	SymptomRule createSymptomRules(SymptomRuleModel ymptomRuleModel);

	
	/**
	 * 
	 * @param rulueID
	 */
	void delete(Integer rulueID);
	
	
    /** 
     * 
     * @return
     */
	SymptomRuleModel getRoleByID(Integer rulueID);

}
