package com.advinow.mica.mapper;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.advinow.mica.domain.Category;
import com.advinow.mica.domain.DataStoreModifier;
import com.advinow.mica.domain.DataStoreSources;
import com.advinow.mica.domain.Illness;
import com.advinow.mica.domain.Symptom;
import com.advinow.mica.domain.SymptomDataStore;
import com.advinow.mica.model.CategoryModel;
import com.advinow.mica.model.DataStoreSourcesModel;
import com.advinow.mica.model.IllnessModel;
import com.advinow.mica.model.IllnessStatusModel;
import com.advinow.mica.model.ModifierTypeModel;
import com.advinow.mica.model.ScaleModel;
import com.advinow.mica.model.SectionModel;
import com.advinow.mica.model.SymptomDataStoreModel;
import com.advinow.mica.model.SymptomGroups;
import com.advinow.mica.model.Symptoms;
import com.advinow.mica.util.MICAConstants;
import com.advinow.mica.util.MICAUtil;
//import com.advinow.mica.domain.Symptom;
//import com.advinow.mica.repositories.SymptomRepository;

public class IllnessRequestMapper {
	
//	protected Logger logger = LoggerFactory.getLogger(getClass());

	
	/*
	List<Symptoms> illnessSymptoms = null;*/

	public Illness prepareIllness(IllnessModel illnessModel) {
		//	illnessSymptoms = new ArrayList<Symptoms>();
		//Set<Illness> illnessList = new HashSet<Illness>();

		Illness dbIlness = new Illness();
		if(illnessModel.getCode() != null ) {
			dbIlness.setCode(illnessModel.getCode());
		}

		if(illnessModel.getIdIcd10Code() != null ) {
			dbIlness.setIcd10Code(illnessModel.getIdIcd10Code());
		}
		if(illnessModel.getRejectionReason() != null ) {
			dbIlness.setRejectionReason(illnessModel.getRejectionReason());
		}

		if(illnessModel.getCriticality() !=null ) {
			dbIlness.setCriticality(illnessModel.getCriticality());
		}
		
		if(illnessModel.getName() !=null ) {
			dbIlness.setName(illnessModel.getName());
		}
		
		if(illnessModel.getDfstatus() !=null ) {
			dbIlness.setDfstatus(illnessModel.getDfstatus());
		}
		
		if(illnessModel.getPrevalence() !=null ) {
			dbIlness.setPrevalence(illnessModel.getPrevalence());
		}
		
		
		if(illnessModel.getActive() !=null ) {
			dbIlness.setActive(illnessModel.getActive());
		}
		
		
		if(illnessModel.getState() != null ) {
			dbIlness.setState(illnessModel.getState());
			if(illnessModel.getState().equalsIgnoreCase(MICAConstants.APPROVED)){
				dbIlness.setDfstatus(MICAConstants.APPROVED);
			//	dbIlness.setActive(true);
			}
			
			
		}
		if(illnessModel.getPrior() !=null ) {
			dbIlness.setPrior(illnessModel.getPrior());
		}

		if(illnessModel.getGroupsComplete() !=null ) {
			dbIlness.setGroupsComplete(illnessModel.getGroupsComplete());
		}

		if(illnessModel.getCreatedDate() != null){
			dbIlness.setCreatedDate(illnessModel.getCreatedDate());
		} else{
			dbIlness.setCreatedDate(new Date().getTime());
		}


		dbIlness.setModifiedDate(new Date().getTime());

		if(illnessModel.getSource() ==null ) {
			dbIlness.setSource(MICAConstants.SOURCE);
		} else{
			dbIlness.setSource(illnessModel.getSource());
		}

		if(illnessModel.getWeight() !=null ) {
			dbIlness.setWeight(illnessModel.getWeight());
		}

		//  Need to revisit this version later

		if(illnessModel.getVersion() != null ) {			
			dbIlness.setVersion(illnessModel.getVersion());
		} else{
			dbIlness.setVersion(1);
		}

	/*	if(illnessModel.getState().equalsIgnoreCase(MICAConstants.APPROVED)){
			dbIlness.setActive(true);
		}*/

		dbIlness.setCategories(processCategory(illnessModel));

		//illnessList.add(dbIlness);

		// TODO Auto-generated method stub
		return dbIlness;
	}

