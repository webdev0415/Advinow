package com.advinow.mica.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.advinow.mica.domain.DataKeys;
import com.advinow.mica.domain.DataStore;
import com.advinow.mica.domain.RCodeDataStore;
import com.advinow.mica.domain.Section;
import com.advinow.mica.domain.SymptomCategory;
import com.advinow.mica.domain.SymptomGroup;
import com.advinow.mica.domain.SymptomTemplate;
import com.advinow.mica.domain.queryresult.GenericQueryResultEntity;
import com.advinow.mica.model.CategoryModel;
import com.advinow.mica.model.DataKeyStore;
import com.advinow.mica.model.SectionModel;
import com.advinow.mica.model.SymptomGroups;
import com.advinow.mica.model.Symptoms;
import com.advinow.mica.model.SymptomsTmplModel;
import com.advinow.mica.model.ValueStore;
import com.advinow.mica.model.enums.TimeType;
import com.advinow.mica.repositories.DataKeysRepository;
import com.advinow.mica.util.MICAConstants;
/**
 * 
 * @author Govinda Reddy
 *
 */
public class SymptomGroupResponseMapper {



	protected Logger logger = LoggerFactory.getLogger(getClass());

	Set<SymptomTemplate> rCodeSymTemplates  = null;		
	Set<DataStore> dataStores = null;
	String lCode = null;
	//Map<Integer, Object> classifications = null;
	List<GenericQueryResultEntity> logicalGroups= null; 
	
	List<GenericQueryResultEntity> deGroups = null;

	/**
	 * Prepares the response massage for the given group name.
	 * 
	 * @param symptomGroup
	 * @param dataKeysRepository
	 * @param rCodeDataKeyRespository
	 * @param dataStoreRepository
	 * @return
	 */
	public SymptomGroups prepareSymptomsGroupByName(SymptomGroup symptomGroup,DataKeysRepository dataKeysRepository,Set<DataStore> dataStores,Set<SymptomTemplate> rCodeSymTemplates,String lCode,String groupID,List<GenericQueryResultEntity> logicalGroups,List<GenericQueryResultEntity> deGroups ) {

		this.rCodeSymTemplates = rCodeSymTemplates;
		this.dataStores = dataStores;
		this.lCode = lCode;
		this.logicalGroups =logicalGroups;
		this.deGroups = deGroups;
     //  this.classifications = classifications; 
		// TODO Auto-generated method stub
		Set<String> dataStoreRefList= new HashSet<String>();
		SymptomGroups group= new SymptomGroups();

		if(MICAConstants.SPANISH.equals(lCode)) {   
			group.setEs_name(symptomGroup.getEs_name());
		} 
		if(symptomGroup.getName() !=null ){
			group.setName(symptomGroup.getName());
		}
		group.setGroupID(symptomGroup.getGroupID());
		group.setUpdatedDate(symptomGroup.getUpdatedDate());

		Set<SymptomCategory> resCategoryList = symptomGroup.getCategories();     	
		if ( resCategoryList != null && ! resCategoryList.isEmpty()  ){
			group.setCategories(createCategories(resCategoryList,dataStoreRefList,groupID));
		}

		Set<Section> sections = symptomGroup.getSections();
		if(sections != null  &&  !sections.isEmpty() ){
			group.setSections(createSections(sections,dataStoreRefList,groupID));
		}

		group.setDataStoreRefTypes(createDataStoreRefTypes(dataStoreRefList,dataKeysRepository));
		return group;
	}

	
	
	private List<SectionModel> createSections(Set<Section> sections,
			Set<String> dataStoreRefList,String groupID) {
		List<SectionModel> modelSections = new ArrayList<SectionModel>();
		Iterator<Section> dbSectionItr = sections.iterator();
		while(dbSectionItr.hasNext()){
			Section dbSection = dbSectionItr.next();
			SectionModel sectionModel = new SectionModel();
			sectionModel.setSectionID(dbSection.getCode());
			if(MICAConstants.SPANISH.equals(lCode)) {   
				sectionModel.setEs_name(dbSection.getEs_name());
			} 
			sectionModel.setName(dbSection.getName());
			Set<SymptomCategory> sectionCategories = dbSection.getSymptomCategories();
			if(sectionCategories != null && ! sectionCategories.isEmpty() ) {
				sectionModel.setCategories(createCategories(sectionCategories,dataStoreRefList,groupID));
			}

			modelSections.add(sectionModel);
		}

		// TODO Auto-generated method stub
		return modelSections;
	}

