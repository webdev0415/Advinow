/**
 * 
 */
package com.advinow.mica.services;

import java.util.List;

import com.advinow.mica.domain.PolicyRef;
import com.advinow.mica.model.PolicyModel;

/**
 * @author Govinda Reddy
 *
 */
public interface TreatmentPolicyService {

	List<PolicyRef> getPolicies();

	PolicyRef createPolicy(PolicyRef tretmentPolicy);

	String delete(Integer policyID);

	String updateSymptomPolicies(PolicyModel treatmentPolicyModel);

	PolicyModel getSymptomsPolicies(Integer policyID);

}