	private Set<Category> processCategory(
			IllnessModel illnessModel) {
		// TODO Auto-generated method stub

		Set<Category>  categories = new HashSet<Category>();
		List<SymptomGroups> modelSymptomGroups = illnessModel.getSymptomGroups();
		for (int i = 0; i < modelSymptomGroups.size(); i++) {	
			SymptomGroups groupModel = modelSymptomGroups.get(i);
			if(groupModel !=null ) {
				List<CategoryModel> modelCategories = groupModel.getCategories();

				if(modelCategories !=null && ! modelCategories.isEmpty()) {
					for (int j = 0; j < modelCategories.size(); j++) {
						CategoryModel categoryModel = modelCategories.get(j);
						if(categoryModel !=null ) {
							categories.add(prepareCategory(categoryModel));
						}

					}
				}

				List<SectionModel> modelSections =	groupModel.getSections();

				if(modelSections !=null && ! modelSections.isEmpty()) {
					for (int k = 0; k < modelSections.size(); k++) {
						SectionModel modelSection = modelSections.get(k);
						if(modelSection !=null ) {
							List<CategoryModel> modelSecCategories = modelSection.getCategories();
							if(modelSecCategories !=null && ! modelSecCategories.isEmpty()) {
								for (int m = 0; m < modelSecCategories.size(); m++) {
									CategoryModel categorySecModel = modelSecCategories.get(m);
									if(categorySecModel !=null ) {
										categories.add(prepareCategory(categorySecModel));

									}

								}

							}

						}

					}


				}

			}

		}


		return categories;
	}

	private Category prepareCategory(CategoryModel categoryModel) {
		// TODO Auto-generated method stub
		Category dbCategory = new Category();
		if(categoryModel.getCategoryID()  !=null ) {
			dbCategory.setCode(categoryModel.getCategoryID());
		}
		if(categoryModel.getName() != null ) {
			dbCategory.setName(categoryModel.getName());
		}
		if(categoryModel.getSymptoms() !=null && ! categoryModel.getSymptoms().isEmpty()){
			dbCategory.setSymptoms(prepareSymptoms(categoryModel.getSymptoms()));
		} 
		return dbCategory;
	}

	/*private Set<Symptom> processSymptoms(IllnessModel illnessModel) {

		Set<Symptom>  symptoms = new HashSet<Symptom>();

		List<SymptomGroups> modelSymptomGroups = illnessModel.getSymptomGroups();

		for (int i = 0; i < modelSymptomGroups.size(); i++) {	
			SymptomGroups groupModel = modelSymptomGroups.get(i);
			if(groupModel !=null ) {
				List<CategoryModel> modelCategories = groupModel.getCategories();
				if(modelCategories !=null && ! modelCategories.isEmpty()) {
					for (int j = 0; j < modelCategories.size(); j++) {
						CategoryModel categoryModel = modelCategories.get(j);
						if(categoryModel !=null ) {	
							if(categoryModel.getSymptoms() !=null && ! categoryModel.getSymptoms().isEmpty()){
								symptoms.addAll(prepareSymptoms(categoryModel.getSymptoms()));
							} 

						}

					}
				}

			}

		}
		return symptoms;
	}*/

