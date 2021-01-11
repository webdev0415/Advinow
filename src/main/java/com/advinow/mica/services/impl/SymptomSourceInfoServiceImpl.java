package com.advinow.mica.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.resilience4j.retry.annotation.Retry;

import com.advinow.mica.domain.SymptomSourceInfo;
import com.advinow.mica.repositories.SymptomSourceInfoRepository;
import com.advinow.mica.services.SymptomSourceInfoService;

@Service
@Retry(name = "neo4j")
public class SymptomSourceInfoServiceImpl implements SymptomSourceInfoService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired	
	SymptomSourceInfoRepository symptomSourceInfoRepository;

	@Override
	public List<SymptomSourceInfo> getSourceSymptoms() {
	 Iterable<SymptomSourceInfo> symptomsItr = symptomSourceInfoRepository.findAll();
	 List<SymptomSourceInfo> symptoms = new ArrayList<SymptomSourceInfo>();
	 if( symptomsItr != null ) {
         symptoms =	(List<SymptomSourceInfo>) StreamSupport.stream(symptomsItr.spliterator(), false).collect(Collectors.<SymptomSourceInfo> toList());
	  }
	  return symptoms;
	}

	@Override
	public SymptomSourceInfo createSourceSymptom(SymptomSourceInfo sourceInfoModel) {		
		SymptomSourceInfo dbSource = null;
		if(sourceInfoModel != null ) {
			logger.info("Source :: "+ sourceInfoModel.getSource() );
		   dbSource = symptomSourceInfoRepository.findBySource(sourceInfoModel.getSource());
		   if(dbSource==null) {
			   dbSource = new SymptomSourceInfo();
			 Integer   sourceID = symptomSourceInfoRepository.getMaxSourceID();
			 if(sourceID==null) {
				 sourceID =0;
			 }
			    dbSource.setSourceID(sourceID + 1);
			    dbSource.setSource(sourceInfoModel.getSource());
				dbSource.setSourceType(sourceInfoModel.getSourceType());
				SymptomSourceInfo finalDb = symptomSourceInfoRepository.save(dbSource);
				return finalDb;
		   }
		   return dbSource;
		   
		}
		
	 return null;
	}

	@Override
	public List<SymptomSourceInfo> searchSources(String source) {
		// TODO Auto-generated method stub
		 List<SymptomSourceInfo> symptoms = new ArrayList<SymptomSourceInfo>();
		 if(source != null ) {
			 symptoms = symptomSourceInfoRepository.searchSource(source.toUpperCase());
		 }
		return symptoms;
	}

	@Override
	public void delete(Integer sourceID) {
		symptomSourceInfoRepository.deleteBySourceID(sourceID);
	}
	
	
	
}
