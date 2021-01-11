package com.advinow.mica.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.advinow.mica.domain.Drug;
import com.advinow.mica.domain.DrugDosage;
import com.advinow.mica.domain.IllnessTreatment;
import com.advinow.mica.domain.NonDrug;
import com.advinow.mica.domain.RxNorms;
import com.advinow.mica.domain.SymptomTreatment;
import com.advinow.mica.domain.Treatment;
import com.advinow.mica.domain.TreatmentGroup;
import com.advinow.mica.domain.TreatmentPolicy;
import com.advinow.mica.domain.TreatmentSources;
import com.advinow.mica.domain.TreatmentTypeRef;
import com.advinow.mica.domain.TreatmentTypeRefDesc;
import com.advinow.mica.model.DrugDescModel;
import com.advinow.mica.model.DrugDocModel;
import com.advinow.mica.model.DrugDosageModel;
import com.advinow.mica.model.NonDrugDescModel;
import com.advinow.mica.model.RxNormsModel;
import com.advinow.mica.model.TreatmentDocModel;
import com.advinow.mica.model.TreatmentGroupModel;
import com.advinow.mica.model.TreatmentMainDocModel;
import com.advinow.mica.model.TreatmentMainModel;
import com.advinow.mica.model.TreatmentModel;
import com.advinow.mica.model.TreatmentPolicyModel;
import com.advinow.mica.model.TreatmentSourcesModel;
import com.advinow.mica.model.enums.PolicyAction;
import com.advinow.mica.model.enums.PolicyTarget;
import com.advinow.mica.model.enums.PolicyTargetCompare;
import com.advinow.mica.model.enums.PolicyTargetOperator;
import com.advinow.mica.repositories.TreatmentTypeRefRepository;

/**
 * 
 * @author Govinda Reddy
 *
 */
public class TreatmentResponseMapper {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	//TreatmentTypeRefRepository  treatmentTypeRepository;
	
	List<TreatmentTypeRef> treatmentTypeRefList = null;
	
    /**
     * 
     * @param dbIllnesTreatment
     * @param ttypeRepository
     * @param descFlag
     * @return TreatmentMainModel
     */
	//public TreatmentMainModel createIllnessTreatment(	IllnessTreatment dbIllnesTreatment, TreatmentTypeRefRepository ttypeRepository,Boolean descFlag) {
	public TreatmentMainModel createIllnessTreatment(	IllnessTreatment dbIllnesTreatment, List<TreatmentTypeRef> treatmentTypeList,Boolean descFlag) {
	//	treatmentTypeRepository  = ttypeRepository;
		treatmentTypeRefList = treatmentTypeList;
		TreatmentMainModel  treatmentMailModel = null;
		if(dbIllnesTreatment != null){
			treatmentMailModel = new TreatmentMainModel();
			treatmentMailModel.setIcd10Code(dbIllnesTreatment.getIcd10Code());
			treatmentMailModel.setName(dbIllnesTreatment.getName());
			treatmentMailModel.setTreatments(createTreatments(dbIllnesTreatment.getTreatments(),descFlag));
		}

		return treatmentMailModel;
	}
	

   /**
    *   
    * @param symptomTreatment
    * @param ttypeRepository
    * @param descFlag
    * @return TreatmentMainModel
    */
	public TreatmentMainModel createSymptomTreatment(
			SymptomTreatment symptomTreatment, List<TreatmentTypeRef> treatmentTypeList,Boolean descFlag) {
		treatmentTypeRefList = treatmentTypeList;
		TreatmentMainModel  treatmentMailModel = null;
		if(symptomTreatment != null){
			treatmentMailModel = new TreatmentMainModel();
			treatmentMailModel.setSymptomID(symptomTreatment.getCode());
			treatmentMailModel.setName(symptomTreatment.getName());
			treatmentMailModel.setTreatments(createTreatments(symptomTreatment.getTreatments(),descFlag));
		}

		return treatmentMailModel;
	}


	private List<TreatmentModel> createTreatments(Set<Treatment> dbTreatments,Boolean descFlag) {
		List<TreatmentModel>  modelTreatments = null;
		if(dbTreatments != null && ! dbTreatments.isEmpty()) {
			modelTreatments = new ArrayList<TreatmentModel>();
			Iterator<Treatment> dbTreatmentItr = dbTreatments.iterator();
			while(dbTreatmentItr.hasNext()) {
				Treatment dbTreatment = dbTreatmentItr.next();

				if(dbTreatment != null  &&  dbTreatment.getTypeID() != null ){
					modelTreatments.add( createTreatment(dbTreatment,descFlag));
				}
			}
		}
		// TODO Auto-generated method stub
		return modelTreatments;
	}