	private Set<Symptom> prepareSymptoms(
			List<Symptoms> symptoms) {
		Set<Symptom>  dbSymptoms = new HashSet<Symptom>();

		if(symptoms != null && !symptoms.isEmpty()){
			for (int i = 0; i < symptoms.size(); i++) {
				Symptoms modelSymptom = symptoms.get(i);
				if(modelSymptom !=null ) {
					Symptom dbSymptom = new Symptom();
					if(modelSymptom.getSymptomID() !=null ) {
						//	illnessSymptoms.add(modelSymptom);
						dbSymptom.setCode(modelSymptom.getSymptomID());

					}

					/*if(modelSymptom.getCriticality() != null ) {
						dbSymptom.setCriticality(modelSymptom.getCriticality());
					}

					if(modelSymptom.getTreatable() != null ) {
						dbSymptom.setTreatable(modelSymptom.getTreatable());
					}*/


					/*if(modelSymptom.getPrior() !=null ) {

						dbSymptom.setPrior(modelSymptom.getPrior());
					}*/
					/*if(modelSymptom.getQuestion() !=null ) {

						dbSymptom.setQuestion(modelSymptom.getQuestion());
					}
					if(modelSymptom.getAntithesis() != null ) {
						dbSymptom.setAntithesis(modelSymptom.getAntithesis());

					}*/

					if(modelSymptom.getBodyParts() != null ) {
						dbSymptom.setBodyParts(modelSymptom.getBodyParts());

					}

			/*		if(modelSymptom.getMinDiagCriteria() !=null ) {

						dbSymptom.setMinDiagCriteria(modelSymptom.getMinDiagCriteria());
					}
*/
/*
					if(modelSymptom.getMedNecessary() !=null ) {

						dbSymptom.setMedNecessary(modelSymptom.getMedNecessary());
					}
*/

					dbSymptom.setSymptomDataStore(prepareDataStore(modelSymptom));			 

					dbSymptoms.add(dbSymptom);

				}
			}

		}

		return dbSymptoms;
	}

	private Set<SymptomDataStore> prepareDataStore(Symptoms modelSymptom) {
		Set<SymptomDataStore> dbsymtomDataStores = new HashSet<SymptomDataStore>();		
		List<SymptomDataStoreModel> modelDataStores = modelSymptom.getRows();		
		if(modelDataStores != null && ! modelDataStores.isEmpty()){			
			for (int j = 0; j < modelDataStores.size(); j++) {
				SymptomDataStoreModel modelDataStore = modelDataStores.get(j);
				dbsymtomDataStores.add(preparesSymptomDSModel(modelDataStore));
			}
		}


		// TODO Auto-generated method stub
		return dbsymtomDataStores;
	}



	private SymptomDataStore preparesSymptomDSModel(SymptomDataStoreModel symtomDSModel) {

		SymptomDataStore dbDataStore = new SymptomDataStore();

		if(symtomDSModel.getBias() != null ){

			dbDataStore.setBias(symtomDSModel.getBias());
		}

		/*if(symtomDSModel.getCode() != null ) {
			dbDataStore.setCode(symtomDSModel.getCode());
		}
		 */

	

		if(symtomDSModel.getLikelihood() != null ) {
			dbDataStore.setLikelihood(MICAUtil.round(symtomDSModel.getLikelihood()/100.0d,2));
			}


		if(symtomDSModel.getMultiplier() != null  && ! symtomDSModel.getMultiplier().isEmpty()  && symtomDSModel.getMultiplier().size() > 0) {
			dbDataStore.setMultiplier(symtomDSModel.getMultiplier());
		}
		
		if(symtomDSModel.getMultiplierCode() != null  && ! symtomDSModel.getMultiplierCode().isEmpty()  && symtomDSModel.getMultiplierCode().size() > 0) {
			dbDataStore.setMultiplierCode(symtomDSModel.getMultiplierCode());
		}

	/*	if(symtomDSModel.getSources() != null  && ! symtomDSModel.getSources().isEmpty()  && symtomDSModel.getSources().size() > 0) {
			dbDataStore.setSources(symtomDSModel.getSources());
		}

		if(symtomDSModel.getSourceType() != null ) {
			dbDataStore.setSourceType(symtomDSModel.getSourceType() );
		}

		if(symtomDSModel.getSoureInfo() != null  && ! symtomDSModel.getSoureInfo().isEmpty()  && symtomDSModel.getSoureInfo().size() > 0) {
			dbDataStore.setSoureInfo(symtomDSModel.getSoureInfo());
		}
*/

		if(symtomDSModel.getSourceInfo() != null  && ! symtomDSModel.getSourceInfo().isEmpty()  && symtomDSModel.getSourceInfo().size() > 0) {
			dbDataStore.setSourceInfo(prepareSourceInfo(symtomDSModel));
		}
		
		
	
	//	dbDataStore.setSourceRefDate(new Date().getTime());


		if(symtomDSModel.getModifierValues() != null && ! symtomDSModel.getModifierValues().isEmpty() && symtomDSModel.getModifierValues().size() >0 ) {
			dbDataStore.setModifierValues(prepareModifierTypes(symtomDSModel));
		}
		
		if(symtomDSModel.getLikelyDiseases() != null  && ! symtomDSModel.getLikelyDiseases().isEmpty()  && symtomDSModel.getLikelyDiseases().size() > 0) {
			dbDataStore.setLikelyDiseases(symtomDSModel.getLikelyDiseases());
		}
		
		
		if (symtomDSModel.getRuleOut() != null) {

			dbDataStore.setRuleOut(symtomDSModel.getRuleOut());
		}
		
			
		if (symtomDSModel.getMust() != null) {

			dbDataStore.setMust(symtomDSModel.getMust());
		}
		
		if(symtomDSModel.getMinDiagCriteria() !=null ) {

			dbDataStore.setMinDiagCriteria(symtomDSModel.getMinDiagCriteria());
		}
		

		if(symtomDSModel.getMedNecessary() !=null ) {

			dbDataStore.setMedNecessary(symtomDSModel.getMedNecessary());
		}



		return dbDataStore;
	}

