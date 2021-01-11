package com.advinow.mica.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.advinow.mica.domain.Category;
import com.advinow.mica.domain.DataStoreModifier;
import com.advinow.mica.domain.DataStoreSources;
import com.advinow.mica.domain.Illness;
import com.advinow.mica.domain.Section;
import com.advinow.mica.domain.Symptom;
import com.advinow.mica.domain.SymptomCategory;
import com.advinow.mica.domain.SymptomDataStore;
import com.advinow.mica.domain.SymptomGroup;
import com.advinow.mica.domain.User;
import com.advinow.mica.model.CategoryModel;
import com.advinow.mica.model.DataStoreSourcesModel;
import com.advinow.mica.model.ICD10CodeModel;
import com.advinow.mica.model.ICD10CodesModel;
import com.advinow.mica.model.IllnessModel;
import com.advinow.mica.model.ModifierTypeModel;
import com.advinow.mica.model.ScaleModel;
import com.advinow.mica.model.SectionModel;
import com.advinow.mica.model.SymptomDataStoreModel;
import com.advinow.mica.model.SymptomGroups;
import com.advinow.mica.model.Symptoms;
import com.advinow.mica.model.UserICD10CodeModel;
import com.advinow.mica.util.MICAUtil;


public class IllnessResponseMapper {
	
//	protected Logger logger = LoggerFactory.getLogger(getClass());
	Set<Section> mapperSections = null;
		

	public IllnessModel createIllnessModel(Illness dbIllness) {
		
		IllnessModel illnessModel = new IllnessModel();

		if (dbIllness.getName() != null) {
			illnessModel.setName(dbIllness.getName());
		}
		if (dbIllness.getIcd10Code() != null) {

			illnessModel.setIdIcd10Code(dbIllness.getIcd10Code());
		}

		if (dbIllness.getCriticality() != null) {
			illnessModel.setCriticality(dbIllness.getCriticality());
		}
		
		illnessModel.setUpdatedDate(dbIllness.getModifiedDate());
		
		illnessModel.setSource(dbIllness.getSource());
		
		illnessModel.setWeight(dbIllness.getWeight());
		
		illnessModel.setVersion(dbIllness.getVersion());
				
		
		if (dbIllness.getGroupsComplete() != null) {
			illnessModel.setGroupsComplete(dbIllness.getGroupsComplete());
		}
		
		if (dbIllness.getRejectionReason() != null) {

			illnessModel.setRejectionReason(dbIllness.getRejectionReason());
		}

		if (dbIllness.getState() != null) {
			illnessModel.setState(dbIllness.getState());
		}

		if (dbIllness.getPrior() != null) {
			illnessModel.setPrior(dbIllness.getPrior());
		}

		
		if (dbIllness.getPrevalence() != null) {
			illnessModel.setPrevalence(dbIllness.getPrevalence());
		}

		
		
		if (dbIllness.getCategories() != null
				&& !dbIllness.getCategories().isEmpty()) {

			illnessModel.setSymptoms(prepareSymtopmsByCategory(dbIllness
					.getCategories()));
		}

		return illnessModel;
	}

	private List<Symptoms> prepareSymtopmsByCategory(Set<Category> categories) {
		List<Symptoms> symptoms = new ArrayList<Symptoms>();
		Iterator<Category> dbCategoryItr = categories.iterator();
		while (dbCategoryItr.hasNext()) {
			Category dbCategory = dbCategoryItr.next();
			if (dbCategory != null && dbCategory.getSymptoms() != null
					&& !dbCategory.getSymptoms().isEmpty()) {
				symptoms.addAll(getAllSymptoms(dbCategory.getSymptoms()));
			}
		}

		return symptoms;
	}

