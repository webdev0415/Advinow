package com.advinow.mica.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.advinow.mica.domain.CPTCodes;
import com.advinow.mica.domain.SnomedCodes;
import com.advinow.mica.domain.TreatmentTypeRef;
import com.advinow.mica.domain.TreatmentTypeRefDesc;
import com.advinow.mica.model.TreatmentTypeRefDescModel;
import com.advinow.mica.model.TreatmentTypeRefGroups;
import com.advinow.mica.model.TreatmentTypeRefModel;

/**
 * 
 * @author Govinda Reddy
 *
 */
public class TreatmentTypeRefResponseMapper {
/*
	public TreatmentGroups createTreatmentMapper(
			Set<TreatmentType> dbTreatmentTypes) {
		TreatmentGroups treatmentGroups = new TreatmentGroups();
		List<TreatmentTypeModel> treatmentTypes = new ArrayList<TreatmentTypeModel>();
		if (dbTreatmentTypes != null && !dbTreatmentTypes.isEmpty()) {
			Iterator<TreatmentType> dbTreatmentTypeItr = dbTreatmentTypes
					.iterator();
			while (dbTreatmentTypeItr.hasNext()) {
				TreatmentType dbTreatmentType = dbTreatmentTypeItr.next();
				treatmentTypes.add(createTreatmentTypeModel(dbTreatmentType));
			}
		}

		treatmentGroups.setTreatmentTypes(treatmentTypes);
		return treatmentGroups;
	}*/
	
	public TreatmentTypeRefGroups createTreatmentMapper(
			Iterable<TreatmentTypeRef> dbTreatmentTypes) {
		TreatmentTypeRefGroups treatmentGroups = null;
		List<TreatmentTypeRefModel> treatmentTypes = new ArrayList<TreatmentTypeRefModel>();
		if (dbTreatmentTypes != null ) {
			 treatmentGroups = new TreatmentTypeRefGroups();
			 treatmentTypes = new ArrayList<TreatmentTypeRefModel>();		
			Iterator<TreatmentTypeRef> dbTreatmentTypeItr = dbTreatmentTypes.iterator();
			while (dbTreatmentTypeItr.hasNext()) {
				TreatmentTypeRef dbTreatmentType = dbTreatmentTypeItr.next();
				if(dbTreatmentType.getActive()) {
				treatmentTypes.add(createTreatmentTypeModel(dbTreatmentType));
				}
			}
			treatmentGroups.setTreatmentTypes(treatmentTypes);
		}

	
		return treatmentGroups;
	}

	public TreatmentTypeRefModel createTreatmentTypeModel(
			TreatmentTypeRef dbTreatmentType) {
		// TODO Auto-generated method stub
		TreatmentTypeRefModel  treatmentTypeModel =  new TreatmentTypeRefModel();;
		if(dbTreatmentType != null ){
			treatmentTypeModel.setName(dbTreatmentType.getName());
			treatmentTypeModel.setTypeID(dbTreatmentType.getTypeID());
			//treatmentTypeModel.setType(dbTreatmentType.getType());
			treatmentTypeModel.setTreatmentTypeDesc(createtreatmentDetails(dbTreatmentType.getTreatmentDetails()));
		}
		return treatmentTypeModel;
	}

	private List<TreatmentTypeRefDescModel> createtreatmentDetails(
			Set<TreatmentTypeRefDesc> dbTreatmentDetails) {
			 List<TreatmentTypeRefDescModel>  treatmentTypeDetails =  new ArrayList<TreatmentTypeRefDescModel>();
		
			 if(dbTreatmentDetails != null && ! dbTreatmentDetails.isEmpty() ){
				 Iterator<TreatmentTypeRefDesc> dbTreatmentDetailsItr = dbTreatmentDetails.iterator();
				 while(dbTreatmentDetailsItr.hasNext()){
					 TreatmentTypeRefDesc dbTreatmenytDetail = dbTreatmentDetailsItr.next();
					 treatmentTypeDetails.add(createTreatmentTypeDetail(dbTreatmenytDetail));
					 
				 }
			 }
		
		// TODO Auto-generated method stub
		return treatmentTypeDetails;
	}

	private TreatmentTypeRefDescModel createTreatmentTypeDetail(
			TreatmentTypeRefDesc dbTreatmenytDetail) {
		// TODO Auto-generated method stub
		TreatmentTypeRefDescModel  treatmentTypeDetailsModel = null;
         if(dbTreatmenytDetail != null ){
        	 treatmentTypeDetailsModel = new TreatmentTypeRefDescModel();
        	 treatmentTypeDetailsModel.setDefaultValue(dbTreatmenytDetail.getDefaultValue()); 
        	 treatmentTypeDetailsModel.setLongName(dbTreatmenytDetail.getLongName());
        	 treatmentTypeDetailsModel.setShortName(dbTreatmenytDetail.getShortName());        	 
        	 treatmentTypeDetailsModel.setPriority(dbTreatmenytDetail.getPriority());        
             treatmentTypeDetailsModel.setTypeDescID(dbTreatmenytDetail.getTypeDescID());
             treatmentTypeDetailsModel.setDescription(dbTreatmenytDetail.getDescription());
         
             
             Set<CPTCodes> cptCodes = dbTreatmenytDetail.getcPTCodes();
             if(cptCodes != null  && ! cptCodes.isEmpty() && cptCodes.size() > 0) {
            	 CPTCodes cpt = cptCodes.stream().findFirst().orElse(null);
            	 if(cpt != null &&  cpt.getCptCode() != null && ! cpt.getCptCode().isEmpty()  && cpt.getCptCode().size() > 0 ) {
                treatmentTypeDetailsModel.setCptCode(cpt.getCptCode());
            	 }
             }
             
             
             Set<SnomedCodes> snomedCodes = dbTreatmenytDetail.getSnomedCodes();
             if(snomedCodes != null  && ! snomedCodes.isEmpty() && snomedCodes.size() > 0) {
            	 SnomedCodes snc = snomedCodes.stream().findFirst().orElse(null);
            	 if(snc != null &&  snc.getConceptID() != null && ! snc.getConceptID().isEmpty()  && snc.getConceptID().size() > 0 ) {
                treatmentTypeDetailsModel.setConceptID(snc.getConceptID());
            	 }
             }
           
             
           /*  
             Set<CPTCodes> cptCodes = dbTreatmenytDetail.getcPTCodes();
            if(cptCodes != null && ! cptCodes.isEmpty() && cptCodes.size() > 0 ) {
            	
            CPTCodes cpt = cptCodes.stream().findFirst().orElse(null);
            		if(cpt != null) {
            			
            			treatmentTypeDetailsModel.setCptCodes(cpt.getCptCodes());
            		}
                     
            	
            
             
         }*/
         }
		
		return treatmentTypeDetailsModel;
	}
	
}