	private Set<DataStoreSources> prepareSourceInfo(
			SymptomDataStoreModel symtomDSModel) {
		Set<DataStoreSources> dbSources = new HashSet<DataStoreSources>();
		List<DataStoreSourcesModel> modelSources = symtomDSModel.getSourceInfo();
		if(modelSources != null) {
		for (DataStoreSourcesModel dataStoreSourcesModel : modelSources) {
			DataStoreSources dbSource = new DataStoreSources();
			dbSource.setAddedBy(dataStoreSourcesModel.getAddedBy());
			dbSource.setVerified(dataStoreSourcesModel.getVerified());
		//	dbSource.setEnable(dataStoreSourcesModel.getEnable());
			dbSource.setSourceID(dataStoreSourcesModel.getSourceID());
			dbSource.setSourceInfo(dataStoreSourcesModel.getSourceInfo());
			dbSource.setSourceRefDate(new Date().getTime());
			dbSources.add(dbSource);
			
		}
		
		}
		return dbSources;
	}

	private Set<DataStoreModifier> prepareModifierTypes(SymptomDataStoreModel symtomDSModel) {

		Set<DataStoreModifier>  modifiers = new HashSet<DataStoreModifier>();

		List<ModifierTypeModel> modelModifiers = symtomDSModel.getModifierValues();
		if(modelModifiers != null &&  ! modelModifiers.isEmpty()){

			for (int i = 0; i < modelModifiers.size(); i++) {
				ModifierTypeModel modelModifer = modelModifiers.get(i);
				modifiers.add(prepareModifierType(modelModifer));
			}
		}



		// TODO Auto-generated method stub
		return modifiers;
	}

	private DataStoreModifier prepareModifierType(ModifierTypeModel modelModifer) {

		DataStoreModifier  dbmodifier = new DataStoreModifier();

		if(modelModifer != null ) {

			if(modelModifer.getName() != null ) {
				dbmodifier.setName(modelModifer.getName() );
			}

			if(modelModifer.getLikelihood() != null ) {
				dbmodifier.setLikelihood(MICAUtil.round(modelModifer.getLikelihood()/100.0d,2));
			}
			if(modelModifer.getModifierValue() != null ) {
				dbmodifier.setModifierValue(modelModifer.getModifierValue());
			}
			if(modelModifer.getScale() != null ) {
				ScaleModel modelScale = modelModifer.getScale();

				if(modelScale.getTimeFrame() !=null ) {					
					updateTimeFrame(modelScale, dbmodifier);
				} else {
					/*if(modelScale.getUpperTimeLimit() != null ){
						dbmodifier.setStop(modelScale.getUpperTimeLimit());				
					}*/

				/*	if(modelScale.getScaleTimeLimitStart() != null ) {

						dbmodifier.setStart(modelScale.getScaleTimeLimitStart());
					}
*/
			/*		if(modelScale.getSlope() != null ) {

						dbmodifier.setSlope(modelScale.getSlope());
					}

					if(modelScale.getSlopeStrength() != null ) {

						dbmodifier.setSlopeStrength(modelScale.getSlopeStrength());
					}
					
					if(modelScale.getTimeUnit() !=null ) {
						
						dbmodifier.setTimeUnit(modelScale.getTimeUnit());
					}
						if(modelScale.getValue() !=null ) {
						
						dbmodifier.setValue(modelScale.getValue());
					}*/
				}

			}


		}

		return dbmodifier;
	}

