package com.advinow.mica.services;

import java.util.List;
import java.util.Map;

import com.advinow.mica.domain.BadIllness;
import com.advinow.mica.domain.Coding_Rules;
import com.advinow.mica.domain.SymptomSourceInfo;
import com.advinow.mica.exception.MICAApplicationException;
import com.advinow.mica.model.ICD10CodeModel;
import com.advinow.mica.model.ICD10CodesModel;
import com.advinow.mica.model.IllnessModel;
import com.advinow.mica.model.IllnessSourcesModel;
import com.advinow.mica.model.IllnessStatusModel;
import com.advinow.mica.model.IllnessUserData;
import com.advinow.mica.model.PaginationModel;
import com.advinow.mica.model.UserICD10CodeModel;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 
 * @author Govinda Reddy
 *
 */
public interface MICAIllnessService {
	/**
	 * 
	 * @param userID
	 * @return
	 */
	public List<IllnessModel> getIllnessesByUser(Integer userID);

	/**
	 * 
	 * @param userData
	 * @return
	 * @throws MICAApplicationException
	 * @throws JsonProcessingException
	 */
	public IllnessStatusModel addIllnessUserData(IllnessUserData userData)
			throws MICAApplicationException, JsonProcessingException;

	/**
	 * 
	 * @param userID
	 * @return
	 */
	public IllnessUserData getIllnessesForGroupsyByUser(Integer userID);

	/**
	 * 
	 * @return
	 */
	public IllnessUserData getIllnessesForGroups();

	/**
	 * 
	 * @param symptomIDs
	 * @return
	 */
	public List<IllnessModel> getIllnessesBySymptomsIDs(List<String> symptomIDs);

	/**
	 * 
	 * @param illnessStatusModel
	 * @return
	 * @throws MICAApplicationException
	 */
	public IllnessStatusModel illnessStatusUpdate(IllnessStatusModel illnessStatusModel)
			throws MICAApplicationException;

	/**
	 * 
	 * @param illnessStatusModel
	 * @return
	 * @throws MICAApplicationException
	 */
	public IllnessStatusModel illnessStatusUpdateWithVersion(List<IllnessStatusModel> illnessStatusModel)
			throws MICAApplicationException;

	/**
	 * 
	 * @param illnessStatusModel
	 * @return
	 * @throws MICAApplicationException
	 */
	public IllnessStatusModel updateRollebackUser(List<IllnessStatusModel> illnessStatusModel)
			throws MICAApplicationException;

	/**
	 * 
	 * @param id
	 */
	public void getIllnessesDeleteBycode(Long id);

	/**
	 * 
	 * @param id
	 */
	public void deleteSymptomByID(Long id);

	/**
	 * 
	 * @param illnessStatusModel
	 * @return
	 */
	public IllnessUserData findByIllnessByUserAndStatus(IllnessStatusModel illnessStatusModel);

	/**
	 * 
	 * @param illnessStatusModel
	 * @return
	 * @throws MICAApplicationException
	 */
	public IllnessStatusModel deleteIllnessesByIDS(IllnessStatusModel illnessStatusModel)
			throws MICAApplicationException;

	/**
	 * 
	 * @param illnessStatusModel
	 * @return
	 * @throws MICAApplicationException
	 */
	public IllnessStatusModel cloneIllness(IllnessStatusModel illnessStatusModel) throws MICAApplicationException;

	/**
	 * 
	 * @param illnessStatusModel
	 * @return
	 */
	public ICD10CodesModel getIllnessFromICDCodes(IllnessStatusModel illnessStatusModel);

	/**
	 * 
	 * @param iCD10CodeModel
	 * @return
	 */
	public ICD10CodeModel updateIllnessRecord(ICD10CodeModel iCD10CodeModel);

	/**
	 * 
	 * @param symptomID
	 */
	public void deleteBySymptomID(String symptomID);

	/**
	 * 
	 * @param icd10Code
	 * @param state
	 * @return
	 */
	public IllnessUserData getIllnessByIcd10Code(String icd10Code, String state);

	/* public void updateSymptomRank(); */

	/**
	 * 
	 * @param userID
	 * @return
	 */
	public Iterable<BadIllness> getInvalidIllnesses();

	public Map<String, List<Integer>> findByicd10CodeByUserAndStatus(Integer userID, String state);

	public List<UserICD10CodeModel> getIllnessInformation(Integer userID, String source);

	public List<String> getUniqueIllnesses(String icd10Code, Boolean time, Integer version);

	public PaginationModel getIllnessWithPagingForGivenSymptom(Integer page, Integer size, String upperCase,
			String symptomID, String symptomName);

	public Iterable<Coding_Rules> getDiseaseCodingRules();

	public Boolean chiefcomplaintcheck(String icd10Code, Integer version);

	public Map<String, List<String>> getMDCSymptomsByIllnesses(List<String> icd10Codes, boolean mdcFlag);

	public Map<String, List<String>> getBiasSymptomsByIllnesses(List<String> icd10Codes, boolean bias);

	public List<SymptomSourceInfo> getIllnessSymptomsSources(String icd10code, String state, Integer version);

	public List<SymptomSourceInfo> getIllnessSymptomsSourcesForGivenUser(Integer userID, List<String> state);

	public String updateIllnessSources(IllnessSourcesModel illnessSourcesModel);

}