	public IllnessModel createIllnessModelByGroup(Illness dbIllness,
			Iterable<SymptomGroup> groups,Set<Section> dbSections) {
		IllnessModel illnessModel = new IllnessModel();
		// secRepository = sectionRepository;
		mapperSections = dbSections;
		// dataStoreModifierRepository = dsModifierRepository;

		if (dbIllness.getName() != null) {
			illnessModel.setName(dbIllness.getName());
		}
		if (dbIllness.getIcd10Code() != null) {

			illnessModel.setIdIcd10Code(dbIllness.getIcd10Code());
			
		}
		
		illnessModel.setUpdatedDate(dbIllness.getModifiedDate());
		
		if(dbIllness.getCreatedDate() != null ) {
		illnessModel.setCreatedDate(dbIllness.getCreatedDate());
		}
		
		illnessModel.setSource(dbIllness.getSource());
		illnessModel.setWeight(dbIllness.getWeight());
		if(dbIllness.getVersion() != null ) {
		illnessModel.setVersion(dbIllness.getVersion());
		}
		if (dbIllness.getCriticality() != null) {
			illnessModel.setCriticality(dbIllness.getCriticality());
		}

		if (dbIllness.getGroupsComplete() != null) {
			illnessModel.setGroupsComplete(dbIllness.getGroupsComplete());
		}

		if (dbIllness.getState() != null) {
			illnessModel.setState(dbIllness.getState());
		}
		

		if (dbIllness.getRejectionReason() != null) {

			illnessModel.setRejectionReason(dbIllness.getRejectionReason());
		}

		if (dbIllness.getPrior() != null) {
			illnessModel.setPrior(dbIllness.getPrior());
		}

	
		
		illnessModel.setSymptomGroups(prepareSymptomGroups(dbIllness, groups));

		return illnessModel;
	}

	private List<SymptomGroups> prepareSymptomGroups(Illness dbIllness,
			Iterable<SymptomGroup> groups) {

		List<SymptomGroups> modelGroups = new ArrayList<SymptomGroups>();
		Iterator<SymptomGroup> dbGroupsitr = groups.iterator();
		while (dbGroupsitr.hasNext()) {
			SymptomGroup dbGroup = dbGroupsitr.next();
			//if (! dbGroup.getName().equalsIgnoreCase("NLP")) {
			modelGroups.add(prepareGroup(dbIllness, dbGroup));
		//	}

		}
		return modelGroups;
	}

	private SymptomGroups prepareGroup(Illness dbIllness, SymptomGroup dbGroup) {

		SymptomGroups modelGroup = new SymptomGroups();
		if (dbGroup.getGroupID() != null) {
			modelGroup.setGroupID(dbGroup.getGroupID());
		}
		if (dbGroup.getName() != null) {
			modelGroup.setName(dbGroup.getName());
		}

	//	List<CategoryModel> categories = new ArrayList<CategoryModel>();
		Set<SymptomCategory> dbCategories = dbGroup.getCategories();
		if (dbCategories != null && ! dbCategories.isEmpty()) {/*
			Iterator<SymptomCategory> dbCategoryItr = dbCategories.iterator();
		
			while (dbCategoryItr.hasNext()) {
				SymptomCategory modelCategory  = dbCategoryItr.next();
		     Category illnessCategory = categoryCheck(dbIllness, modelCategory);
			if (illnessCategory != null) {
				categories.add(prepareCategoryModel(illnessCategory));

			}
			}
		*/
			modelGroup.setCategories(prepareCategoryModel(dbCategories,dbIllness));
		}

		
		Set<Section> dbsections = dbGroup.getSections();
		if(dbsections != null  &&  !dbsections.isEmpty() ){
		//	group.setSections(createSections(sections,dataStoreRefList));
			List<SectionModel> modelSections = new ArrayList<SectionModel>();
//			logger.info("Size of sections " + dbsections.size());
			Iterator<Section> sectionDBItr = dbsections.iterator(); 
			while(sectionDBItr.hasNext()) {
				Section dbSection = sectionDBItr.next();
				SectionModel sectionModel = new SectionModel();
	//			logger.info("Section ID " + dbSection.getCode());
				sectionModel.setSectionID(dbSection.getCode());
				//Set<SymptomCategory> sectionCategories =	secRepository.getSymptomCategoryFromIDs(dbSection.getId());
				Section section = mapperSections.stream().filter(s->s.getId().longValue()==dbSection.getId().longValue()).findAny().orElse(null);
				Set<SymptomCategory> sectionCategories = null;
				if(section != null ) {
					sectionCategories =			section.getSymptomCategories();
				}
				//Set<SymptomCategory> sectionCategories = dbSection.getSymptomCategories();
//				logger.info("Category size " + sectionCategories.size());
				if(sectionCategories != null && ! sectionCategories.isEmpty()) {
				sectionModel.setCategories(prepareCategoryModel(sectionCategories,dbIllness));
				modelSections.add(sectionModel);							
			}
				
			}
				
			
			modelGroup.setSections(modelSections);

		}
		
		
		

		return modelGroup;
	}