	private TreatmentModel createTreatment(Treatment dbTreatment,Boolean descFlag) {
		TreatmentModel   treatmentModel = new TreatmentModel();
		Integer typeID = dbTreatment.getTypeID();
		
		TreatmentTypeRef treatmentRefType =	treatmentTypeRefList.stream().filter(s->s.getTypeID()==typeID).findAny().orElse(null);
	//	TreatmentTypeRef treatmentRefType = treatmentTypeRepository.findTreatmentTypeID(typeID);
		if(treatmentRefType != null) {
		treatmentModel.setName(treatmentRefType.getName());
		treatmentModel.setTypeID(typeID);
		
	//	logger.info("treatmentRefType is not null for " + typeID);
		
		
		} 
		Set<TreatmentGroup> dbGorups = dbTreatment.getGroups();
		
		//logger.info("Type ID" + typeID);
		//dbGorups.sort((TreatmentGroup tg1, TreatmentGroup tg2)->tg1.getRank()-tg2.getRank());
		
		if(dbGorups != null ) {
			treatmentModel.setGroups(createGroups(dbGorups,typeID,descFlag));
		} else if(typeID != 0 && typeID != 12){
			if(!descFlag && treatmentRefType != null) {
				treatmentModel.setGroups(createDefaultGroup(treatmentRefType));
			}
		}


		// TODO Auto-generated method stub
		return treatmentModel;
	}


	private List<TreatmentGroupModel> createDefaultGroup(TreatmentTypeRef treatmentRefType) {
		List<TreatmentGroupModel> modelGroups = new ArrayList<TreatmentGroupModel>();
	Set<TreatmentTypeRefDesc> treatmentDetails = treatmentRefType.getTreatmentDetails();
	if(treatmentDetails != null && ! treatmentDetails.isEmpty() ) {
	TreatmentTypeRefDesc defaultType = treatmentDetails.stream().filter(s->s.getDefaultValue() != null && s.getDefaultValue()).findAny().orElse(null);
	if( defaultType != null) {
		TreatmentGroupModel treatmentGroupModel = new TreatmentGroupModel();
		treatmentGroupModel.setGroupName("A");
		List<Integer> typeDescIDs = new ArrayList<Integer>();
		typeDescIDs.add(defaultType.getTypeDescID());
	//	treatmentGroupModel.setTypeDescIDs(typeDescIDs);
		modelGroups.add(treatmentGroupModel);
	
	}
	

	}
		
	return modelGroups;
		
		
	}


	private List<TreatmentGroupModel> createGroups(Set<TreatmentGroup> dbGorups,Integer typeID,Boolean descFlag) {
		// TODO Auto-generated method stub
		List<TreatmentGroupModel> modelGroups = new ArrayList<TreatmentGroupModel>();
		Iterator<TreatmentGroup> dbtreatmentItr = dbGorups.iterator();
		while(dbtreatmentItr.hasNext())	   {
			TreatmentGroup dbTreatmentGroup = dbtreatmentItr.next();
			if(dbTreatmentGroup != null && dbTreatmentGroup.getAdded()==null){
				TreatmentGroupModel treatmentGroupModel = new TreatmentGroupModel();
				treatmentGroupModel.setGroupName(dbTreatmentGroup.getName());
				//treatmentGroupModel.setRank(dbTreatmentGroup.getRank());
				Set<Drug> drugs = dbTreatmentGroup.getDrugs();
				//if(typeID ==0 || typeID ==12){
				
				if(drugs != null && ! drugs.isEmpty()) {
					logger.info( typeID +  " drugs is not null SIZE :  " + drugs.size());
					

				
					// Remove typeDescIDs property once source integrated with UI
					//treatmentGroupModel.setDrugs(createDrugModel(drugs));
					treatmentGroupModel.setDrugDescriptions(createDrugsModel(drugs));
					// modelGroups.add(treatmentGroupModel);
				}
				Set<NonDrug> nonDrugs = dbTreatmentGroup.getNonDrungs();
				if(nonDrugs != null &&  !nonDrugs.isEmpty()){
					if(descFlag) {
						treatmentGroupModel.setDescriptions(createNonDrugDescriptions(nonDrugs, typeID));
					}
					else{
						// Remove typeDescIDs property once source integrated with UI
						
						//treatmentGroupModel.setTypeDescIDs(getTypeIDs(nonDrugs));
						treatmentGroupModel.setDescriptions(getTypeDesc(nonDrugs));
					}

				}
				treatmentGroupModel.setRank(dbTreatmentGroup.getRank());
				modelGroups.add(treatmentGroupModel);
			}


		}
		modelGroups.sort((TreatmentGroupModel tg1, TreatmentGroupModel tg2)->tg1.getRank()-tg2.getRank());
		
		
		
		return modelGroups;
	}

