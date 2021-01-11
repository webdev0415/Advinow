package com.advinow.mica.services;

import java.util.List;
import java.util.Set;

import com.advinow.mica.domain.SymptomCategory;
import com.advinow.mica.domain.SymptomGroup;
import com.advinow.mica.domain.SymptomTemplate;
import com.advinow.mica.domain.queryresult.GenericQueryResultEntity;
import com.advinow.mica.exception.MICAApplicationException;
import com.advinow.mica.model.CategoryModel;
import com.advinow.mica.model.GenericSymptomGroups;
import com.advinow.mica.model.GenericSymptomPagination;
import com.advinow.mica.model.MICASymptomsGroup;
import com.advinow.mica.model.PaginationModel;
import com.advinow.mica.model.LogicalSymptomGroupsModel;
import com.advinow.mica.model.SymptomGroups;
import com.advinow.mica.model.SymptomTemplateDTO;
import com.advinow.mica.model.SymptomTemplateModel;
import com.advinow.mica.model.SymptomTemplateModelV1;
import com.advinow.mica.model.SymptomTemplateValidate;
import com.advinow.mica.model.Symptoms;
import com.advinow.mica.model.SymptomsMainModel;

/**
 * 
 * @author Govinda Reddy
 *
 */
public interface MICATemplateService {
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public SymptomGroups getSymptomsByGroup(String name,String lCode);
	
	/**
	 * 
	 * @return
	 */
	public MICASymptomsGroup getSymtomGroups(String lCode);
	/**
	 * 
	 * @param micaSymptomsGroup
	 * @return
	 */
	public Iterable<SymptomGroup>  createSymtoms(MICASymptomsGroup micaSymptomsGroup);
	/**
	 * 
	 * @param category
	 * @return
	 * @throws MICAApplicationException
	 */
	public SymptomCategory createSymptomsByCategory(CategoryModel category) throws MICAApplicationException;
    /**
     * 
     * @param symptomsGroup
     * @param groupId
     * @return
     * @throws MICAApplicationException
     */
	public SymptomGroups createsymptomsByGroup(SymptomGroups symptomsGroup,String groupId) throws MICAApplicationException;
	/**
	 * 
	 * @param symptomID
	 * @return
	 * @throws MICAApplicationException
	 */
	public String deleteSymptom(String symptomID) throws MICAApplicationException;
    
	/**
     * 
     * @param groupId
     * @return
     */
	public SymptomTemplateModel getSymptomByID(String groupId,String lCode);
	
	/**
     * 
     * @param groupId
     * @return
     */
	public SymptomTemplateModelV1 getSymptomByIDV1(String groupId,String lCode);
	
	
	/**
	 * 
	 * @param symptomTemplate
	 * @return
	 */
	public SymptomTemplateModel updateSymptom(SymptomTemplateModel symptomTemplate,String languagecode);
	
	
	/**
	 * 
	 * @param symptomTemplate
	 * @return
	 */
	public SymptomTemplateModelV1 updateSymptomV1(SymptomTemplateModelV1 symptomTemplate,String languagecode);
	
	/**
	 * 
	 * @param symptomID
	 * @return
	 */
	public Symptoms  getSymptomByCode(String symptomID);
	
	/**
	 * 
	 * @return
	 */
	public List<SymptomTemplateValidate> validateTemplates(String language);	
	
	public Set<SymptomTemplate> getAllSymptomTemplates();

	public List<SymptomTemplateModel> getAllSymptomDefinitions();

	public PaginationModel getSymptomsWithPaging(Integer page, Integer size,
			String upperCase,  String code, String name,
			String group);
	
	
	GenericSymptomPagination getSymptomTemplatesWithPaging(String source,
			String name, Integer page, Integer size);

	public GenericSymptomGroups symptomsByCategory(String groupId,	String language);

	public SymptomsMainModel getSymptomBasicInfo();
    
	/**
	 * 
	 * Returns questions for all the symptoms
	 * 
	 * @return List<SymptomTemplateModel>
	 */
	public List<SymptomTemplateDTO> getAllSymptomQuestions();

	public SymptomTemplateDTO getSymptomQuestion(String symptomID);

	public List<GenericQueryResultEntity> searchSymptomsByName(String symptomName);

	public String SaveOrUpdateSymptomGroups(LogicalSymptomGroupsModel logicalSymptomGroupsModel);

	public LogicalSymptomGroupsModel getSymptomsForGivenGroup(Integer groupID);

/*	public PaginationModel searchSymptomsByName(Integer page, Integer size,String name);*/

		
}
