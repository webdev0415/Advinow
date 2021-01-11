/**
 * 
 */
package com.advinow.mica.services;

import java.util.List;

import com.advinow.mica.domain.PVMasterQueryResult;

/**
 * @author Govinda Reddy
 *
 */

public interface PVMasterService {

	List<PVMasterQueryResult> searchByPVDrugName(String drugName);
	
	List<PVMasterQueryResult> searchPvDruNameWithParameters(String drugName,String status);

	List<PVMasterQueryResult> getCombinationDrugWithParameters(List<Long> pvid,String status);

	List<PVMasterQueryResult> getCombinationDrugs(List<Long> pvid);
	

}
