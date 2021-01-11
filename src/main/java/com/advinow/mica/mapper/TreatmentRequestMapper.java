package com.advinow.mica.mapper;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.advinow.mica.model.DrugDescModel;
import com.advinow.mica.model.DrugDosageModel;
import com.advinow.mica.model.NonDrugDescModel;
import com.advinow.mica.model.RxNormsModel;
import com.advinow.mica.model.TreatmentGroupModel;
import com.advinow.mica.model.TreatmentMainModel;
import com.advinow.mica.model.TreatmentModel;
import com.advinow.mica.model.TreatmentPolicyModel;
import com.advinow.mica.model.TreatmentSourcesModel;
import com.advinow.mica.model.enums.TimeType;

/**
 *
 * @author Govinda Reddy
 *
 */
public class TreatmentRequestMapper {

	public IllnessTreatment createDbIllnessTreatment(
			TreatmentMainModel illnessTreatmentModel) {
			IllnessTreatment dbIllnessTreatment = new IllnessTreatment();
	   	if(illnessTreatmentModel != null ) {
			dbIllnessTreatment.setIcd10Code(illnessTreatmentModel.getIcd10Code().toUpperCase());
			dbIllnessTreatment.setName(illnessTreatmentModel.getName());
			dbIllnessTreatment.setTreatments(createTreatemnts(illnessTreatmentModel.getTreatments()));
		}
		return dbIllnessTreatment;
	}

	public SymptomTreatment createDbSymptomsTreatment(
			TreatmentMainModel symptomTreatmentModel) {
		SymptomTreatment symptomTreatment = new SymptomTreatment();
	   	if(symptomTreatmentModel != null ) {
	   		symptomTreatment.setCode(symptomTreatmentModel.getSymptomID().toUpperCase());
	   		symptomTreatment.setName(symptomTreatmentModel.getName());
	   		symptomTreatment.setTreatments(createTreatemnts(symptomTreatmentModel.getTreatments()));
		}

		return symptomTreatment;
	}
	private Set<Treatment> createTreatemnts(List<TreatmentModel> treatments) {
			Set<Treatment> dbTreatments = null;
		if(treatments != null & ! treatments.isEmpty()) {
			dbTreatments = new HashSet<Treatment>();
			for (int i = 0; i < treatments.size(); i++) {
				TreatmentModel modelTreatModel = treatments.get(i);
				dbTreatments.add(createTreatment(modelTreatModel));
			}
		}

		return dbTreatments;
	}

	private Treatment createTreatment(TreatmentModel modelTreatModel) {
		Treatment dbTreatment = new Treatment();
		dbTreatment.setTypeID(modelTreatModel.getTypeID());
		dbTreatment.setName(modelTreatModel.getName());
		dbTreatment.setGroups(createGroups(modelTreatModel.getGroups()));
		return dbTreatment;
	}

	private Set<TreatmentGroup> createGroups(List <TreatmentGroupModel> groups) {
		Set<TreatmentGroup> dbTreatmentGroups= null;
		if(groups != null && !groups.isEmpty()){
	    dbTreatmentGroups = new HashSet<TreatmentGroup>();
				for (int i = 0; i < groups.size(); i++) {
				TreatmentGroupModel modelTreatmentGroup = groups.get(i);
				int rank = i + 1;
			   dbTreatmentGroups.add(createTreatmentGroups(modelTreatmentGroup,rank));
			}
		}

		return dbTreatmentGroups;
	}

	private TreatmentGroup createTreatmentGroups(
			TreatmentGroupModel modelTreatmentGroup, int rank) {
		TreatmentGroup  treatmentGroup = null;

		if(modelTreatmentGroup != null ) {
			treatmentGroup = new  TreatmentGroup();
			treatmentGroup.setRank(rank);
			treatmentGroup.setName(modelTreatmentGroup.getGroupName().toUpperCase());
		  //   List<String> drungNames = modelTreatmentGroup.getDrugs();
		
		     // this part of code will be remove once sources integrated
		     
		     /*if(drungNames != null &&  !drungNames.isEmpty()) {
		    	treatmentGroup.setDrugs(createDrugDesc(drungNames));
			  }*/
		     
		     // end here
		     
		    List<DrugDescModel> modelDrugs = modelTreatmentGroup.getDrugDescriptions();
		    
		    if(modelDrugs != null && ! modelDrugs.isEmpty()) {
		    	
		    	treatmentGroup.setDrugs(createDrugDescriptions(modelDrugs));
		    }
		     
		     

		  // this part of code will be remove once sources integrated
		     
			/*if( !modelTreatmentGroup.getTypeDescIDs().isEmpty()){
				treatmentGroup.setNonDrungs(createNonDrungsFromTypeDesc(modelTreatmentGroup.getTypeDescIDs()));
			}*/
			// end here
			
			List<NonDrugDescModel> modelNonDrugs = modelTreatmentGroup.getDescriptions();
			  if(modelNonDrugs != null && ! modelNonDrugs.isEmpty()) {
			    	treatmentGroup.setNonDrungs(createNonDrugDescriptions(modelNonDrugs));
			    }
		}


		return treatmentGroup;
	}