	private List<NonDrugDescModel> getTypeDesc(Set<NonDrug> nonDrugs) {
		// TODO Auto-generated method stub
		List<NonDrugDescModel> nonDrugDescList = new ArrayList<NonDrugDescModel>();
		Iterator<NonDrug> dbtreatmentItr = nonDrugs.iterator();
		while(dbtreatmentItr.hasNext())	   {
			NonDrug dbTreatmentDesc = dbtreatmentItr.next();
			NonDrugDescModel nonDrugDesc = new NonDrugDescModel();
			nonDrugDesc.setRank(dbTreatmentDesc.getRank());
			nonDrugDesc.setTypeDescID(dbTreatmentDesc.getTypeDescID());
			nonDrugDesc.setLikelihood(dbTreatmentDesc.getLikelihood());
			if(dbTreatmentDesc.getNotes() != null) {
			nonDrugDesc.setNotes(dbTreatmentDesc.getNotes());
		}
			//nonDrugDesc.setSources(dbTreatmentDesc.getSources());
			if( dbTreatmentDesc.getSourceInfo() != null ) {
				nonDrugDesc.setSourceInfo(prepareModelSources(dbTreatmentDesc.getSourceInfo()));
				}
			
			
			nonDrugDescList.add(nonDrugDesc);
		}

		nonDrugDescList.sort((NonDrugDescModel drug1, NonDrugDescModel drug2)->drug1.getRank()-drug2.getRank());
		//List<Integer> drugList = nonDrugDescList.stream().map(e->e.getTypeDescID()).collect(Collectors.toList());

		return nonDrugDescList;
	}

	/*private List<Integer> getTypeIDs(Set<NonDrug> nonDrugs) {
		// TODO Auto-generated method stub
		List<NonDrugDescModel> nonDrugDescList = new ArrayList<NonDrugDescModel>();
		Iterator<NonDrug> dbtreatmentItr = nonDrugs.iterator();
		while(dbtreatmentItr.hasNext())	   {
			NonDrug dbTreatmentDesc = dbtreatmentItr.next();
			NonDrugDescModel nonDrugDesc = new NonDrugDescModel();
			nonDrugDesc.setRank(dbTreatmentDesc.getRank());
			nonDrugDesc.setTypeDescID(dbTreatmentDesc.getTypeDescID());
			nonDrugDescList.add(nonDrugDesc);
		}

		nonDrugDescList.sort((NonDrugDescModel drug1, NonDrugDescModel drug2)->drug1.getRank()-drug2.getRank());
		List<Integer> drugList = nonDrugDescList.stream().map(e->e.getTypeDescID()).collect(Collectors.toList());

		return drugList;
	}
*/



	private List<NonDrugDescModel> createNonDrugDescriptions(Set<NonDrug> dbTreatmentDescList,Integer typeID) {
		// TODO Auto-generated method stub

	//	TreatmentTypeRef treatmentRefType = treatmentTypeRepository.getTreatmentByTypeID(typeID);
		
		
		TreatmentTypeRef treatmentRefType =	treatmentTypeRefList.stream().filter(s->s.getTypeID()==typeID).findAny().orElse(null);
		
		
		

		// TODO Auto-generated method stub
		List<NonDrugDescModel>    treatmentDescModelList = null;
		if(dbTreatmentDescList != null && ! dbTreatmentDescList.isEmpty()  && treatmentRefType != null) {
			treatmentDescModelList = new ArrayList<NonDrugDescModel>();
			Set<TreatmentTypeRefDesc> treatmentRefDetails = treatmentRefType.getTreatmentDetails();

			Iterator<NonDrug> dbtreatmentItr = dbTreatmentDescList.iterator();
			while(dbtreatmentItr.hasNext())	   {
				NonDrug dbTreatmentDesc = dbtreatmentItr.next();
				NonDrugDescModel treatmentDescModel = new NonDrugDescModel();
				treatmentDescModel.setTypeDescID(dbTreatmentDesc.getTypeDescID());
				treatmentDescModel.setLikelihood(dbTreatmentDesc.getLikelihood());
				if(dbTreatmentDesc.getNotes() != null) {
				treatmentDescModel.setNotes(dbTreatmentDesc.getNotes());
				}
				poupulateDescInfofromRefDesc(treatmentDescModel,dbTreatmentDesc,treatmentRefDetails);
				treatmentDescModelList.add(treatmentDescModel);
			}
		}

		return treatmentDescModelList;

	}