	private List<CategoryModel> createCategories(Set<SymptomCategory> sysmptomCategoryList,Set<String> dataStoreRefList,String groupID) {
		List<CategoryModel> resCategoryList= new ArrayList<CategoryModel>();
		Iterator<SymptomCategory> symtomIt = sysmptomCategoryList.iterator();
		while (symtomIt.hasNext()) {
			SymptomCategory symptomCategory = symtomIt.next();
			CategoryModel category = new CategoryModel();	

			if(MICAConstants.SPANISH.equals(lCode)) {   
				category.setEs_name(symptomCategory.getEs_name());
			} 
			category.setName(symptomCategory.getName());
			category.setCategoryID(symptomCategory.getCode());
		//	if(! groupID.equalsIgnoreCase("nlp")){
			category.setSymptoms(createSymptomps(symptomCategory,dataStoreRefList));
		//	}
			resCategoryList.add(category);
		}
		return resCategoryList;
	}



	private Hashtable<String, DataKeyStore> createDataStoreRefTypes(
			Set<String> dataStoreRefList,DataKeysRepository dataKeysRepository) {
		Hashtable<String, DataKeyStore>  dataStoreRefTypes= new Hashtable<String, DataKeyStore> ();		
		List<String> names = dataStoreRefList.stream().collect(Collectors.toList());
		Iterable<DataKeys> dataKeysList =	dataKeysRepository.getDataKeysForNames(names);
		Iterator<DataKeys> dataKeysListItr = dataKeysList.iterator();
		while(dataKeysListItr.hasNext()){
			DataKeys dataKeys  = 	dataKeysListItr.next();
			if(dataKeys != null){
				String title = dataKeys.getTitle();
				String es_title = dataKeys.getEs_title();

				Set<DataStore> dataStore = dataKeys.getDataStoreList();
				if(dataStore != null){
					dataStoreRefTypes.put(dataKeys.getName(), createValueStore(dataStore,title,es_title));
				}

			}
		}

		return dataStoreRefTypes;
	}


	/**
	 * Prepares the Datakey store from the given datastore to match the below perttern
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param dataStore
	 * @param title
	 * @return
	 */
	private DataKeyStore createValueStore(
			Set<DataStore> dataStore,String title, String es_title ) {
		DataKeyStore  DataKeyStore = new DataKeyStore();
		if(MICAConstants.SPANISH.equals(lCode)) {   
			DataKeyStore.setEs_title(es_title);
		}
		DataKeyStore.setTitle(title);
		List<ValueStore>  valueStores = new ArrayList<ValueStore>();
		Iterator<DataStore> dataStoreItr = dataStore.iterator();
		while(dataStoreItr.hasNext()){
			DataStore dbDataStore=  dataStoreItr.next();
			ValueStore dbValueStore = new ValueStore() ;  

			if(MICAConstants.SPANISH.equals(lCode)) {   
				dbValueStore.setEs_name(dbDataStore.getEs_name());
			} 
			dbValueStore.setName(dbDataStore.getName());
			dbValueStore.setKiosk_name(dbDataStore.getKioskName());
			dbValueStore.setEs_kiosk_name(dbDataStore.getEs_kioskName());
		
			dbValueStore.setM_antithesis(dbDataStore.getM_antithesis());
			dbValueStore.setCount(dbDataStore.getCount());
			dbValueStore.setDisplayOrder(dbDataStore.getDisplayOrder());		
			dbValueStore.setDisplayListValue(dbDataStore.getDisplayListValue());
			dbValueStore.setCode(dbDataStore.getCode());
			dbValueStore.setAnyOption(dbDataStore.getAnyOption());
			
			
			// lab attributes starts here
			
			dbValueStore.setLowerLimit(dbDataStore.getLowerLimit());
			dbValueStore.setLowerLimitCondition(dbDataStore.getLowerLimitCondition());
			dbValueStore.setUpperLimit(dbDataStore.getUpperLimit());
			dbValueStore.setUpperLimitCondition(dbDataStore.getUpperLimitCondition());
			dbValueStore.setIsNormal(dbDataStore.getNormal());
						
			// lab attributes starts here
		
			if(dbDataStore.getDefaultValue() !=null ){
				dbValueStore.setDefaultValue(dbDataStore.getDefaultValue());
			}
			if(dbDataStore.getValue() != null){
				dbValueStore.setValue(dbDataStore.getValue());
			}
			valueStores.add(dbValueStore);
		}
		DataKeyStore.setValues(valueStores);

		return DataKeyStore;
	}