	private Set<NonDrug> createNonDrugDescriptions(
			List<NonDrugDescModel> modelNonDrugs) {
		int rank = 1;
		Set<NonDrug> dbTreatmentDesList = new HashSet<NonDrug>();
		for (NonDrugDescModel modelNonDrug : modelNonDrugs) {
			NonDrug dbTreatmentDesc = new NonDrug();
			dbTreatmentDesc.setRank(rank);
			dbTreatmentDesc.setLikelihood(modelNonDrug.getLikelihood());
			dbTreatmentDesc.setTypeDescID(modelNonDrug.getTypeDescID());
			if(modelNonDrug.getNotes() != null) {
			dbTreatmentDesc.setNotes(modelNonDrug.getNotes());
			}
		//	dbTreatmentDesc.setSources(modelNonDrug.getSources());
	
			if(modelNonDrug.getSourceInfo() != null  && ! modelNonDrug.getSourceInfo().isEmpty()  && modelNonDrug.getSourceInfo().size() > 0) {
				dbTreatmentDesc.setSourceInfo(prepareSourceInfo(modelNonDrug.getSourceInfo()));
			}
			
			dbTreatmentDesList.add(dbTreatmentDesc);
			rank = rank +1;
		}
		return dbTreatmentDesList;
	
	}
	
	
	private Set<TreatmentSources> prepareSourceInfo(List<TreatmentSourcesModel> modelSources) {
		Set<TreatmentSources> dbSources = new HashSet<TreatmentSources>();
	//	List<TreatmentSourcesModel> modelSources = symtomDSModel.getSourceInfo();
		if(modelSources != null) {
		for (TreatmentSourcesModel dataStoreSourcesModel : modelSources) {
			TreatmentSources dbTreatmentSource = new TreatmentSources();
			dbTreatmentSource.setAddedBy(dataStoreSourcesModel.getAddedBy());
			dbTreatmentSource.setVerified(dataStoreSourcesModel.getVerified());
			dbTreatmentSource.setSourceID(dataStoreSourcesModel.getSourceID());
			dbTreatmentSource.setSourceInfo(dataStoreSourcesModel.getSourceInfo());
			dbTreatmentSource.setSourceRefDate(new Date().getTime());
			dbSources.add(dbTreatmentSource);
			
		}
		
		}
		return dbSources;
	}
	
	
	

	private Set<Drug> createDrugDescriptions(List<DrugDescModel> modelDrugs) {
		Set<Drug> dbDrugs= null;
		//if(drungNames != null && !drungNames.isEmpty()){
			dbDrugs = new HashSet<Drug>();
			int rank = 1;
			for (DrugDescModel modelDrug : modelDrugs) {
				Drug drug = new Drug();
				drug.setRank(rank);
				drug.setName(modelDrug.getDrugName());
				drug.setLikelihood(modelDrug.getLikelihood());
				//drug.setSources(modelDrug.getSources());
								
		/*	   List<TreatmentPolicyModel> policies = modelDrug.getPolicies();
			
			   if(policies != null  && ! policies.isEmpty()  && policies.size() > 0) {
					drug.setPolicies(preparePolicies(policies));
				}
				*/
							
				
				if(modelDrug.getSourceInfo() != null  && ! modelDrug.getSourceInfo().isEmpty()  && modelDrug.getSourceInfo().size() > 0) {
					drug.setSourceInfo(prepareSourceInfo(modelDrug.getSourceInfo()));
				}
				
			 List<RxNormsModel> rxNormsModel = modelDrug.getRxNorms();
				
				if(rxNormsModel != null  && ! rxNormsModel.isEmpty()  && rxNormsModel.size() > 0) {
					drug.setRxNorms(prepareRxNorms(rxNormsModel));
				}
				
				
				
				dbDrugs.add(drug);
				rank = rank +1;
			}
			
			
	//	}

		return dbDrugs;
		}

	private Set<TreatmentPolicy> preparePolicies(
			List<TreatmentPolicyModel> policies) {
		
		Set<TreatmentPolicy> dbPolices = new HashSet<TreatmentPolicy>();
		
		for (TreatmentPolicyModel treatmentPolicyModel : policies) {
			TreatmentPolicy dbPolicy = new TreatmentPolicy();
		
			if(treatmentPolicyModel.getAction() !=null ) {
			dbPolicy.setAction(treatmentPolicyModel.getAction().getText());
			}
			if(treatmentPolicyModel.getTarget() != null) {
			dbPolicy.setTarget(treatmentPolicyModel.getTarget().getText());
			}
			
			if(treatmentPolicyModel.getTargetCompare() != null) {
			dbPolicy.setTargetCompare(treatmentPolicyModel.getTargetCompare().getText());
			}
			
			if(treatmentPolicyModel.getTargetOperator() != null) {
			dbPolicy.setTargetOperator(treatmentPolicyModel.getTargetOperator().getText());
			}
		
			dbPolicy.setPolicyID(treatmentPolicyModel.getPolicyID());
			dbPolicy.setName(treatmentPolicyModel.getName());
			dbPolicy.setTargetDetail(treatmentPolicyModel.getTargetDetail());
				dbPolices.add(dbPolicy);
		}
	
		
		return dbPolices;
	}

