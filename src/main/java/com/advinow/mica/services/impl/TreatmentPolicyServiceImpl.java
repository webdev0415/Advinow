/**
 * 
 */
package com.advinow.mica.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.resilience4j.retry.annotation.Retry;

import com.advinow.mica.domain.Policy;
import com.advinow.mica.domain.PolicyRef;
import com.advinow.mica.domain.SymptomTemplate;
import com.advinow.mica.exception.DataInvalidException;
import com.advinow.mica.exception.DataNotFoundException;
import com.advinow.mica.model.PolicyModel;
import com.advinow.mica.repositories.PolicyRepositoty;
import com.advinow.mica.repositories.SymptomTemplateRepository;
import com.advinow.mica.repositories.PolicyRefRepositoty;
import com.advinow.mica.services.TreatmentPolicyService;

/**
 * @author Govinda Reddy
 *
 */

@Service
@Retry(name = "neo4j")
public class TreatmentPolicyServiceImpl implements TreatmentPolicyService{

	@Autowired
	SymptomTemplateRepository symptomTemplateRepository;

	@Autowired
	PolicyRefRepositoty treatmentPolicyRefRepositoty;

	@Autowired
	PolicyRepositoty  treatmentPolicyRepositoty;

	@Override
	public List<PolicyRef> getPolicies() {
		// TODO Auto-generated method stub
		return treatmentPolicyRefRepositoty.findPolicies();
	}

	@Override
	public PolicyRef createPolicy(PolicyRef tretmentPolicy) {		
		PolicyRef  dbPolicy = null;
		if(tretmentPolicy.getPolicyID() != null) {
			dbPolicy = treatmentPolicyRefRepositoty.findByPolicyID(tretmentPolicy.getPolicyID());
		}
		if(tretmentPolicy.getName().length() >  30){
			throw new DataInvalidException("Group name should be limited to 30 characters.");
		}
		if(dbPolicy != null) {
			dbPolicy=	treatmentPolicyRefRepositoty.updatePolicyName(tretmentPolicy.getPolicyID(),tretmentPolicy.getName());
		} else {
			dbPolicy = treatmentPolicyRefRepositoty.findByName(tretmentPolicy.getName().toUpperCase());
		}	

		if(dbPolicy==null) {
			dbPolicy = new PolicyRef();
			Integer   policyID = treatmentPolicyRefRepositoty.getMaxPolicyID();
			if(policyID==null) {
				policyID =0;
			}
			dbPolicy.setPolicyID(policyID + 1);
			dbPolicy.setName(tretmentPolicy.getName());	
			dbPolicy = treatmentPolicyRefRepositoty.save(dbPolicy);


		}
		return   dbPolicy;

	}

	@Override
	public String delete(Integer policyID) {
		PolicyRef dbPolicy = treatmentPolicyRefRepositoty.findByPolicyID(policyID);
		if(dbPolicy != null ){
			List<String> restSymptoms = symptomTemplateRepository.deletePolicies(policyID);
			treatmentPolicyRefRepositoty.delete(policyID);
			return "Policy deleted Successfully";
		} else {
			throw new DataNotFoundException("No matching Policy found.");

		}

	}

	@Override
	public String updateSymptomPolicies(
			PolicyModel treatmentPolicyModel) {
		Integer policyID =treatmentPolicyModel.getPolicyID();
		List<String> symptoms =treatmentPolicyModel.getSymptoms();

		if(policyID == null) {
			throw new DataNotFoundException("PolicyID should not be null.");
		}

		if(symptoms != null && symptoms.isEmpty() ) {
			throw new DataNotFoundException("No symptoms availble to update.");
		}

		PolicyRef dbPolicyRef = treatmentPolicyRefRepositoty.findByPolicyID(policyID);

		if(dbPolicyRef != null ){
			List<String> dbSymptoms = symptomTemplateRepository.deletePolicies(policyID);

			List<String> finalSymptoms =                    saveorUpdateSymptomPolicies(symptoms,dbPolicyRef);

			if(finalSymptoms != null &&  dbSymptoms != null) {
				/* List<String> updateTimeSymptoms = Stream.concat(dbSymptoms.stream(),finalSymptoms.stream())
                         .map(x->x)
                         .distinct()
                         .collect(Collectors.toList());*/



				return "Symptoms policies added successfully.";
			}




		} else {
			throw new DataNotFoundException("No matching policy found");
		}

		return null;
	}

	@Override
	public PolicyModel getSymptomsPolicies(Integer policyID) {
		PolicyRef dbPolicyRef = treatmentPolicyRefRepositoty.findByPolicyID(policyID);
		PolicyModel  treatmentPolicyModel = null;
		 if(dbPolicyRef != null ){
				  treatmentPolicyModel = new PolicyModel();
				 treatmentPolicyModel.setPolicyID(dbPolicyRef.getPolicyID());
			 
			List<String> sysmptoms = symptomTemplateRepository.getSymptomsForGivenPolicy(dbPolicyRef.getPolicyID());
			if(sysmptoms !=  null && ! sysmptoms.isEmpty())   {
			treatmentPolicyModel.setSymptoms(sysmptoms);
			}
		 }
			return treatmentPolicyModel;
	}



	private  List<String> saveorUpdateSymptomPolicies(List<String> symptoms, PolicyRef policyRef) {
		List<String> finalSymptoms = new ArrayList<String>();
		List<SymptomTemplate> symptomTemplates = symptomTemplateRepository.getSymptomTemaplesByGroups(symptoms);
		if(symptomTemplates != null) {		
			for (int i = 0; i < symptomTemplates.size(); i++) {
				SymptomTemplate template =	symptomTemplates.get(i);
				if(template != null) {
					Policy policy = new Policy();
					policy.setPolicyID(policyRef.getPolicyID());
					Set<Policy> policies = new HashSet<Policy>();
					policies.add(policy);
					template.getPolicies().addAll(policies);
					finalSymptoms.add(template.getCode());
				}
			}
		}
		symptomTemplateRepository.saveAll(symptomTemplates);

		return finalSymptoms;
	}



}