	private List<Symptoms> createSymptomps(SymptomCategory symptomCategory,Set<String> dataStoreRefList) {
		List<Symptoms>  responseSymptoms = new ArrayList<Symptoms>();
		Set<SymptomTemplate> symptomTemplateList = symptomCategory.getSymptomTemplates();
		if(symptomTemplateList != null){
			Iterator<SymptomTemplate> symptomTmpItr = symptomTemplateList.iterator();
			while(symptomTmpItr.hasNext()){
				SymptomTemplate symptomTemplate =	symptomTmpItr.next();
				responseSymptoms.add(createSymtoms(symptomTemplate,dataStoreRefList));
			}
		}
		return responseSymptoms;
	}


	public Symptoms createSymtoms(SymptomTemplate symptomTemplate, Set<String> dataStoreRefList) {
		Symptoms symptoms = new Symptoms();

		if(MICAConstants.SPANISH.equals(lCode)) {   
			symptoms.setEs_name(symptomTemplate.getEs_name());
			symptoms.setEs_question(symptomTemplate.getEs_questionText());
		} 
		symptoms.setName(symptomTemplate.getName());
		symptoms.setQuestion(symptomTemplate.getQuestionText());
		if(symptomTemplate.getTimeType() != null) {
		symptoms.setTimeType(TimeType.fromText(symptomTemplate.getTimeType()));
		}
		symptoms.setSymptomID(symptomTemplate.getCode());
		symptoms.setMultipleValues(symptomTemplate.getMultipleValues());
		symptoms.setCriticality(symptomTemplate.getCriticality());
		
		symptoms.setAntithesis(symptomTemplate.getAntithesis());
		
		symptoms.setTreatable(symptomTemplate.getTreatable());
		symptoms.setCardinality(symptomTemplate.getCardinality());
		symptoms.setPainSwellingID(symptomTemplate.getPainSwellingID());
		symptoms.setDisplayOrder(symptomTemplate.getDisplayOrder());
		symptoms.setGenderGroup(symptomTemplate.getGenderGroup());
	 //   symptoms.setGroupName(getMasterGroupNames(symptomTemplate));
		symptoms.setLogicalGroupNames(getLogicalGroupNames(symptomTemplate));
		symptoms.setDeGroups(getDeGroupNames(symptomTemplate));
	
		symptoms.setUpdatedDate(symptomTemplate.getUpdatedDate());
		symptoms.setCreateddDate(symptomTemplate.getCreateddDate());
		symptoms.setSubGroups(symptomTemplate.getSubGroups());
		symptoms.setDisplaySymptom(symptomTemplate.getDisplaySymptom());
		symptoms.setDisplayDrApp(symptomTemplate.getDisplayDrApp());
		symptoms.setKioskName(symptomTemplate.getKioskName());
		symptoms.setFormalName(symptomTemplate.getFormalName());
		symptoms.setSymptomType(symptomTemplate.getSymptomType());
		symptoms.setSymptomsModel(createQuetionaireModel(symptomTemplate,dataStoreRefList));		
		SymptomTemplate rcodeSymptomTemplates = rCodeSymTemplates.stream().filter(s->s.getCode().equalsIgnoreCase(symptomTemplate.getCode())).findAny().orElse(null);

		// lab attributes starts here
		 boolean isResultRange = false;
		
		if(symptomTemplate.getRange() != null ){
			 isResultRange =  symptomTemplate.getRange() ;
			 symptoms.setIsRange(isResultRange);
			}
		
		if(isResultRange) {
		symptoms.setMaxRange(symptomTemplate.getMaxRange());
		symptoms.setMinRange(symptomTemplate.getMinRange());
		
		}
		// lab attributes ends here
		

		//  key = data store name, value = icd10Code
		Map<String, String> icdListRCodes = new HashMap<String, String>();
		// symptom level rCodes
		List<String> rCodes = new ArrayList<String>();
		//if(rCodedataKey != null ) {
		if(rcodeSymptomTemplates != null ) {
			//logger.info("createSymtoms  :: template code  " + rcodeSymptomTemplates.getCode()  + " ::  RCodeDataKey code " );
			getRCodeListValues(rcodeSymptomTemplates,rCodes,icdListRCodes);
		}

		symptoms.setIcdRCodes(rCodes);
		symptoms.setListIcdRCodes(icdListRCodes);
		return symptoms;
	}
	
	
	
/*	private List<String>  getMasterGroupNames(SymptomTemplate symptomTemplate) {		
 
		Set<LogicalSymptomGroups> logicalSymptomGroups = symptomTemplate.getLogicalGroups();
    	logger.info("+++++++++++++++ Inside getMasterGroupNames  +++++++++++++++++++++++++"  );
		if(logicalSymptomGroups != null && ! logicalSymptomGroups.isEmpty()) {
    	List<Integer> groupIds =logicalSymptomGroups.stream()
			              .map(LogicalSymptomGroups::getGroupID)
			              .collect(Collectors.toList());
    	logger.info("SymptomID " + symptomTemplate.getCode() + "GroupID:: "  + groupIds);
    	System.out.println("SymptomID " + symptomTemplate.getCode() + "GroupID:: "  + groupIds);
    	
 	if(groupIds!= null  && ! groupIds.isEmpty() &&  !classifications.isEmpty()) {
		 List<String> groupNames = classifications.entrySet().stream()
 	      		 .filter(entry -> groupIds.contains(entry.getKey().intValue()) )
 	                .map(Map.Entry::getValue).map( Object::toString ) .collect(Collectors.toList());
			logger.info("Logical groups " + groupNames);
			System.out.println("Logical groups " + groupNames);
		return groupNames;
	}
    }
    return null;
	}*/

