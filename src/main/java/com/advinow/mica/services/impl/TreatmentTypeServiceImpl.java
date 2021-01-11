/**
 * 
 */
package com.advinow.mica.services.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.resilience4j.retry.annotation.Retry;

import com.advinow.mica.domain.TreatmentTypeRef;
import com.advinow.mica.domain.TreatmentTypeRefDesc;
import com.advinow.mica.exception.DataCreateException;
import com.advinow.mica.mapper.TreatmentTypeRefRequestMapper;
import com.advinow.mica.mapper.TreatmentTypeRefResponseMapper;
import com.advinow.mica.model.TreatmentStatusModel;
import com.advinow.mica.model.TreatmentTypeRefGroups;
import com.advinow.mica.model.TreatmentTypeRefModel;
import com.advinow.mica.repositories.TreatmentTypeRefDescRepository;
import com.advinow.mica.repositories.TreatmentTypeRefRepository;
import com.advinow.mica.services.TreatmentTypeService;

/**
 * 
 * @author Govinda Reddy
 *
 */
@Service
@Retry(name = "neo4j")
public class TreatmentTypeServiceImpl implements TreatmentTypeService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	TreatmentTypeRefRepository  treatmentTypeRepository;
	@Autowired
	TreatmentTypeRefDescRepository treatmentTypeRefDescRepository;
	
	TreatmentTypeRefResponseMapper treatmentResponseMapper = new TreatmentTypeRefResponseMapper();

	TreatmentTypeRefRequestMapper treatmentRequestMapper = new TreatmentTypeRefRequestMapper();

	@Override
	public TreatmentTypeRefGroups getAllTreatmentTypes() {
		 Iterable<TreatmentTypeRef> treatmentTypes = treatmentTypeRepository.findAll(2);
		return treatmentResponseMapper.createTreatmentMapper(treatmentTypes);
	}

	@Override
	public TreatmentTypeRefModel getTreatmentByType(Integer type) {
		TreatmentTypeRef dbTreatmentType = treatmentTypeRepository.getTreatmentByTypeID(type);
		return treatmentResponseMapper.createTreatmentTypeModel(dbTreatmentType);
	}

	@Override
	public TreatmentStatusModel createtreatTrementType(
			TreatmentTypeRefModel treatmentTypeModel) {
		TreatmentTypeRef dbResultType = null;
		TreatmentStatusModel  treatmentStatusModel = new TreatmentStatusModel();
		TreatmentTypeRef dbTreatmentType = treatmentTypeRepository.getTreatmentTypeByName(treatmentTypeModel.getName().toUpperCase());
		Integer typeID = treatmentTypeRepository.getMaxTypeID();
		if(typeID == null ){
			typeID = 0;
		}
		Integer typeDescID = treatmentTypeRefDescRepository.getMaxTypeDescID(); 
		if(typeDescID == null ){
			typeDescID = 0;
		}
		TreatmentTypeRef 	dbTreatToSave = 	treatmentRequestMapper.createTreatmentType(treatmentTypeModel,typeID ,typeDescID);	      	
		if( dbTreatToSave.getDefaultTreatments().size() > 1 ) {
			logger.info("Default value size :: "+ dbTreatToSave.getDefaultTreatments().size());
			logger.info("Default value should be only one with TRUE for the TreatmentType");
			throw new DataCreateException("Default value should be only one with TRUE for the TreatmentType");
		} else if(dbTreatmentType != null &&  dbTreatToSave.getDefaultTreatments().size() ==1) {
			updateTreatmentDefaultValue(dbTreatmentType);
		}
		if(dbTreatmentType == null) {
			dbResultType = treatmentTypeRepository.save(dbTreatToSave);
		} else{
			mergeRecords(dbTreatmentType,dbTreatToSave);
			dbResultType = 	treatmentTypeRepository.save(dbTreatmentType);
		}
		if(dbResultType != null ) {
			treatmentStatusModel.setStatus("Created/updated successfully.........");
		} else{
			logger.info("Failed to Created/updated ...........");
			throw new DataCreateException("Failed to Created/updated ...........");
		}

		return treatmentStatusModel;
	}

	
	/**
	 * This method would update all the to null wherever default value is true
	 * 
	 * @param dbTreatmentType
	 */
	private void updateTreatmentDefaultValue(TreatmentTypeRef dbTreatmentType) {
		Set<TreatmentTypeRefDesc> dbTreatmentDesc = dbTreatmentType.getTreatmentDetails();
		if(dbTreatmentDesc != null ) {
			Iterator<TreatmentTypeRefDesc> dbTreatmentDescItr = dbTreatmentDesc.iterator();
		while(dbTreatmentDescItr.hasNext()){
			TreatmentTypeRefDesc dbTreatmentTypeDesc = 	dbTreatmentDescItr.next();
			if( dbTreatmentTypeDesc != null && dbTreatmentTypeDesc.getDefaultValue() != null && dbTreatmentTypeDesc.getDefaultValue()){
				dbTreatmentTypeDesc.setDefaultValue(null);
			}
		}
		}
	}

	/**
	 * This method would take the database treatment types and incoming 
	 * treatments from the request and merge the treatments which are not in the database.
	 * @param dbTreatmentType
	 * @param dbTreatToSave
	 */
	private void mergeRecords(TreatmentTypeRef dbTreatmentType,
			TreatmentTypeRef dbTreatToSave) {
		// TODO Auto-generated method stub
		Set<TreatmentTypeRefDesc> treatmentsdescToSave = dbTreatToSave.getTreatmentDetails();
		Set<TreatmentTypeRefDesc> dbTreatmentDesc = dbTreatmentType.getTreatmentDetails();
		Set<TreatmentTypeRefDesc> newRecords = new HashSet<TreatmentTypeRefDesc>();
		Iterator<TreatmentTypeRefDesc> treatmentToSaveItr = treatmentsdescToSave.iterator();     
		while(treatmentToSaveItr.hasNext()) {
			TreatmentTypeRefDesc treatmentToSave = treatmentToSaveItr.next(); 
			boolean  flag =	 updateTreatmentRecords(dbTreatmentDesc,treatmentToSave);
			if(!flag){
				newRecords.add(treatmentToSave);
			}
		}
		dbTreatmentDesc.addAll(newRecords);

	}
    /**
     * Check for the existng records and update with the request message.
     * 
     * @param dbTreatmentDesc
     * @param treatmentToSave
     * @return
     */
	private Boolean updateTreatmentRecords(
			Set<TreatmentTypeRefDesc> dbTreatmentDesc,
			TreatmentTypeRefDesc treatmentToSave) {
		if(dbTreatmentDesc != null ) {
			Iterator<TreatmentTypeRefDesc> dbTreatmentDescItr = dbTreatmentDesc.iterator();
			while(dbTreatmentDescItr.hasNext()){
				TreatmentTypeRefDesc dbTreatmentType = 	dbTreatmentDescItr.next();
				if(dbTreatmentType.getShortName().equalsIgnoreCase(treatmentToSave.getShortName())){
					dbTreatmentType.setDefaultValue(treatmentToSave.getDefaultValue()); 
					dbTreatmentType.setLongName(treatmentToSave.getLongName());
					return true;

				}
			}

		}
		return false;
	}

}