	private List<CategoryModel> prepareCategoryModel(
			Set<SymptomCategory> dbCategories, Illness dbIllness) {
		
		List<CategoryModel> categories = new ArrayList<CategoryModel>();
		
		Iterator<SymptomCategory> dbCategoryItr = dbCategories.iterator();
		
		while (dbCategoryItr.hasNext()) {
			SymptomCategory modelCategory  = dbCategoryItr.next();
			
	     Category illnessCategory = categoryCheck(dbIllness, modelCategory);
		if (illnessCategory != null) {
			categories.add(prepareCategoryModel(illnessCategory));

		} else{
			categories.add(prepareSymptomCategoryModel(modelCategory));
		}
		
		}
		return categories;
	}

	private CategoryModel prepareSymptomCategoryModel(
			SymptomCategory modelCategory) {
		CategoryModel CategoryModel = new CategoryModel();
		
		if(modelCategory.getCode() != null ) {
			CategoryModel.setCategoryID(modelCategory.getCode());
		}
		
		return CategoryModel;
	}

	private CategoryModel prepareCategoryModel(Category illnessCategory) {
		CategoryModel categoryModel = new CategoryModel();

		if (illnessCategory.getCode() != null) {
			categoryModel.setCategoryID(illnessCategory.getCode());
		}
		if (illnessCategory.getName() != null) {
			categoryModel.setName(illnessCategory.getName());
		}
		Set<Symptom> dbSymtoms = illnessCategory.getSymptoms();
		
		if(dbSymtoms != null&& !dbSymtoms.isEmpty()) {
			categoryModel.setSymptoms(getAllSymptoms(dbSymtoms));
		}
	
		return categoryModel;
	}

	private Category categoryCheck(Illness dbIllness,
			SymptomCategory modelCategory) {
		Set<Category> dbCategories = dbIllness.getCategories();
		if (dbCategories != null && !dbCategories.isEmpty()) {
			Iterator<Category> dbCategoryItr = dbCategories.iterator();
			while (dbCategoryItr.hasNext()) {
				Category dbCategory = dbCategoryItr.next();
				if (dbCategory.getCode().equalsIgnoreCase(modelCategory.getCode())) {
						return dbCategory;
				} 
				}

			}

		
		return null;

	}

	private List<Symptoms> getAllSymptoms(Set<Symptom> dbSymtoms) {
		List<Symptoms> symptoms = new ArrayList<Symptoms>();
		Iterator<Symptom> dbSymptoms = dbSymtoms.iterator();
		while (dbSymptoms.hasNext()) {
			Symptom dbSymptom = dbSymptoms.next();
			if(dbSymptom != null ) {
			Symptoms symptomsModel = prepareSymtomModel(dbSymptom);
			if (symptomsModel != null) {
				symptoms.add(symptomsModel);
			}
			
		}
		}
		return symptoms;
	}