	/**
	 * 
	 * <1D should be saved a 1-24 hours 
	 * 2-3D should be saved as 25-72 hours
	 * <1W should be saved as 1-7 days
	 * 2-3W should be saved as 8-21 days
	 * <1M should be saved as 1-31 days
	 * 2-3M should be saved as 32-90 days
	 * Longer should be saved as 3-1200 Months
	 * 
	 * @param modelScale
	 * @param dbmodifier
	 */

	private void updateTimeFrame(ScaleModel modelScale, DataStoreModifier  dbmodifier) {
		
		String timeFrame = modelScale.getTimeFrame();
		
	/*	Integer start =1;
		Integer value = 1200;
		String timeUnits = "Days";
		String timeFrame = modelScale.getTimeFrame();
		if(timeFrame.equalsIgnoreCase(MICAUtil.DAY)) {
			value = 24;
			timeUnits = "Hours";
		} else if(timeFrame.equalsIgnoreCase(MICAUtil.ONE_TO_THREE_DAYS)) {
			start = 25;
			value = 72;
			timeUnits = "Hours";
		} else if(timeFrame.equalsIgnoreCase(MICAUtil.WEEK)) {
			value = 7;
		} else if(timeFrame.equalsIgnoreCase(MICAUtil.ONE_TO_THREE_WEEKS)) {
			start = 8;
			value = 21;
		}else if(timeFrame.equalsIgnoreCase(MICAUtil.MONTH)) {
			value = 31;
		}else if(timeFrame.equalsIgnoreCase(MICAUtil.ONE_TO_THREE_MONTHS)) {
			start = 32;
			value = 90;
		}else if(timeFrame.equalsIgnoreCase(MICAUtil.Longer)) {
			start = 3;
			value = 1200;
			timeUnits = "Months";
		}*/
		dbmodifier.setTimeFrame(timeFrame);
	/*	dbmodifier.setStop(value);				
		dbmodifier.setStart(start);
		dbmodifier.setSlope("Flat");
		dbmodifier.setSlopeStrength(modelScale.getSlopeStrength());
		dbmodifier.setValue(value);
		dbmodifier.setTimeUnit(timeUnits);
*/
	}

	/*
public List<String> getString(List<Double> doubleList){

		List<String> listStr = new ArrayList<String>();
		for (int i = 0; i < doubleList.size(); i++) {

			listStr.add(Double.toString(doubleList.get(i)));
		}

		return listStr;


	}*/

	/*public List<Symptoms> getIllnessSymptoms() {
		return illnessSymptoms;
	}

	public void setIllnessSymptoms(List<Symptoms> illnessSymptoms) {
		this.illnessSymptoms = illnessSymptoms;
	}*/