	private List<String> createDrugModel(Set<Drug> drugs) {
		// TODO Auto-generated method stub
		List<DrugDescModel>    	drugsDesc = new ArrayList<DrugDescModel>();
		if(drugs != null && ! drugs.isEmpty()) {
			Iterator<Drug> dbtreatmentItr = drugs.iterator();
			while(dbtreatmentItr.hasNext())	   {
				Drug dbDrug = dbtreatmentItr.next();
				drugsDesc.add(createDrugDescription(dbDrug));
			}
		}

		drugsDesc.sort((DrugDescModel drug1, DrugDescModel drug2)->drug1.getRank()-drug2.getRank());
		List<String> drugList = drugsDesc.stream().map(e->e.getDrugName()).collect(Collectors.toList());


		return drugList;

	}
	
	
	
	private List<DrugDescModel>   createDrugsModel(Set<Drug> drugs) {
		// TODO Auto-generated method stub
		List<DrugDescModel>    	drugsDesc = new ArrayList<DrugDescModel>();
	//	if(drugs != null && ! drugs.isEmpty()) {
			Iterator<Drug> dbtreatmentItr = drugs.iterator();
			while(dbtreatmentItr.hasNext())	   {
				Drug dbDrug = dbtreatmentItr.next();
				drugsDesc.add(createDrugDescription(dbDrug));
			}
		//}

		drugsDesc.sort((DrugDescModel drug1, DrugDescModel drug2)->drug1.getRank()-drug2.getRank());
		//List<String> drugList = drugsDesc.stream().map(e->e.getDrugName()).collect(Collectors.toList());
		return drugsDesc;

	}


	private DrugDescModel createDrugDescription(Drug dbDrug) {

		DrugDescModel drugDescModel =  null;

		if(dbDrug != null) {
			drugDescModel = new DrugDescModel();
			drugDescModel.setRank(dbDrug.getRank());
			drugDescModel.setDrugName(dbDrug.getName());
			drugDescModel.setLikelihood(dbDrug.getLikelihood());
		//	drugDescModel.setSources(dbDrug.getSources());
			if( dbDrug.getSourceInfo() != null ) {
			drugDescModel.setSourceInfo(prepareModelSources(dbDrug.getSourceInfo()));
			}
			
			if( dbDrug.getRxNorms() != null ) {
				drugDescModel.setRxNorms(prepareModelRxNorms(dbDrug.getRxNorms()));
			}
			
		/*	
			
			if( dbDrug.getPolicies() != null ) {
				drugDescModel.setPolicies(prepareModelPolicies(dbDrug.getPolicies()));
			}*/
	
				
			
		}
		return drugDescModel;
	}
	
	private List<TreatmentPolicyModel> prepareModelPolicies(
			Set<TreatmentPolicy> policies) {
		// TODO Auto-generated method stub
		List<TreatmentPolicyModel> modelPolicies = new ArrayList<TreatmentPolicyModel>();
		for (TreatmentPolicy treatmentPolicy : policies) {
			TreatmentPolicyModel modelPolicy = new TreatmentPolicyModel();
			if (treatmentPolicy.getAction() != null) {
			modelPolicy.setAction(PolicyAction.fromText(treatmentPolicy.getAction()));
			}
			if(treatmentPolicy.getTarget() != null) {
			modelPolicy.setTarget(PolicyTarget.fromText(treatmentPolicy.getTarget()));
			}
			if(treatmentPolicy.getTargetCompare() != null) {
			modelPolicy.setTargetCompare(PolicyTargetCompare.fromText(treatmentPolicy.getTargetCompare()));
			}
			
			if(treatmentPolicy.getTargetOperator() != null) {
			modelPolicy.setTargetOperator(PolicyTargetOperator.fromText(treatmentPolicy.getTargetOperator()));
			}
			
			modelPolicy.setPolicyID(treatmentPolicy.getPolicyID());
			modelPolicy.setName(treatmentPolicy.getName());
			modelPolicy.setTargetDetail(treatmentPolicy.getTargetDetail());
			
		
			modelPolicies.add(modelPolicy);
		}
		
		return modelPolicies;
	}