	private Symptoms prepareSymtomModel(Symptom dbSymptom) {
		Symptoms symptomsModel = new Symptoms();
/*
		if (dbSymptom.getCriticality() != null) {
			symptomsModel.setCriticality(dbSymptom.getCriticality());
		}*/

		if (dbSymptom.getCode() != null) {

			symptomsModel.setSymptomID(dbSymptom.getCode());
			}

		if (dbSymptom.getName() != null) {
			symptomsModel.setName(dbSymptom.getName());

		}
	/*	if (dbSymptom.getPrior() != null) {
			symptomsModel.setPrior(dbSymptom.getPrior());

		}*/

		/*if (dbSymptom.getQuestion() != null) {
			symptomsModel.setQuestion(dbSymptom.getQuestion());
		}

		if (dbSymptom.getAntithesis() != null) {
			symptomsModel.setAntithesis(dbSymptom.getAntithesis());
		}
	
		
		if (dbSymptom.getTreatable() != null) {
			symptomsModel.setTreatable(dbSymptom.getTreatable());
		}*/
		
				
		
		if (dbSymptom.getBodyParts() != null) {
			symptomsModel.setBodyParts(dbSymptom.getBodyParts());
		}
		
/*		if (dbSymptom.getMinDiagCriteria() != null) {
			symptomsModel.setMinDiagCriteria(dbSymptom.getMinDiagCriteria());
		}*/
	     
		//symptomsModel.setMedNecessary(dbSymptom.getMedNecessary());
	
		symptomsModel.setRows(prepareDataStores(dbSymptom));
			return symptomsModel;
	}

	private List<SymptomDataStoreModel> prepareDataStores(Symptom dbSymptom) {
		List<SymptomDataStoreModel> modifiers = new ArrayList<SymptomDataStoreModel>();
		Set<SymptomDataStore> dbSymptomStores = dbSymptom.getSymptomDataStore();
		if (dbSymptomStores != null && !dbSymptomStores.isEmpty()) {
			Iterator<SymptomDataStore> dbSymptomStoreItr = dbSymptomStores
					.iterator();
			while (dbSymptomStoreItr.hasNext()) {
				SymptomDataStore dbSymptomStore = dbSymptomStoreItr.next();
				if(dbSymptomStore !=null ) {
				modifiers.add(preparesSymptomDSModel(dbSymptomStore));
				}
			}
		}
		return modifiers;

	}

