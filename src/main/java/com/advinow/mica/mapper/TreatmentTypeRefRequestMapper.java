package com.advinow.mica.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.advinow.mica.domain.TreatmentTypeRef;
import com.advinow.mica.domain.TreatmentTypeRefDesc;
import com.advinow.mica.model.TreatmentTypeRefDescModel;
import com.advinow.mica.model.TreatmentTypeRefModel;

/**
 * 
 * @author Govinda Reddy
 *
 */
public class TreatmentTypeRefRequestMapper {
	
	private Integer typeDescID= 0;
	
	public TreatmentTypeRef createTreatmentType(
			TreatmentTypeRefModel treatmentTypeModel, Integer dbTypeID, Integer typeDescID) {
		    this.typeDescID = typeDescID;
 	
    		TreatmentTypeRef  dbTreatmentType = new TreatmentTypeRef();
			if(treatmentTypeModel != null ){
				List<String> defaultTreatments =   new ArrayList<String>();
				dbTreatmentType.setName(treatmentTypeModel.getName());
				Integer typeId = treatmentTypeModel.getTypeID();
				if(typeId == null ) {
					typeId = dbTypeID + 1;
				}
				
				dbTreatmentType.setTypeID(typeId);
				dbTreatmentType.setType(treatmentTypeModel.getType());
				dbTreatmentType.setTreatmentDetails(createtreatmentDetails(treatmentTypeModel.getTreatmentTypeDesc(),defaultTreatments));
				dbTreatmentType.setDefaultTreatments(defaultTreatments);
			}
			return dbTreatmentType;
	}
	private Set<TreatmentTypeRefDesc> createtreatmentDetails(
			List<TreatmentTypeRefDescModel> treatmentDescModel, List<String> defaultTreatments) {
		Set<TreatmentTypeRefDesc> dbtreatmentTypeDesc =  new HashSet<TreatmentTypeRefDesc>();		
			 if(treatmentDescModel != null && ! treatmentDescModel.isEmpty() ){
				 for (int i = 0; i < treatmentDescModel.size(); i++) {
					 TreatmentTypeRefDescModel  treatmentTypeDescModel = treatmentDescModel.get(i);
					 dbtreatmentTypeDesc.add(createTreatmentTypeDetail(treatmentTypeDescModel)); 
					 if(treatmentTypeDescModel.getDefaultValue() !=  null && treatmentTypeDescModel.getDefaultValue()){
						 defaultTreatments.add(treatmentTypeDescModel.getShortName());
					 }
			    	}
					 
				}
		
		// TODO Auto-generated method stub
		return dbtreatmentTypeDesc;
	}
	

	private TreatmentTypeRefDesc createTreatmentTypeDetail(
			TreatmentTypeRefDescModel treatmenytDescModel) {
		// TODO Auto-generated method stub
		TreatmentTypeRefDesc  dbTreatmenytDesc = new TreatmentTypeRefDesc();
         if(treatmenytDescModel != null ){
        	 dbTreatmenytDesc.setDefaultValue(treatmenytDescModel.getDefaultValue()); 
        	 dbTreatmenytDesc.setLongName(treatmenytDescModel.getLongName());
        	 dbTreatmenytDesc.setShortName(treatmenytDescModel.getShortName());        	 
        	 dbTreatmenytDesc.setPriority(treatmenytDescModel.getPriority());    
        	 typeDescID = typeDescID +1;
        	 dbTreatmenytDesc.setTypeDescID(typeDescID);
         }
		
		
		return dbTreatmenytDesc;
	}

}