	/*private List<String>  getLogicalGroupNames(SymptomTemplate symptomTemplate) {	
		GenericQueryResultEntity groupSymptom = logicalGroups.stream().filter(s->s.getSymptomID().equalsIgnoreCase(symptomTemplate.getCode())).findAny().orElse(null);
	
		if(groupSymptom != null) {
		return groupSymptom.getLogicalGroupNames();
		}
		
		return null;

	}*/
	
	
	private List<String>  getLogicalGroupNames(SymptomTemplate symptomTemplate) {	
		return logicalGroups.stream().filter(s->s.getSymptomID().equalsIgnoreCase(symptomTemplate.getCode())).map(GenericQueryResultEntity::getLogicalGroupNames) .flatMap(List::stream).collect(Collectors.toList());
	
	}
	
	
	
	private List<String>  getDeGroupNames(SymptomTemplate symptomTemplate) {	
		return deGroups.stream().filter(s->s.getSymptomID().equalsIgnoreCase(symptomTemplate.getCode())).map(GenericQueryResultEntity::getDeGroupNames) .flatMap(List::stream).collect(Collectors.toList());

	}

	

	/*private void getRCodeListValues(RCodeDataKey rCodedataKey, List<String> rCodes,
			Map<String, String> icdListRCodes ) {
		Set<RCodeDataStore> dbDataStoreSet = rCodedataKey.getrCodeDataStores();
		if(dbDataStoreSet != null ){
			Iterator<RCodeDataStore> rCodedataStoreItr = dbDataStoreSet.iterator();
			while(rCodedataStoreItr.hasNext()){
				RCodeDataStore dbRCodeDataStore = rCodedataStoreItr.next();
				if(dbRCodeDataStore != null ) {	
					DataStore dataStore = dataStores.stream().filter(s->s.getCode().equalsIgnoreCase(dbRCodeDataStore.getDsCode())).findAny().orElse(null);
					if(dbRCodeDataStore.getDsCode() == null  &&  dbRCodeDataStore.getM_icd10RCode() != null ) {
						rCodes.add(dbRCodeDataStore.getM_icd10RCode());
					} else if(dbRCodeDataStore.getDsCode() != null &&  dbRCodeDataStore.getM_icd10RCode() != null )  {
						if(dataStore != null ) {
							icdListRCodes.put(dataStore.getName(),  dbRCodeDataStore.getM_icd10RCode());
						//	logger.info("DS Name ::  " + dataStore.getName() + "::: RCode DS Code "+ dbRCodeDataStore.getDsCode());
						}
					}
				} 
			}
		} 
	}*/


	
	private void getRCodeListValues(SymptomTemplate symptomTemplate, List<String> rCodes,
			Map<String, String> icdListRCodes ) {
		Set<RCodeDataStore> dbDataStoreSet = symptomTemplate.getrCodeDataStores();
		if(dbDataStoreSet != null ){
			Iterator<RCodeDataStore> rCodedataStoreItr = dbDataStoreSet.iterator();
			while(rCodedataStoreItr.hasNext()){
				RCodeDataStore dbRCodeDataStore = rCodedataStoreItr.next();
				if(dbRCodeDataStore != null ) {	
				//	logger.info("Snomed dsCode :: "+ dbRCodeDataStore.getDsCode());
					DataStore dataStore = dataStores.stream().filter(s->s.getCode().equalsIgnoreCase(dbRCodeDataStore.getDsCode())).findAny().orElse(null);
					if(dbRCodeDataStore.getDsCode() == null  &&  dbRCodeDataStore.getM_icd10RCode() != null ) {
						rCodes.add(dbRCodeDataStore.getM_icd10RCode());
					//	logger.info("Root RCode  "+ dbRCodeDataStore.getM_icd10RCode());
					} else if(dbRCodeDataStore.getDsCode() != null &&  dbRCodeDataStore.getM_icd10RCode() != null )  {
						if(dataStore != null ) {
							icdListRCodes.put(dataStore.getName(),  dbRCodeDataStore.getM_icd10RCode());
						//	logger.info("DS Name ::  " + dataStore.getName() + "::: RCode DS Code ::: "+ dbRCodeDataStore.getM_icd10RCode());
						}
					}
				}  else{
				//	logger.info("dbRCodeDataStore is null :::  "+ dbRCodeDataStore);
				}
			}
		}  else{
			//logger.info("List is null for ::: "+ symptomTemplate.getCode());
		}
	}
	