	/*private DrugDosageModel prepareModelDosage(DrugDosage drugDosage) {
		DrugDosageModel dosageModel = null ;
		if(drugDosage != null) {
		dosageModel = new DrugDosageModel();
		dosageModel.setDaw(drugDosage.getDaw());
		dosageModel.setDirections(drugDosage.getDirections());
		dosageModel.setDispenseForm(drugDosage.getDispenseForm());
		dosageModel.setForm(drugDosage.getForm());
		dosageModel.setFrequency(drugDosage.getFrequency());
		dosageModel.setMeasurement(drugDosage.getMeasurement());
		dosageModel.setPrn(drugDosage.getPrn());
		dosageModel.setQuantity(drugDosage.getQuantity());
		dosageModel.setRoute(drugDosage.getRoute());
		dosageModel.setStrength(drugDosage.getStrength());
		dosageModel.setAmount(drugDosage.getAmount());
		}
	// TODO Auto-generated method stub
	return dosageModel;
}*/



	private List<RxNormsModel> prepareModelRxNorms(Set<RxNorms> rxNorms) {
			List<RxNormsModel> rxNormsModel = new ArrayList<RxNormsModel>();
	 for (RxNorms rxNorm : rxNorms) {
			RxNormsModel rxNormModel = new RxNormsModel();
			rxNormModel.setRxcui(rxNorm.getRxcui());
			rxNormModel.setAddedBy(rxNorm.getAddedBy());
			rxNormsModel.add(rxNormModel);
		 }
		// TODO Auto-generated method stub
		return rxNormsModel;
	}


	private List<TreatmentSourcesModel> prepareModelSources(
			Set<TreatmentSources> dbSources) {
		// TODO Auto-generated method stub
		List<TreatmentSourcesModel>  modelSources = new ArrayList<TreatmentSourcesModel>();
		//Set<TreatmentSources> dbSources = dbDataStore.getSourceInfo();
		if(dbSources != null) {
			List<TreatmentSources> finalSources = adjustSources(dbSources);
			for (TreatmentSources dbSource : finalSources) {
			TreatmentSourcesModel treatmentSourcesModel = new TreatmentSourcesModel();
			treatmentSourcesModel.setAddedBy(dbSource.getAddedBy());
			treatmentSourcesModel.setVerified(dbSource.getVerified());
			treatmentSourcesModel.setSourceID(dbSource.getSourceID());
			treatmentSourcesModel.setSourceInfo(dbSource.getSourceInfo());
			treatmentSourcesModel.setSourceRefDate(new Date().getTime());
			modelSources.add(treatmentSourcesModel);
		}
		
		}
		
		return modelSources;
	}

	
	private List<TreatmentSources> adjustSources(Set<TreatmentSources> dbSources) {
	// TODO Auto-generated method stub
		List<TreatmentSources> sources =new ArrayList<TreatmentSources>();
		if(dbSources != null && ! dbSources.isEmpty()) {
			sources.addAll(dbSources.stream().filter(s->s.getAddedBy().equalsIgnoreCase("Doctor") && s.getVerified()).collect(Collectors.toList()));
			sources.addAll(dbSources.stream().filter(s->s.getAddedBy().equalsIgnoreCase("NLP") && s.getVerified()).collect(Collectors.toList()));
		    sources.addAll(dbSources.stream().filter(s->s.getAddedBy().equalsIgnoreCase("Doctor") && !s.getVerified()).collect(Collectors.toList()));
			sources.addAll(dbSources.stream().filter(s->s.getAddedBy().equalsIgnoreCase("NLP") && !s.getVerified()).collect(Collectors.toList()));
		}
		return sources;
	
}

	