	public Illness cloneIllness(Illness dbIllness,
			IllnessStatusModel illnessStatusModel) {
		Illness targetIllness = new Illness();

		if (dbIllness.getName() != null) {
			targetIllness.setName(dbIllness.getName());
		}
		if (dbIllness.getIcd10Code() != null) {

			targetIllness.setIcd10Code(dbIllness.getIcd10Code());
		}

		if (dbIllness.getCriticality() != null) {
			targetIllness.setCriticality(dbIllness.getCriticality());
		}


		if (dbIllness.getGroupsComplete() != null) {
			targetIllness.setGroupsComplete(dbIllness.getGroupsComplete());
		}

		if (dbIllness.getRejectionReason() != null) {

			targetIllness.setRejectionReason(dbIllness.getRejectionReason());
		}

		if (dbIllness.getState() != null) {
			targetIllness.setState(dbIllness.getState());
		}

		if (dbIllness.getPrior() != null) {
			targetIllness.setPrior(dbIllness.getPrior());
		}

		if(illnessStatusModel.getToState() != null){
			targetIllness.setState(illnessStatusModel.getToState());
		}

		if(dbIllness.getSource() != null) {
			targetIllness.setSource(dbIllness.getSource());
		} else {
			targetIllness.setSource(MICAConstants.SOURCE);
		}

		if(dbIllness.getCreatedDate() != null) {
			targetIllness.setCreatedDate(dbIllness.getCreatedDate() );
		}

		if (dbIllness.getModifiedDate() != null ) {

			targetIllness.setModifiedDate(dbIllness.getModifiedDate());
		}

		if(dbIllness.getVersion() != null ) {
			targetIllness.setVersion(dbIllness.getVersion());
		}

		if(dbIllness.getWeight() != null ) {
			targetIllness.setWeight(dbIllness.getWeight());
		}


		targetIllness.setCategories(cloneCategories(dbIllness.getCategories()));

		return targetIllness;
	}

	private Set<Category> cloneCategories(Set<Category> categories) {
		Set<Category> cloeCategories = new HashSet<Category>();
		Iterator<Category> dbCategoryItr = categories.iterator();
		while (dbCategoryItr.hasNext()) {
			Category dbCategory = dbCategoryItr.next();
			cloeCategories.add(closeCategory(dbCategory));
		}
		return cloeCategories;
	}

	private Category closeCategory(Category dbCategory) {
		// TODO Auto-generated method stub
		Category cloneCategory = new Category();
		if(dbCategory.getCode() !=null ) {
			cloneCategory.setCode(dbCategory.getCode());
		}

		if(dbCategory.getName() != null ) {

			cloneCategory.setName(dbCategory.getName());
		}

		if(dbCategory.getSymptoms() !=null && ! dbCategory.getSymptoms().isEmpty()){
			cloneCategory.setSymptoms(cloneSymptoms(dbCategory.getSymptoms()));
		} 


		return cloneCategory;
	}


	private Set<Symptom> cloneSymptoms(Set<Symptom>  symptoms) {
		Set<Symptom>  cloneDBSymptoms = new HashSet<Symptom>();

		Iterator<Symptom> dbSymptoms = symptoms.iterator();
		while (dbSymptoms.hasNext()) {
			Symptom dbSymptom = dbSymptoms.next();
			if(dbSymptom !=null ) {
				Symptom clonedbSymptom = new Symptom();
				if(dbSymptom.getCode() !=null ) {

					clonedbSymptom.setCode(dbSymptom.getCode());

				}

				/*if(dbSymptom.getCriticality() != null ) {
						clonedbSymptom.setCriticality(dbSymptom.getCriticality());
					}

					if(dbSymptom.getTreatable() != null ) {
						clonedbSymptom.setTreatable(dbSymptom.getTreatable());
					}*/

			/*	if(dbSymptom.getPrior() !=null ) {

					clonedbSymptom.setPrior(dbSymptom.getPrior());
				}*/

				/*if(dbSymptom.getQuestion() !=null ) {

						clonedbSymptom.setQuestion(dbSymptom.getQuestion());
					}
					if(dbSymptom.getAntithesis() != null ) {
						clonedbSymptom.setAntithesis(dbSymptom.getAntithesis());

					}*/

				if(dbSymptom.getBodyParts() != null ) {
					clonedbSymptom.setBodyParts(dbSymptom.getBodyParts());

				}

				clonedbSymptom.setSymptomDataStore(cloneDataStore(dbSymptom));			 

				cloneDBSymptoms.add(clonedbSymptom);

			}
		}
		return cloneDBSymptoms;

	}

