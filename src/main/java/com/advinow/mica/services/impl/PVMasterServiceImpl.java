package com.advinow.mica.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.resilience4j.retry.annotation.Retry;

import com.advinow.mica.domain.PVMasterQueryResult;
import com.advinow.mica.exception.DataInvalidException;
import com.advinow.mica.repositories.PVMasterRepository;
import com.advinow.mica.services.PVMasterService;

@Service
@Retry(name = "neo4j")
public class PVMasterServiceImpl implements PVMasterService {

	@Autowired
	PVMasterRepository pVMasterRepository;
		
	@Override
	public List<PVMasterQueryResult> searchByPVDrugName(String drugName) {
		String fuzzySearch = drugName.toUpperCase().concat("~").concat("0.7") ;
		List<PVMasterQueryResult> v = pVMasterRepository.searchPvDruName(fuzzySearch);
		return v;
	
	}
	
	
	@Override
	public List<PVMasterQueryResult> searchPvDruNameWithParameters(String drugName,String status) {
		String fuzzySearch = drugName.toUpperCase().concat("~").concat("0.7") ;
		List<PVMasterQueryResult> v = pVMasterRepository.searchPvDruNameWithParameters(fuzzySearch,status);
		return v;
	
	}


	@Override
	public List<PVMasterQueryResult> getCombinationDrugWithParameters(
			List<Long> pvid, String status) {
	
		return pVMasterRepository.getCombinationDrugWithParameters(pvid,status);
	}


	@Override
	public List<PVMasterQueryResult> getCombinationDrugs(List<Long> pvid) {
		
		return pVMasterRepository.getCombinationDrugs(pvid);
	}
	

}
