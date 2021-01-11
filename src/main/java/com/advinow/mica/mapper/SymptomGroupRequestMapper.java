package com.advinow.mica.mapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.advinow.mica.domain.Section;
import com.advinow.mica.domain.SymptomCategory;
import com.advinow.mica.domain.SymptomGroup;
import com.advinow.mica.domain.SymptomTemplate;
import com.advinow.mica.model.CategoryModel;
import com.advinow.mica.model.SectionModel;
import com.advinow.mica.model.SymptomGroups;
import com.advinow.mica.model.Symptoms;
import com.advinow.mica.model.SymptomsTmplModel;
import com.advinow.mica.repositories.SymptomTemplateRepository;
/**
 * 
 * @author Govinda Reddy
 *
 */
public class SymptomGroupRequestMapper {


	public void createsymptomGroup(SymptomGroup symptomGroup,SymptomGroups group,SymptomTemplateRepository symptomTemplateRepository) {		
		Set<SymptomCategory> dbSymptomCategory = symptomGroup.getCategories();
		if( dbSymptomCategory != null ){
			List<CategoryModel> categories = group.getCategories();	
			parseCategories(dbSymptomCategory,symptomTemplateRepository,categories);
		}
		// This is to populate sections
		Set<Section> dbSections = symptomGroup.getSections();

		if(dbSections != null &&  !dbSections.isEmpty()) {
			Iterator<Section> dbSectionItr = dbSections.iterator();
			while(dbSectionItr.hasNext()){
				Section dbSection = dbSectionItr.next();
				Set<SymptomCategory> dbSectionCatetories = dbSection.getSymptomCategories();
				List<SectionModel> sections = group.getSections();
				if(sections != null && !sections.isEmpty()){
					for (int i = 0; i < sections.size(); i++) {
						SectionModel section = sections.get(i);
						if(section.getSectionID().equals(dbSection.getCode())){
							List<CategoryModel> painCategories = section.getCategories();
							if( dbSectionCatetories != null ){
								parseCategories(dbSectionCatetories,symptomTemplateRepository,painCategories);
							}

						}


					}


				}


			}


		}

	}


	private void parseCategories(Set<SymptomCategory> dbSymptomCategory,
			SymptomTemplateRepository symptomTemplateRepository,List<CategoryModel> categories) {
		if(dbSymptomCategory !=null &&  ! dbSymptomCategory.isEmpty()) {
			Iterator<SymptomCategory> dbItr = dbSymptomCategory.iterator();
			while( dbItr.hasNext()){
				SymptomCategory dbsymptomCategory = dbItr.next();
				for (int i = 0; i < categories.size(); i++) {
					CategoryModel category = categories.get(i);
					if(category.getCategoryID().equalsIgnoreCase(dbsymptomCategory.getCode())){
						createCategories(dbsymptomCategory,category,symptomTemplateRepository);
					}

				}
			}
		}

	}


	public void createCategories(SymptomCategory dbsymptomCategory,
			CategoryModel category,SymptomTemplateRepository symptomTemplateRepository) {
		// TODO Auto-generated method stub		
		dbsymptomCategory.setSymptomTemplates(createSymptomTemplateList(category,symptomTemplateRepository));

	}

	public Set<SymptomTemplate> createSymptomTemplateList(CategoryModel category,SymptomTemplateRepository symptomTemplateRepository) {
		Set<SymptomTemplate>  sytompTemplate= new HashSet<SymptomTemplate>();
		List<Symptoms> symptoms = category.getSymptoms();
		if(symptoms != null ) {
			for (int i = 0; i < symptoms.size(); i++) {
				Symptoms symptom = symptoms.get(i);
				/*SymptomTemplate dbSymtomTeplate=symptomTemplateRepository.findByCode(symptom.getCode(), 1);
				if(dbSymtomTeplate == null){
					dbSymtomTeplate = new  SymptomTemplate();
				}*/
				
				SymptomTemplate	dbSymtomTeplate = new  SymptomTemplate();
				createSymptomTemplate(symptom,dbSymtomTeplate);
				sytompTemplate.add(dbSymtomTeplate);	
			}
		}
		return sytompTemplate;
	}