	private SymptomDataStoreModel preparesSymptomDSModel(
			SymptomDataStore dbDataStore) {
		SymptomDataStoreModel symptomModel = new SymptomDataStoreModel();
	
		if (dbDataStore.getBias() != null) {

			symptomModel.setBias(dbDataStore.getBias());
		}

		if (dbDataStore.getCode() != null) {
			symptomModel.setCode(dbDataStore.getCode());
			
		}

		if (dbDataStore.getName() != null) {
			symptomModel.setName(dbDataStore.getName());
		}
	
		Set<DataStoreModifier> dbmodifierTypes = dbDataStore.getModifierValues();
		/*if(dbmodifierTypes == null ) {
		
	     dbmodifierTypes =	dataStoreModifierRepository.getAllModifiersForDS(dbDataStore.getId());
		
		}*/
		if (dbmodifierTypes != null && ! dbmodifierTypes .isEmpty() && dbmodifierTypes.size() >0) {
			symptomModel.setModifierValues(prepareModifierTypes(dbmodifierTypes));
		} 
		
		if(dbmodifierTypes == null && dbDataStore.getLikelihood() == null) {
			symptomModel.setLikelihood(0);
		}
		
		
		if (dbDataStore.getLikelihood() != null) {
			
		Long likelihoods =	Math.round(MICAUtil.round( dbDataStore.getLikelihood() ,2)*100);
				symptomModel.setLikelihood(likelihoods.intValue());
		}
			
		 
		List<String> multiplier = dbDataStore.getMultiplier();		
		
		if (multiplier != null && ! multiplier.isEmpty() && multiplier.size() > 0) {
		     	 symptomModel.setMultiplier(multiplier);
		}
		
      List<String> multiplierCodes = dbDataStore.getMultiplierCode();		
		
		if (multiplierCodes != null && ! multiplierCodes.isEmpty() && multiplierCodes.size() > 0) {
		     	 symptomModel.setMultiplierCode(multiplierCodes);
		}
		
		
	 /*   Set<String> sources = dbDataStore.getSoureInfo();	
	
	    if (sources != null && ! sources.isEmpty() && sources.size() > 0) {
		     	 symptomModel.setSoureInfo(sources);
		}
	    
	    if (dbDataStore.getSourceRefDate() != null) {
			symptomModel.setSourceRefDate(dbDataStore.getSourceRefDate());
		}
	    
	    if (dbDataStore.getSourceType() != null) {
			symptomModel.setSourceType(dbDataStore.getSourceType());
		}
	    */
	    
	    symptomModel.setSourceInfo(prepareModelSources(dbDataStore));
	    
	    
	   /* 
	    Set<Integer> symptomSources = dbDataStore.getSources();
	    
	    if (symptomSources != null && ! symptomSources.isEmpty() && symptomSources.size() > 0) {
	     	 symptomModel.setSources(symptomSources);
	}*/
   
		
		Set<String> diseases = dbDataStore.getLikelyDiseases();		
		
		if (diseases != null && ! diseases.isEmpty() && diseases.size() > 0) {
		     	 symptomModel.setLikelyDiseases(diseases);
		}
		
					
		symptomModel.setRuleOut(dbDataStore.getRuleOut());	
		symptomModel.setMedNecessary(dbDataStore.getMedNecessary());
		symptomModel.setMust(dbDataStore.getMust());
		symptomModel.setMinDiagCriteria(dbDataStore.getMinDiagCriteria());

	    
	    
		return symptomModel;
	}

/*	private List<DataStoreSourcesModel> prepareModelSources(
			SymptomDataStore dbDataStore) {
		// TODO Auto-generated method stub
		List<DataStoreSourcesModel>  modelSources = new ArrayList<DataStoreSourcesModel>();
		Set<DataStoreSources> dbSources = dbDataStore.getSourceInfo();
		if(dbSources != null) {
		for (DataStoreSources dbSource : dbSources) {
			DataStoreSourcesModel dataStoreSourcesModel = new DataStoreSourcesModel();
			dataStoreSourcesModel.setAddedBy(dbSource.getAddedBy());
			dataStoreSourcesModel.setEnable(dbSource.getEnable());
			dataStoreSourcesModel.setVerified(dbSource.getVerified());
			dataStoreSourcesModel.setSourceID(dbSource.getSourceID());
			dataStoreSourcesModel.setSourceInfo(dbSource.getSourceInfo());
			dataStoreSourcesModel.setSourceRefDate(new Date().getTime());
			modelSources.add(dataStoreSourcesModel);
			
		}
		
		}
		
		return modelSources;
	}
*/
	
	
	private List<DataStoreSourcesModel> prepareModelSources(
			SymptomDataStore dbDataStore) {
		// TODO Auto-generated method stub
		List<DataStoreSourcesModel>  modelSources = new ArrayList<DataStoreSourcesModel>();
		Set<DataStoreSources> dbSources = dbDataStore.getSourceInfo();
		if(dbSources != null) {
			List<DataStoreSources> finalSources = adjustSources(dbSources);
			for (DataStoreSources dbSource : finalSources) {
			DataStoreSourcesModel dataStoreSourcesModel = new DataStoreSourcesModel();
			dataStoreSourcesModel.setAddedBy(dbSource.getAddedBy());
		//	dataStoreSourcesModel.setEnable(dbSource.getEnable());
			dataStoreSourcesModel.setVerified(dbSource.getVerified());
			dataStoreSourcesModel.setSourceID(dbSource.getSourceID());
			dataStoreSourcesModel.setSourceInfo(dbSource.getSourceInfo());
			dataStoreSourcesModel.setSourceRefDate(new Date().getTime());
			modelSources.add(dataStoreSourcesModel);
		}
		
		}
		
		return modelSources;
	}

	
	private List<DataStoreSources> adjustSources(Set<DataStoreSources> dbSources) {
	// TODO Auto-generated method stub
		List<DataStoreSources> sources =new ArrayList<DataStoreSources>();
		
		if(dbSources != null && ! dbSources.isEmpty()) {
			sources.addAll(dbSources.stream().filter(s->s.getAddedBy().equalsIgnoreCase("Doctor") && s.getVerified()).collect(Collectors.toList()));
			sources.addAll(dbSources.stream().filter(s->s.getAddedBy().equalsIgnoreCase("NLP") && s.getVerified()).collect(Collectors.toList()));
		    sources.addAll(dbSources.stream().filter(s->s.getAddedBy().equalsIgnoreCase("Doctor") && !s.getVerified()).collect(Collectors.toList()));
			sources.addAll(dbSources.stream().filter(s->s.getAddedBy().equalsIgnoreCase("NLP") && !s.getVerified()).collect(Collectors.toList()));
				
		}
		return sources;
	
}
	
	
/*	private List<DataStoreSources> adjustSources(Set<DataStoreSources> dbSources) {
	// TODO Auto-generated method stub
		List<DataStoreSources> sources =new ArrayList<DataStoreSources>();
		
		if(dbSources != null && ! dbSources.isEmpty()) {
	
			sources.addAll(dbSources.stream().filter(s->s.getAddedBy().equalsIgnoreCase("Doctors") && s.getVerified()).collect(Collectors.toList()));
		
			if(sources.isEmpty() || sources.size() < MICAConstants.sourceSize ) {
				sources.addAll(dbSources.stream().filter(s->s.getAddedBy().equalsIgnoreCase("NLP") && s.getVerified()).limit(5).collect(Collectors.toList()));
			}
			if(sources.isEmpty() || sources.size() < MICAConstants.sourceSize ) {
				sources.addAll(dbSources.stream().filter(s->s.getAddedBy().equalsIgnoreCase("NLP") && !s.getVerified()).limit(5).collect(Collectors.toList()));
			} 
			if(sources.isEmpty() || sources.size() < MICAConstants.sourceSize ) {
			 sources.addAll(dbSources.stream().filter(s->s.getAddedBy().equalsIgnoreCase("Doctors") && !s.getVerified()).limit(5).collect(Collectors.toList()));
			} 
		
			if(sources.isEmpty() || sources.size() < MICAConstants.sourceSize ) {
				sources.addAll( dbSources.stream().limit(5).collect(Collectors.toList()));
			}
	
			if(sources.size() > MICAConstants.sourceSize) {
				sources  = sources.stream().limit(5).collect(Collectors.toList());
			}
			
		}
		return sources;
	
}*/