	private Set<SymptomDataStore> cloneDataStore(Symptom dbSymptom) {
		Set<SymptomDataStore> clonedbsymtomDataStores = new HashSet<SymptomDataStore>();		
		Set<SymptomDataStore> dbSymptomStores = dbSymptom.getSymptomDataStore();
		if (dbSymptomStores != null && !dbSymptomStores.isEmpty()) {
			Iterator<SymptomDataStore> dbSymptomStoreItr = dbSymptomStores
					.iterator();
			while (dbSymptomStoreItr.hasNext()) {
				SymptomDataStore dbSymptomStore = dbSymptomStoreItr.next();
				if(dbSymptomStore !=null ) {
					clonedbsymtomDataStores.add(cloneSymptomDSModel(dbSymptomStore));
				}
			}
		}


		// TODO Auto-generated method stub
		return clonedbsymtomDataStores;

	}

	private SymptomDataStore cloneSymptomDSModel(SymptomDataStore dbSymptomStore) {

		SymptomDataStore clonbDataStore = new SymptomDataStore();

		if(dbSymptomStore.getBias() != null ){

			clonbDataStore.setBias(dbSymptomStore.getBias());
		}

		if(dbSymptomStore.getLikelihood() != null ) {
			clonbDataStore.setLikelihood(dbSymptomStore.getLikelihood());
		}

		if(dbSymptomStore.getMultiplier() != null && ! dbSymptomStore.getMultiplier().isEmpty() ) {
			clonbDataStore.setMultiplier(dbSymptomStore.getMultiplier());
		}




		if(dbSymptomStore.getModifierValues() != null ) {
			clonbDataStore.setModifierValues(cloneModifierTypes(dbSymptomStore));
		}

		return clonbDataStore;
	}

	private Set<DataStoreModifier> cloneModifierTypes(
			SymptomDataStore dbSymptomStore) {
		Set<DataStoreModifier>  cloneModifiers = new HashSet<DataStoreModifier>();
		Set<DataStoreModifier> dbSymptomStores = dbSymptomStore.getModifierValues();
		Iterator<DataStoreModifier> dbmodifierItr = dbSymptomStores.iterator();
		while (dbmodifierItr.hasNext()) {
			DataStoreModifier modifierType = dbmodifierItr.next();
			if(modifierType != null ) {
				cloneModifiers.add(cloneModifierType(modifierType));
			}
		}
		return dbSymptomStores;

	}

	private DataStoreModifier cloneModifierType(DataStoreModifier modelModifer) {

		DataStoreModifier  cloneDBmodifier = new DataStoreModifier();

		if(modelModifer != null ) {

			if(modelModifer.getName() != null ) {
				cloneDBmodifier.setName(modelModifer.getName() );
			}

			if(modelModifer.getLikelihood() != null ) {
				//cloneDBmodifier.setLikelihood(modelModifer.getLikelihood()/100f);
				cloneDBmodifier.setLikelihood(modelModifer.getLikelihood());
			}
			if(modelModifer.getModifierValue() != null ) {
				cloneDBmodifier.setModifierValue(modelModifer.getModifierValue());
			}

		/*	if (modelModifer.getStop() != null) {
				cloneDBmodifier.setStop(modelModifer.getStop());
			}
			if (modelModifer.getStart() != null) {
				cloneDBmodifier.setStart(modelModifer.getStart());
			}
			if (modelModifer.getSlope() != null) {
				cloneDBmodifier.setSlope(modelModifer.getSlope());
			}
			if (modelModifer.getSlopeStrength() != null) {
				cloneDBmodifier.setSlopeStrength(modelModifer.getSlopeStrength());
			}

			if (modelModifer.getTimeUnit() != null) {
				cloneDBmodifier.setTimeUnit(modelModifer.getTimeUnit());
			}

			if (modelModifer.getValue() != null) {
				cloneDBmodifier.setValue(modelModifer.getValue());
			}*/

			if(modelModifer.getTimeFrame() !=null ) {

				cloneDBmodifier.setTimeFrame(modelModifer.getTimeFrame());
			}

		}
		return cloneDBmodifier;


	}


}