	public SymptomTemplate createSymptomTemplate(Symptoms symptom,SymptomTemplate dbSymtomTeplate) {
		if(symptom.getName() != null) {
			dbSymtomTeplate.setName(symptom.getName());
		}
		
		if(symptom.getCriticality() != null ) {
			dbSymtomTeplate.setCriticality(symptom.getCriticality());
		}
		
		if(symptom.getCardinality() != null ) {
			dbSymtomTeplate.setCardinality(symptom.getCardinality());
		}
		
		if(symptom.getTreatable() != null ) {
			dbSymtomTeplate.setTreatable(symptom.getTreatable());
		}
		if(symptom.getAntithesis()!= null ) {
			dbSymtomTeplate.setAntithesis(symptom.getAntithesis());
		}
		
		
		if(symptom.getPainSwellingID()!= null ) {
			dbSymtomTeplate.setPainSwellingID(symptom.getPainSwellingID());
		}
		
		if(symptom.getDisplayOrder()!= null ) {
			dbSymtomTeplate.setDisplayOrder(symptom.getDisplayOrder());
			
		}
		
	
		
		//if(symptom.getDisplayOrder()!= null ) {
		
			//dbSymtomTeplate.setGenderGroup(symptom.getGenderGroup());
		dbSymtomTeplate.setGenderGroup(String.valueOf(symptom.getGenderGroup()));
		
		//}
		
	/*	if(symptom.getIcdRCode() != null){
			dbSymtomTeplate.setICDRCode(symptom.getIcdRCode());
		}
		*/
		
		if(symptom.getSubGroups() != null  && ! symptom.getSubGroups().isEmpty()) {
			dbSymtomTeplate.setSubGroups(symptom.getSubGroups());
		}
		dbSymtomTeplate.setCode(setCode(symptom));
		//dbSymtomTeplate.setiCD10Code(createICD10Codes(symptom.getSymptomICD10Code()));
		if(symptom.getMultipleValues() !=null && ! symptom.getMultipleValues().isEmpty()){		
			dbSymtomTeplate.setMultipleValues(symptom.getMultipleValues());
		}
		
		SymptomsTmplModel questionaireModel = symptom.getSymptomsModel(); 
		if(questionaireModel.getBias() !=null) {
			dbSymtomTeplate.setBias(questionaireModel.getBias());
		}
		if(questionaireModel.getRangeValues() != null ){
			dbSymtomTeplate.setRangeValues(getString(questionaireModel.getRangeValues()));

		}
		if(questionaireModel.getDescriptors() != null) {
			dbSymtomTeplate.setDescriptors(questionaireModel.getDescriptors());
		}
		if(questionaireModel.getDescriptorFile() != null) {
			dbSymtomTeplate.setDescriptorFile(questionaireModel.getDescriptorFile());
		}
		if(questionaireModel.getScaleTimeLimit() !=null ) {
			dbSymtomTeplate.setTimeRangeStop(questionaireModel.getScaleTimeLimit());
		}

		if(questionaireModel.getScaleInfoText() !=null ) {
			dbSymtomTeplate.setScaleTimeLimitText(questionaireModel.getScaleInfoText());   
		}

		if(questionaireModel.getScaleTimeLimitStart() !=null ) {
			dbSymtomTeplate.setTimeRangeStart(questionaireModel.getScaleTimeLimitStart());
		}
		
		if(questionaireModel.getQuestionText() != null ) {
			dbSymtomTeplate.setQuestionText(questionaireModel.getQuestionText());
		}
		
		if(questionaireModel.getTimeType() != null ) {
			dbSymtomTeplate.setTimeType(questionaireModel.getTimeType().getText());
		}
		
		
		if(questionaireModel.getName() != null ) { 
			dbSymtomTeplate.setName(questionaireModel.getName());
		}
		if ( questionaireModel.getTimeUnitDefault() != null ) {
			dbSymtomTeplate.setTimeUnitDefault(questionaireModel.getTimeUnitDefault());
		}
		if( questionaireModel.getDataStoreTypes() != null  ) {
			dbSymtomTeplate.setDataStoreTemplates(questionaireModel.getDataStoreTypes());
		}
		
		
	
		
		return dbSymtomTeplate;

	}


	private String setCode(Symptoms symptom) {
		String code = symptom.getCode();
		if(code == null || code.isEmpty()){
			code= symptom.getSymptomID();
		}

		if(code == null || code.isEmpty()){
			code= "SYMPT" + getTimeStamp();
		}
		return code;
	}

/*
	private SymptomTemplateModel createSymtomModel(SymptomTemplateModel dbSymptomModel,
			SymptomsTmplModel questionaireModel) {
		// TODO Auto-generated method stub

		if(questionaireModel.getBias() !=null) {
			dbSymptomModel.setBias(questionaireModel.getBias());
		}
		if(questionaireModel.getRangeValues() != null ){
			dbSymptomModel.setRangeValues(questionaireModel.getRangeValues());

		}
		if(questionaireModel.getDescriptors() != null) {
			dbSymptomModel.setDescriptors(questionaireModel.getDescriptors());
		}
		if(questionaireModel.getDescriptorFile() != null) {
			dbSymptomModel.setDescriptorFile(questionaireModel.getDescriptorFile());
		}
		if(questionaireModel.getScaleTimeLimit() !=null ) {
			dbSymptomModel.setScaleTimeLimit(questionaireModel.getScaleTimeLimit());
		}

		if(questionaireModel.getScaleInfoText() !=null ) {
			dbSymptomModel.setScaleTimeLimitText(questionaireModel.getScaleInfoText());   
		}

		if(questionaireModel.getScaleTimeLimitStart() !=null ) {
			dbSymptomModel.setScaleTimeLimitStart(questionaireModel.getScaleTimeLimitStart());
		}
		if(questionaireModel.getQuestionText() != null ) {
			dbSymptomModel.setQuestionText(questionaireModel.getQuestionText());
		}
		if(questionaireModel.getName() != null ) { 
			dbSymptomModel.setName(questionaireModel.getName());
		}
		if ( questionaireModel.getTimeUnitDefault() != null ) {
			dbSymptomModel.setTimeUnitDefault(questionaireModel.getTimeUnitDefault());
		}
		if( questionaireModel.getDataStoreTypes() != null  ) {
			dbSymptomModel.setDataStoreTeplates(questionaireModel.getDataStoreTypes());
		}
		return dbSymptomModel;
	}
*/
	
	public List<String> getString(List<Double> doubleList){
		
		List<String> listStr = new ArrayList<String>();
		for (int i = 0; i < doubleList.size(); i++) {
			
			listStr.add(Double.toString(doubleList.get(i)));
		}
		
		
		return listStr;
		
		
	}

	private String getTimeStamp(){
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
		String datetime = ft.format(dNow);
		return datetime;
	}
}