	private List<ModifierTypeModel> prepareModifierTypes(
			Set<DataStoreModifier> dbmodifierTypes ) {

		List<ModifierTypeModel> modifierModelList = new ArrayList<ModifierTypeModel>();
		Iterator<DataStoreModifier> dbmodifierItr = dbmodifierTypes.iterator();
			while (dbmodifierItr.hasNext()) {
				DataStoreModifier modifierType = dbmodifierItr.next();
				if(modifierType != null ) {
				modifierModelList.add(prepareModifierType(modifierType));
				}

			}
		//}

		return modifierModelList;
	}

	private ModifierTypeModel prepareModifierType(
			DataStoreModifier dbmodifierType) {
		ModifierTypeModel modelModifierType = new ModifierTypeModel();
		
	    String modifierName = dbmodifierType.getName(); 
		
			if (modifierName != null) {
				modelModifierType.setName(dbmodifierType.getName());
			}

			if (dbmodifierType.getLikelihood() != null) {
				Long likelihoods =	Math.round(MICAUtil.round( dbmodifierType.getLikelihood() ,2)*100);
				modelModifierType.setLikelihood(likelihoods.intValue());
			}
		
			/*if (dbmodifierType.getModifierValue() != null) {
				modelModifierType.setModifierValue(dbmodifierType
						.getModifierValue());
			} 
			
			if(dbmodifierType.getModifierValue() == null &&  modifierName != null && modifierName.equals("Ethnicity")){
				modelModifierType.setModifierValue(" ");
			}*/
			
			if( modifierName != null && (modifierName.equals("Ethnicity") || (modifierName.equals("Recurs"))) ){
				if (dbmodifierType.getModifierValue() != null) {
					modelModifierType.setModifierValue(dbmodifierType
							.getModifierValue());
				}  else{
					modelModifierType.setModifierValue(" ");
				}
				
			}

			if(modifierName != null && ! modifierName.equalsIgnoreCase("Ethnicity") && ! modifierName.equalsIgnoreCase("Recurs")) {
		    	modelModifierType.setScale(prepareScale(dbmodifierType, modifierName ));
			}
		

		return modelModifierType;
	}