	private SymptomsTmplModel createQuetionaireModel(
			SymptomTemplate symptomModel, Set<String> dataStoreRefList) {
		// TODO Auto-generated method stub
		SymptomsTmplModel questionaireModel = new SymptomsTmplModel();
		if( symptomModel != null ) {
			if(symptomModel.getBias() != null ) {
				questionaireModel.setBias(symptomModel.getBias());
			}
			if(symptomModel.getRangeValues() != null) {
				questionaireModel.setRangeValues(getDouble(symptomModel.getRangeValues()));
			}
			questionaireModel.setDataStoreTypes(symptomModel.getDataStoreTemplates());
			dataStoreRefList.addAll(symptomModel.getDataStoreTemplates());
			if(symptomModel.getDescriptors() != null) {
				questionaireModel.setDescriptors(symptomModel.getDescriptors());
			}

			if(symptomModel.getDescriptorFile() != null) {
				questionaireModel.setDescriptorFile(symptomModel.getDescriptorFile());
			}
			/*if(symptomModel.getName() != null ) {
				questionaireModel.setName(symptomModel.getName());
			}*/
			/*	if(symptomModel.getQuestionText() != null ) {
				questionaireModel.setQuestionText(symptomModel.getQuestionText());
			}*/
			if(symptomModel.getScaleTimeLimitText() != null ) {
				questionaireModel.setScaleInfoText(symptomModel.getScaleTimeLimitText());
			}
			if( symptomModel.getTimeRangeStop() != null ) {
				questionaireModel.setScaleTimeLimit(symptomModel.getTimeRangeStop());
			}
			if(symptomModel.getTimeRangeStart() != null ) {
				questionaireModel.setScaleTimeLimitStart(symptomModel.getTimeRangeStart());
			}
			if(symptomModel.getTimeUnitDefault() != null ) {
				questionaireModel.setTimeUnitDefault(symptomModel.getTimeUnitDefault());
			}
		}
		return questionaireModel;
	}


	public List<Double> getDouble(List<String> strList){
		List<Double> listDouble = new ArrayList<Double>();
		for (int i = 0; i < strList.size(); i++) {

			listDouble.add(Double.parseDouble(strList.get(i)));
		}
		return listDouble;


	}


}