	private void poupulateDescInfofromRefDesc(
			NonDrugDescModel treatmentDescModel,
			NonDrug dbTreatmentDesc,
			Set<TreatmentTypeRefDesc> treatmentRefDetails) {
		if(treatmentRefDetails != null && ! treatmentRefDetails.isEmpty() ){
			Iterator<TreatmentTypeRefDesc> dbTreatmentDetailsItr = treatmentRefDetails.iterator();
			while(dbTreatmentDetailsItr.hasNext()){
				TreatmentTypeRefDesc dbTreatmenytDetail = dbTreatmentDetailsItr.next();
				if(dbTreatmentDesc.getTypeDescID().equals(dbTreatmenytDetail.getTypeDescID())){
					treatmentDescModel.setLongName(dbTreatmenytDetail.getLongName());
					treatmentDescModel.setShortName(dbTreatmenytDetail.getShortName());
					treatmentDescModel.setDescription(dbTreatmenytDetail.getDescription());
									
					//treatmentDescModel.setSources(dbTreatmentDesc.getSources());
					
					treatmentDescModel.setLikelihood(dbTreatmentDesc.getLikelihood());
					
					if( dbTreatmentDesc.getSourceInfo() != null ) {
						treatmentDescModel.setSourceInfo(prepareModelSources(dbTreatmentDesc.getSourceInfo()));
					}
					
					if(dbTreatmentDesc.getRank() != null) {
						treatmentDescModel.setRank(dbTreatmentDesc.getRank());
					}
					if(dbTreatmenytDetail.getPriority() != null) {
						treatmentDescModel.setPriority(dbTreatmenytDetail.getPriority());
					}
					
				
					break;
				}

			}

		}
	}


	public TreatmentMainDocModel createIllnessDocTreatment(
			IllnessTreatment dbIllnesTreatment, List<TreatmentTypeRef> treatmentTypeList) {
		treatmentTypeRefList = treatmentTypeList;

		TreatmentMainDocModel  treatmentMailModel = null;
		if(dbIllnesTreatment != null){
			treatmentMailModel = new TreatmentMainDocModel();
			treatmentMailModel.setIcd10Code(dbIllnesTreatment.getIcd10Code());
			treatmentMailModel.setName(dbIllnesTreatment.getName());
			treatmentMailModel.setTreatments(createDocTreatments(dbIllnesTreatment.getTreatments()));
		}

		return treatmentMailModel;
	}


	private List<TreatmentDocModel> createDocTreatments(Set<Treatment> dbTreatments) {
		List<TreatmentDocModel>  modelTreatments = null;
		if(dbTreatments != null && ! dbTreatments.isEmpty()) {
			modelTreatments = new ArrayList<TreatmentDocModel>();
			Iterator<Treatment> dbTreatmentItr = dbTreatments.iterator();
			while(dbTreatmentItr.hasNext()) {
				Treatment dbTreatment = dbTreatmentItr.next();

				if(dbTreatment != null  &&  dbTreatment.getTypeID() != null ){
					modelTreatments.add( createDocTreatment(dbTreatment));
				}
			}
		}
		// TODO Auto-generated method stub
		return modelTreatments;
	}


	private TreatmentDocModel createDocTreatment(Treatment dbTreatment) {
		TreatmentDocModel   treatmentModel = new TreatmentDocModel();
		Integer typeID = dbTreatment.getTypeID();
		
		//TreatmentTypeRef treatmentRefType = treatmentTypeRepository.findTreatmentTypeID(typeID);
		TreatmentTypeRef treatmentRefType =	treatmentTypeRefList.stream().filter(s->s.getTypeID()==typeID).findAny().orElse(null);
		
		if(treatmentRefType != null ) {
		
		treatmentModel.setType(treatmentRefType.getName());	 
		//Set<TreatmentGroup> dbGorups = dbTreatment.getGroups();
		Set<TreatmentGroup> dbGorups = dbTreatment.getGroups();
		if(dbGorups != null ) {
			treatmentModel.setDetails(createDocDetails(dbGorups,typeID));

		} 
		}


		// TODO Auto-generated method stub
		return treatmentModel;
	}


	private List<DrugDocModel> createDocDetails(
			Set<TreatmentGroup> dbGorups, Integer typeID) {
		// TODO Auto-generated method stub
		List<DrugDocModel> details = new ArrayList<DrugDocModel>();
		Iterator<TreatmentGroup> dbtreatmentItr = dbGorups.iterator();
		while(dbtreatmentItr.hasNext())	   {
			TreatmentGroup dbTreatmentGroup = dbtreatmentItr.next();
			if(dbTreatmentGroup != null){
				//DrugDocModel detail = new DrugDocModel();
				String groupName = dbTreatmentGroup.getName();
				//treatmentGroupModel.setRank(dbTreatmentGroup.getRank());
				Set<Drug> drugs = dbTreatmentGroup.getDrugs();
				if(drugs != null &&  !drugs.isEmpty()){	 	    		
					createdDocDrugs(drugs,details,groupName);
				}

				Set<NonDrug> nonDrugs = dbTreatmentGroup.getNonDrungs();

				if(nonDrugs != null &&  !nonDrugs.isEmpty()){	 	    		
					createDocNonDrug(nonDrugs, typeID,details,groupName);
				}

			}

		}


		return details;




	}