	private ScaleModel prepareScale(DataStoreModifier dbmodifier, String modifierName ) {
		ScaleModel modelScale = new ScaleModel();
	/*	if(modifierName.equalsIgnoreCase("Time") ||  modifierName.equalsIgnoreCase("Age") ){
			modelScale.setValue(0);
			modelScale.setScaleTimeLimitStart(0);
			modelScale.setUpperTimeLimit(0);
			modelScale.setTimeUnit("Flat");
			modelScale.setSlope("Years");
		}*/
		
/*		if (dbmodifier.getStop() != null) {
			modelScale.setUpperTimeLimit(dbmodifier.getStop());
		}
		if (dbmodifier.getStart() != null) {
			modelScale.setScaleTimeLimitStart(dbmodifier.getStart());
		}
		if (dbmodifier.getSlope() != null) {
			modelScale.setSlope(dbmodifier.getSlope());
		}
		if (dbmodifier.getSlopeStrength() != null) {
			modelScale.setSlopeStrength(dbmodifier.getSlopeStrength());
		}

		if (dbmodifier.getTimeUnit() != null) {
			modelScale.setTimeUnit(dbmodifier.getTimeUnit());
		}

		if (dbmodifier.getValue() != null) {
			modelScale.setValue(dbmodifier.getValue());
		}*/
		
		if (dbmodifier.getTimeFrame() != null) {
			modelScale.setTimeFrame(dbmodifier.getTimeFrame());
		}

		return modelScale;

	}
	
	public List<Double> getDouble(List<String> strList){
		List<Double> listDouble = new ArrayList<Double>();
		for (int i = 0; i < strList.size(); i++) {
			
			listDouble.add(Double.parseDouble(strList.get(i)));
		}
		return listDouble;
				
	}

	public List<IllnessModel> getIllnessFromItr(Iterable<Illness> illnessList) {		
		List<IllnessModel> illnessModelList = new ArrayList<IllnessModel>();
		if(illnessList != null) {
		Iterator<Illness> dbItr = illnessList.iterator();
		while(dbItr.hasNext()) {
		Illness dbIllness = dbItr.next();
		IllnessModel illnessModel = new IllnessModel();

		if (dbIllness.getName() != null) {
			illnessModel.setName(dbIllness.getName());
		}
		if (dbIllness.getIcd10Code() != null) {

			illnessModel.setIdIcd10Code(dbIllness.getIcd10Code());
		}

		if (dbIllness.getCriticality() != null) {
			illnessModel.setCriticality(dbIllness.getCriticality());
		}

//		if (dbIllness.getGroupsComplete() != null) {
//			illnessModel.setGroupsComplete(dbIllness.getGroupsComplete());
//		}
//		
		/*if (dbIllness.getRejectionReason() != null) {

			illnessModel.setRejectionReason(dbIllness.getRejectionReason());
		}*/

		/*if (dbIllness.getState() != null) {
			illnessModel.setState(dbIllness.getState());
		}
*/
		if (dbIllness.getPrior() != null) {
			illnessModel.setPrior(dbIllness.getPrior());
		}
		illnessModelList.add(illnessModel);
		}
		}
		
		
		// TODO Auto-generated method stub
		return illnessModelList;
	}