	private Set<DrugDosage> prepareDosageInfo(List<DrugDosageModel> dosageRecommendations) {
		

		Set<DrugDosage> dosages = new HashSet<DrugDosage>();
		for (DrugDosageModel drugDosageModel : dosageRecommendations) {
			
			DrugDosage dosage = new DrugDosage();
			
			dosage.setDaw(drugDosageModel.getDaw());
			dosage.setDirections(drugDosageModel.getDirections());
			dosage.setDispenseForm(drugDosageModel.getDispenseForm());
			dosage.setForm(drugDosageModel.getForm());
			dosage.setFrequency(drugDosageModel.getFrequency());
			dosage.setMeasurement(drugDosageModel.getMeasurement());
			dosage.setPrn(drugDosageModel.getPrn());
			dosage.setQuantity(drugDosageModel.getQuantity());
			dosage.setRoute(drugDosageModel.getRoute());
			dosage.setStrength(drugDosageModel.getStrength());
			
			dosages.add(dosage);
			
		}
		
		
		
	
		
		
		return dosages;
	
	}

	private Set<RxNorms> prepareRxNorms(List<RxNormsModel> rxNormsModel) {
		Set<RxNorms> dbRxNorms = new HashSet<RxNorms>();
	
		if(rxNormsModel != null) {
		for (RxNormsModel rxNorm : rxNormsModel) {
			RxNorms dbRxNorm = new RxNorms();
			dbRxNorm.setRxcui(rxNorm.getRxcui());
			dbRxNorm.setAddedBy(rxNorm.getAddedBy());
			dbRxNorms.add(dbRxNorm);
		}
		
		}
		return dbRxNorms;
	}

	private Set<Drug> createDrugDesc(List<String> drungNames) {
		Set<Drug> dbDrugs= null;
		if(drungNames != null && !drungNames.isEmpty()){
			dbDrugs = new HashSet<Drug>();
			for (int i = 0; i < drungNames.size(); i++) {
				Drug drug = new Drug();
				drug.setRank(i+1);
				drug.setName(drungNames.get(i));
				dbDrugs.add(drug);
			}
		}

		return dbDrugs;
		}
/*
	private Set<NonDrug> createNonDrungsFromTypeDesc(List<Integer> typeDescIDs) {
		Set<NonDrug> dbTreatmentDesList = new HashSet<NonDrug>();
			for (int i = 0; i < typeDescIDs.size(); i++) {
				NonDrug dbTreatmentDesc = new NonDrug();
				dbTreatmentDesc.setRank(i+1);
				dbTreatmentDesc.setTypeDescID(typeDescIDs.get(i));
				dbTreatmentDesList.add(dbTreatmentDesc);

				}

		return dbTreatmentDesList;

	}*/

	/*private Set<Drug> createDrugDesc(List<DrugDescModel> drugs) {
		// TODO Auto-generated method stub
		Set<Drug> dbDrugs= null;
		if(drugs != null && !drugs.isEmpty()){
			dbDrugs = new HashSet<Drug>();
			for (int i = 0; i < drugs.size(); i++) {
			DrugDescModel drugDescModel = drugs.get(i);
			dbDrugs.add(createDrugDesc(drugDescModel));
			}
		}

		return dbDrugs;
	}*/

	/*private Drug createDrugDesc(DrugDescModel drugDescModel) {
		// TODO Auto-generated method stub
		Drug drug = new Drug();
		if(drugDescModel.getPriority() != null ) {
	    drug.setPriority(drugDescModel.getPriority());
	  }
		drug.setRank(drugDescModel.getRank());
		drug.setName(drugDescModel.getDrugName());
		return drug;
	}*/

	/*private Set<NonDrug> createDescriptions(
			List<NonDrugDescModel> modelDescriptions) {
		// TODO Auto-generated method stub
		Set<NonDrug> dbTreatmentDesList= null;
		if(modelDescriptions != null && !modelDescriptions.isEmpty()){
			dbTreatmentDesList = new HashSet<NonDrug>();
			for (int i = 0; i < modelDescriptions.size(); i++) {
			NonDrugDescModel modelDesc = modelDescriptions.get(i);
			dbTreatmentDesList.add(createTreatmentDesc(modelDesc));
			}
		}

		return dbTreatmentDesList;
	}*/

	/*private NonDrug createTreatmentDesc(NonDrugDescModel modelDesc) {
		// TODO Auto-generated method stub
		NonDrug dbTreatmentDesc = new NonDrug();

		if(modelDesc.getPriority() != null ) {
		dbTreatmentDesc.setPriority(modelDesc.getPriority());
		}

		dbTreatmentDesc.setTypeDescID(modelDesc.getTypeDescID());

		dbTreatmentDesc.setName(modelDesc.getName());

		if(modelDesc.getRank() !=null) {
		dbTreatmentDesc.setRank(modelDesc.getRank());
		}

		return dbTreatmentDesc;
	}*/





}
