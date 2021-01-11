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

import com.advinow.mica.domain.TreatmentSourceInfo;
import com.advinow.mica.repositories.TreatmentSourceInfoRepository;
import com.advinow.mica.services.TreatmentSourceInfoService;

@Service
@Retry(name = "neo4j")
public class TreatmentSourceInfoServiceImpl implements TreatmentSourceInfoService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired	
	TreatmentSourceInfoRepository treatmentSourceInfoRepository;

	@Override
	public List<TreatmentSourceInfo> getSourceSymptoms() {
	 Iterable<TreatmentSourceInfo> symptomsItr = treatmentSourceInfoRepository.findAll();
	 List<TreatmentSourceInfo> symptoms = new ArrayList<TreatmentSourceInfo>();
	 if( symptomsItr != null ) {
         symptoms =	(List<TreatmentSourceInfo>) StreamSupport.stream(symptomsItr.spliterator(), false).collect(Collectors.<TreatmentSourceInfo> toList());
	  }
	  return symptoms;
	}

	/*@Override
	public TreatmentSourceInfo createSourceSymptom(TreatmentSourceInfo sourceInfoModel) {		
		TreatmentSourceInfo dbSource = null;
		if(sourceInfoModel != null ) {
			logger.info("Source :: "+ sourceInfoModel.getSource() );
		   dbSource = treatmentSourceInfoRepository.findBySource(sourceInfoModel.getSource());
		   if(dbSource==null) {
			   dbSource = new TreatmentSourceInfo();
			 Integer   sourceID = treatmentSourceInfoRepository.getMaxSourceID();
			 if(sourceID==null) {
				 sourceID =0;
			 }
			   
		   }
		   
		}
		 dbSource.setSourceID(sourceID + 1);
		logger.info("Source ID :: "+ dbSource.getSourceID() );
		dbSource.setSource(sourceInfoModel.getSource());
		dbSource.setSourceType(sourceInfoModel.getSourceType());
		dbSource.setSourceTitle(sourceInfoModel.getSourceTitle());
		TreatmentSourceInfo finalDb = treatmentSourceInfoRepository.save(dbSource);
		return finalDb;
	
	}*/
	
	@Override
	public TreatmentSourceInfo createSourceSymptom(TreatmentSourceInfo sourceInfoModel) {		
		TreatmentSourceInfo dbSource = null;
		if(sourceInfoModel != null ) {
			logger.info("Source :: "+ sourceInfoModel.getSource() );
		   dbSource = treatmentSourceInfoRepository.findBySource(sourceInfoModel.getSource());
		   if(dbSource==null) {
			   dbSource = new TreatmentSourceInfo();
			 Integer   sourceID = treatmentSourceInfoRepository.getMaxSourceID();
			 if(sourceID==null) {
				 sourceID =0;
			 }
			    dbSource.setSourceID(sourceID + 1);
				logger.info("sourceID :: "+ dbSource.getSourceID());
			    dbSource.setSource(sourceInfoModel.getSource());
				dbSource.setSourceType(sourceInfoModel.getSourceType());
				TreatmentSourceInfo finalDb = treatmentSourceInfoRepository.save(dbSource);
				return finalDb;
		   }
		   return dbSource;
		   
		}
		
	 return null;
	}
	

	@Override
	public List<TreatmentSourceInfo> searchSources(String source) {
		// TODO Auto-generated method stub
		 List<TreatmentSourceInfo> symptoms = new ArrayList<TreatmentSourceInfo>();
		 if(source != null ) {
			 symptoms = treatmentSourceInfoRepository.searchSource(source.toUpperCase());
		 }
		return symptoms;
	}

	@Override
	public void delete(Integer sourceID) {
		treatmentSourceInfoRepository.deleteBySourceID(sourceID);
		
	}
	
}