	private void createDocNonDrug(Set<NonDrug> nonDrugs, Integer typeID,
			List<DrugDocModel> details, String groupName) {
		// TODO Auto-generated method stub

	//	TreatmentTypeRef treatmentRefType = treatmentTypeRepository.getTreatmentByTypeID(typeID);
		TreatmentTypeRef treatmentRefType =	treatmentTypeRefList.stream().filter(s->s.getTypeID()==typeID).findAny().orElse(null);
		
		if(treatmentRefType != null ) {

		Set<TreatmentTypeRefDesc> treatmentRefDetails = treatmentRefType.getTreatmentDetails();

		Iterator<NonDrug> dbtreatmentItr = nonDrugs.iterator();
		while(dbtreatmentItr.hasNext())	   {
			NonDrug dbTreatmentDesc = dbtreatmentItr.next();
			DrugDocModel treatmentDescModel = new DrugDocModel();
			treatmentDescModel.setGroupName(groupName);
			poupulateDocDescInfofromRefDesc(treatmentDescModel,dbTreatmentDesc,treatmentRefDetails);
			details.add(treatmentDescModel);
		}
		}


	}


	private void createdDocDrugs(Set<Drug> drugs, List<DrugDocModel> details,
			String groupName) {
		Iterator<Drug> dbtreatmentItr = drugs.iterator();
		while(dbtreatmentItr.hasNext())	   {
			Drug dbDrug = dbtreatmentItr.next();
			DrugDocModel drugDocModel = new DrugDocModel();
			drugDocModel.setGroupName(groupName);
			drugDocModel.setRank(dbDrug.getRank());
			drugDocModel.setName(dbDrug.getName());
			details.add(drugDocModel);
		}

	}

	private void poupulateDocDescInfofromRefDesc(
			DrugDocModel treatmentDescModel,
			NonDrug dbTreatmentDesc,
			Set<TreatmentTypeRefDesc> treatmentRefDetails) {
		if(treatmentRefDetails != null && ! treatmentRefDetails.isEmpty() ){
			Iterator<TreatmentTypeRefDesc> dbTreatmentDetailsItr = treatmentRefDetails.iterator();
			while(dbTreatmentDetailsItr.hasNext()){
				TreatmentTypeRefDesc dbTreatmenytDetail = dbTreatmentDetailsItr.next();
				if(dbTreatmentDesc.getTypeDescID().equals(dbTreatmenytDetail.getTypeDescID())){
					treatmentDescModel.setNameDetails(dbTreatmenytDetail.getLongName());
					treatmentDescModel.setName(dbTreatmenytDetail.getShortName());
					treatmentDescModel.setDescription(dbTreatmenytDetail.getDescription());
					if(dbTreatmentDesc.getRank() != null) {
						treatmentDescModel.setRank(dbTreatmentDesc.getRank());
					}
					if(dbTreatmenytDetail.getPriority() != null) {
						treatmentDescModel.setPriority(dbTreatmenytDetail.getPriority());
					}

					break;
				}

			}
		}
	}

    /**
     * 
     * @param symptomTreatment
     * @param ttypeRepository
     * @return TreatmentMainDocModel
     */
	public TreatmentMainDocModel createSymptomDocTreatment(
			SymptomTreatment symptomTreatment, List<TreatmentTypeRef> treatmentTypeList) {
		treatmentTypeRefList = treatmentTypeList;
		TreatmentMainDocModel  treatmentMailModel = null;
		if(symptomTreatment != null){
			treatmentMailModel = new TreatmentMainDocModel();
			treatmentMailModel.setSymptomID(symptomTreatment.getCode());
			treatmentMailModel.setName(symptomTreatment.getName());
			treatmentMailModel.setTreatments(createDocTreatments(symptomTreatment.getTreatments()));
		}

		return treatmentMailModel;
	}






}