	/**
	 * 
	 * @param dbIllnesses
	 * @return
	 */
	public ICD10CodesModel createICD10CodeMapper(Iterable<Illness> dbIllnesses) {
		ICD10CodesModel illnessData = new ICD10CodesModel();
		List <ICD10CodeModel> illnesses = new ArrayList<ICD10CodeModel>();
		
		if(dbIllnesses != null ) {
			Iterator<Illness> illnessItr = dbIllnesses.iterator();
			while(illnessItr.hasNext()) {
			Illness dbIllness = illnessItr.next();
			ICD10CodeModel icd10Model = new ICD10CodeModel();
			icd10Model.setIcd10Code(dbIllness.getIcd10Code());
			icd10Model.setVersion(dbIllness.getVersion());
			icd10Model.setName(dbIllness.getName());
			icd10Model.setPrior(dbIllness.getPrior());
			icd10Model.setCriticality(dbIllness.getCriticality());
			icd10Model.setUpdatedDate(dbIllness.getModifiedDate());
			//icd10Model.setCreateddDate(dbIllness.getCreateddDate());
			icd10Model.setSource(dbIllness.getSource());
			icd10Model.setWeight(dbIllness.getWeight());
			icd10Model.setState(dbIllness.getState());
			illnesses.add(icd10Model);
			}
			
		}
		illnessData.setIllnesses(illnesses);
		return illnessData;
	}

	
	/**
	 * 
	 * @param dbIllnesses
	 * @return
	 */
	public List<UserICD10CodeModel> createICD10CodeMapperWithUser(Set<User> usrInfo) {
		
		List <UserICD10CodeModel> userIcdCodes = new ArrayList<UserICD10CodeModel>(); 
		if(usrInfo != null ) {
		
		 Iterator<User> userItr = usrInfo.iterator();	
		 while(userItr.hasNext()) {
		   UserICD10CodeModel illnessData = new UserICD10CodeModel();
			User user = userItr.next();
			if(user != null) {
			Set<Illness> dbIllnesses = user.getIllnesses();
			Integer userID = user.getUserID();
			illnessData.setUserID(userID);
		
		List <ICD10CodeModel> illnesses = new ArrayList<ICD10CodeModel>();
		if(dbIllnesses != null ) {
			Iterator<Illness> illnessItr = dbIllnesses.iterator();
			while(illnessItr.hasNext()) {
			Illness dbIllness = illnessItr.next();
			ICD10CodeModel icd10Model = new ICD10CodeModel();
			icd10Model.setIcd10Code(dbIllness.getIcd10Code());
			icd10Model.setVersion(dbIllness.getVersion());
			icd10Model.setName(dbIllness.getName());
			icd10Model.setPrior(dbIllness.getPrior());
			icd10Model.setCriticality(dbIllness.getCriticality());
			icd10Model.setUpdatedDate(dbIllness.getModifiedDate());
			//icd10Model.setCreateddDate(dbIllness.getCreateddDate());
			icd10Model.setSource(dbIllness.getSource());
			icd10Model.setWeight(dbIllness.getWeight());
			icd10Model.setState(dbIllness.getState());
			illnesses.add(icd10Model);
			}
			illnessData.setIllnesses(illnesses);
		}
	
		
		}
			 userIcdCodes.add(illnessData);
		}
		
		}
		
		return userIcdCodes;
	}

}
