/**
 * 
 */
package com.advinow.mica.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import io.github.resilience4j.retry.annotation.Retry;

import com.advinow.mica.domain.BadIllness;
import com.advinow.mica.domain.Coding_Rules;
import com.advinow.mica.domain.DataStoreModifier;
import com.advinow.mica.domain.Illness;
import com.advinow.mica.domain.Section;
import com.advinow.mica.domain.Symptom;
import com.advinow.mica.domain.SymptomCategory;
import com.advinow.mica.domain.SymptomDataStore;
import com.advinow.mica.domain.SymptomGroup;
import com.advinow.mica.domain.SymptomSourceInfo;
import com.advinow.mica.domain.User;
import com.advinow.mica.domain.queryresult.IllnessDataQueryResultEnitity;
import com.advinow.mica.exception.DataInvalidException;
import com.advinow.mica.exception.MICAApplicationException;
import com.advinow.mica.exception.ServiceNotAvailableException;
import com.advinow.mica.mapper.IllnessRequestMapper;
import com.advinow.mica.mapper.IllnessResponseMapper;
import com.advinow.mica.mapper.IllnessVersionMergeMapper;
import com.advinow.mica.model.ICD10CodeModel;
import com.advinow.mica.model.ICD10CodesModel;
import com.advinow.mica.model.IllnessModel;
import com.advinow.mica.model.IllnessSourcesModel;
import com.advinow.mica.model.IllnessStatusModel;
import com.advinow.mica.model.IllnessUserData;
import com.advinow.mica.model.MultiplierSources;
import com.advinow.mica.model.PaginationModel;
import com.advinow.mica.model.AuditSourceModel;
import com.advinow.mica.model.SymptomGroups;
import com.advinow.mica.model.UserICD10CodeModel;
import com.advinow.mica.model.mita.CollectorValueObject;
import com.advinow.mica.model.mita.MitaValueObject;
import com.advinow.mica.model.mita.ReviewerValueObject;
import com.advinow.mica.repositories.BadIllnessRepository;
import com.advinow.mica.repositories.CategoryRepository;
import com.advinow.mica.repositories.DataStoreModifierRepository;
import com.advinow.mica.repositories.IllnessRepository;
import com.advinow.mica.repositories.SectionRepository;
import com.advinow.mica.repositories.SymptomCategoryRepository;
import com.advinow.mica.repositories.SymptomDataStoreRepository;
import com.advinow.mica.repositories.SymptomGroupRepository;
import com.advinow.mica.repositories.SymptomRepository;
import com.advinow.mica.repositories.SymptomSourceInfoRepository;
import com.advinow.mica.repositories.SymptomTemplateRepository;
import com.advinow.mica.repositories.UserRepository;
import com.advinow.mica.services.MICAIllnessService;
import com.advinow.mica.util.MICAConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Govinda Reddy
 *
 */

@Service
@Retry(name = "neo4j")
public class MICAIllnessServiceImpl implements MICAIllnessService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	IllnessRepository illnessRepository;

	IllnessRequestMapper illnessRequestMapper = new IllnessRequestMapper();

	@Autowired
	SymptomRepository symptomRepository;

	@Autowired
	UserRepository userRepository;

	IllnessResponseMapper illnessResponseMapper = new IllnessResponseMapper();

	@Autowired
	SymptomCategoryRepository symptomCategoryRepository;

	@Autowired
	SymptomGroupRepository symptomGroupRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	SymptomDataStoreRepository symptomDataStoreRepository;

	@Autowired
	DataStoreModifierRepository dataStoreModifierRepository;

	@Autowired
	SectionRepository sectionRepository;

	@Autowired
	SymptomTemplateRepository symptomTemplateRepository;

	@Autowired
	BadIllnessRepository badIllnessRepository;

	IllnessVersionMergeMapper illnessVersionMergeMapper = new IllnessVersionMergeMapper();

	// Map<String,Long> secondsConvertionMap= MICAUtil.getSecondsMap();

	@Autowired
	SymptomSourceInfoRepository symptomSourceInfoRepository;

	@Value("${mita.rest.url}")
	String mitaBaseUrl;

	@Value("${mica.illness.validation}")
	Boolean valiation;

	@Override
	public List<IllnessModel> getIllnessesByUser(Integer userID) {
		List<IllnessModel> illnesses = new ArrayList<IllnessModel>();
		User user = userRepository.findByUserID(userID, 7);
		if (user != null) {
			Set<Illness> dbIllnesse = user.getIllnesses();
			if (dbIllnesse != null && !dbIllnesse.isEmpty()) {
				Iterator<Illness> dbIllnessItr = dbIllnesse.iterator();
				while (dbIllnessItr.hasNext()) {
					Illness dbIllness = dbIllnessItr.next();
					IllnessModel modeIillness = illnessResponseMapper.createIllnessModel(dbIllness);
					modeIillness.setUserID(userID);
					illnesses.add(modeIillness);
				}
			}
		}

		return illnesses;
	}

	@Override
	public IllnessUserData getIllnessesForGroupsyByUser(Integer userID) {
		Iterable<SymptomGroup> groups = symptomGroupRepository.findAll(2);
		List<IllnessModel> illnesses = new ArrayList<IllnessModel>();
		IllnessUserData userData = new IllnessUserData();
		User user = userRepository.findByUserID(userID, 4);
		Set<Section> dbSection = sectionRepository.getPainSwellingSection();
		if (user != null) {
			Set<Illness> dbIllnesse = user.getIllnesses();
			if (dbIllnesse != null && !dbIllnesse.isEmpty()) {
				Iterator<Illness> dbIllnessItr = dbIllnesse.iterator();
				while (dbIllnessItr.hasNext()) {
					Illness dbIllness = dbIllnessItr.next();
					IllnessModel modeIillness = illnessResponseMapper.createIllnessModelByGroup(dbIllness, groups,
							dbSection);
					modeIillness.setUserID(userID);
					illnesses.add(modeIillness);
				}

			}
		}

		userData.setUserData(illnesses);
		return userData;
	}

	@Override
	@Transactional
	public IllnessStatusModel addIllnessUserData(IllnessUserData userData)
			throws MICAApplicationException, JsonProcessingException {
		IllnessStatusModel responseModel = new IllnessStatusModel();
		Set<String> icdCodes = new HashSet<String>();
		if (userData != null) {
			List<IllnessModel> userIllnessData = userData.getUserData();
			if (userIllnessData != null && !userIllnessData.isEmpty()) {
				for (int i = 0; i < userIllnessData.size(); i++) {
					IllnessModel illnessModel = userIllnessData.get(i);
					if (illnessModel != null) {
						String userType = null;
						String status = null;
						String statusOld = null;
						MitaValueObject mitaObject = null;
						Boolean mitaSuccess = true;
						Boolean micaSuccess = true;

						try {
							logger.info("1 illnessModel.getState() = " + illnessModel.getState());

							if (illnessModel.getState().equalsIgnoreCase(MICAConstants.COMPLETE)) {
								userType = MICAConstants.MITA_COLLECTOR;
								status = MICAConstants.MITA_COMPLETE;
								statusOld = MICAConstants.MITA_PENDING;
								logger.info(userType + status + statusOld);
							} else if (illnessModel.getState().equalsIgnoreCase(MICAConstants.APPROVED)) {
								userType = MICAConstants.MITA_REVIEWER;
								status = MICAConstants.MITA_APPROVED;
								statusOld = MICAConstants.MITA_READY_FOR_REVIEW;
								logger.info(userType + status + statusOld);
							} else if (illnessModel.getState().equalsIgnoreCase(MICAConstants.REJECTED)) {
								userType = MICAConstants.MITA_REVIEWER;
								status = MICAConstants.MITA_REJECTED;
								statusOld = MICAConstants.MITA_READY_FOR_REVIEW;
								logger.info(userType + status + statusOld);
							}

							if (!MICAConstants.NO_MITA_CALL.contains(illnessModel.getState())) {
								logger.info("2 illnessModel.getState() = " + illnessModel.getState());
								mitaObject = prepareMitaObject(illnessModel);
								logger.info("mitaUserSateChange: mitaObject:" + mitaObject + " userType:" + userType
										+ " statusOld:" + statusOld);
								mitaSuccess = mitaUserStateChange(mitaObject, userType, status);
							}

							logger.info("3 illnessModel.getState() = " + illnessModel.getState() + " mitaSuccess="
									+ mitaSuccess);

							if (mitaSuccess) {
								micaSuccess = saveIllnessData(illnessModel);
								if (micaSuccess) {
									logger.info("icdCodes.add " + illnessModel.getIdIcd10Code());
									icdCodes.add(illnessModel.getIdIcd10Code());
								} else {
									logger.warn("saveIllnessData failed micaSuccess = " + micaSuccess);
								}
								if (!MICAConstants.NO_MITA_CALL.contains(illnessModel.getState())) {
									if (!micaSuccess) {
										logger.info("mitaUserSateChange: mitaObject:" + mitaObject + " userType:"
												+ userType + " statusOld:" + statusOld);
										mitaSuccess = mitaUserStateChange(mitaObject, userType, statusOld);
									}
								}
								logger.info("4 illnessModel.getState() = " + illnessModel.getState() + " mitaSuccess="
										+ mitaSuccess);
							}
						} catch (Exception e) {
							logger.error("ERROR addIllnessUserData", e);
							e.printStackTrace();
							if (!MICAConstants.NO_MITA_CALL.contains(illnessModel.getState())) {
								if (!micaSuccess) {
									logger.info("mitaUserSateChange: mitaObject:" + mitaObject + " userType:" + userType
											+ " statusOld:" + statusOld);
									mitaSuccess = mitaUserStateChange(mitaObject, userType, statusOld);
								}
							}
							logger.info("5 illnessModel.getState() = " + illnessModel.getState() + " mitaSuccess="
									+ mitaSuccess);
							throw new MICAApplicationException(e.getCause().getMessage());
						}
					}
				}
			} else {
				throw new MICAApplicationException("Illness Data does not exists");
			}

			if (icdCodes.size() > 0) {
				responseModel.setCount(icdCodes.size());
				responseModel.getIcd10CodesStatus().addAll(icdCodes);
				responseModel.setStatus("Illnessess created successfully..");
			} else {
				throw new MICAApplicationException("No illness data saved..");
			}
		}
		return responseModel;
	}

	private MitaValueObject prepareMitaObject(IllnessModel illnessModel) {
		// TODO Auto-generated method stub
		MitaValueObject mitaObject = new MitaValueObject();
		CollectorValueObject collector = null;
		ReviewerValueObject reviewer = null;
		Integer userID = illnessModel.getUserID();
		Integer version = illnessModel.getVersion();
		if (version == null) {
			version = 1;
		}
		String icd10Code = illnessModel.getIdIcd10Code();
		if (illnessModel.getState().contentEquals(MICAConstants.COMPLETE)) {
			collector = new CollectorValueObject();
			collector.setCollectorId(userID);
			collector.setIllnessCode(icd10Code);
			collector.setVersion(version);
			mitaObject.setCollector(collector);

		} else {
			reviewer = new ReviewerValueObject();
			reviewer.setReviewerId(userID);
			reviewer.setIllnessCode(icd10Code);
			reviewer.setVersion(version);
			mitaObject.setReviewer(reviewer);
		}
		return mitaObject;
	}

	@Transactional
	private Boolean saveIllnessData(IllnessModel illnessModel) {

		String newStatus = null;

		Integer userId = illnessModel.getUserID();
		Boolean micaSuccess = true;
		Set<Illness> illnesses = new HashSet<Illness>();
		User user = null;
		Integer version = illnessModel.getVersion();

		if (userId != null) {
			if (version == null) {
				version = 1;
			}

			List<Illness> dbIllnesses = illnessRepository
					.findIllnessIdByICD10CodeAndVersion(illnessModel.getIdIcd10Code(), version);

			// List<String> dbSymptoms = null;

			// if(valiation){
			// dbSymptoms = getDBSymptomsForValidations(illnessModel);
			// List<SymptomValidatorResultEnitity> templateInfo =
			// symptomTemplateRepository.getSymptomSettings(dbSymptoms);
			// logger.info("size of db symptoms::::::::::::::::::" + templateInfo.size());
			// }

			// logger.info("Symptoms ::" + dbSymptoms);

			if (dbIllnesses != null && !dbIllnesses.isEmpty()) {
				illnessModel.setDfstatus(dbIllnesses.get(0).getDfstatus());
				illnessModel.setActive(dbIllnesses.get(0).getActive());
				List<Long> dbIllnessIds = dbIllnesses.stream().map(Illness::getId).collect(Collectors.toList());
				deleteIllnessData(dbIllnessIds);

			}

			// this part of code to create the illnesses versions

			/*
			 * if(dbIllnesses != null && ! dbIllnesses.isEmpty()) {
			 * illnessModel.setActive(dbIllnesses.get(0).getActive());
			 * if(illnessModel.getState().equalsIgnoreCase(MICAConstants.MITA_APPROVED)) {
			 * newStatus = illnessModel.getUserID() + "_" + illnessModel.getVersion() + "_"
			 * + illnessModel.getUpdatedDate() ;
			 * illnessRepository.updateIllnessIdByICD10CodeAndVersion(illnessModel.
			 * getIdIcd10Code(),version,newStatus); } else { List<Long> dbIllnessIds =
			 * dbIllnesses.stream().map(Illness::getId).collect(Collectors.toList());
			 * deleteIllnessData(dbIllnessIds); }
			 * 
			 * }
			 */

			Illness illness = illnessRequestMapper.prepareIllness(illnessModel);

			illnesses.add(illness);
			user = userRepository.findByUserID(userId, 1);

			if (user == null) {
				user = new User();
			}
			user.setUserID(userId);
			user.getIllnesses().addAll(illnesses);
			User savedUser = userRepository.save(user);

			if (savedUser == null) {
				micaSuccess = false;
			}
			// merge starts here
			/*
			 * if(savedUser != null && illness.getState().equals(MICAConstants.APPROVED) ) {
			 * // mergeIllness(illnessModel); }
			 */
		} else {
			micaSuccess = false;
			logger.error("ERROR :::User information not found and illness data not saved for :: "
					+ illnessModel.getIdIcd10Code() + " " + version);
			// throw new DataCreateException("ERROR :::User information not found and
			// illness data not saved for :: " +illnessModel.getIdIcd10Code() + " " +
			// version);
		}
		return micaSuccess;
	}

	/*
	 * private List<String> getDBSymptomsForValidations(IllnessModel illnessModel) {
	 * 
	 * 
	 * List<String> symptoms = new ArrayList<String>();
	 * 
	 * List<SymptomGroups> symptomGroups = illnessModel.getSymptomGroups();
	 * if(symptomGroups != null && !symptomGroups.isEmpty()) { for (SymptomGroups
	 * symptomGroup : symptomGroups) { if(symptomGroup != null) {
	 * List<CategoryModel> categories = symptomGroup.getCategories(); if(categories
	 * != null) { processCategories(categories,symptoms); } List<SectionModel>
	 * sections = symptomGroup.getSections(); if(sections != null) { for
	 * (SectionModel sectionModel : sections) { if(sectionModel != null ) {
	 * List<CategoryModel> categories1= sectionModel.getCategories(); if(categories1
	 * != null) { processCategories(categories1,symptoms); }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * 
	 * 
	 * return symptoms;
	 * 
	 * 
	 * }
	 * 
	 * 
	 */

	/*
	 * private void processCategories(List<CategoryModel> categories, List<String>
	 * symptoms) {
	 * 
	 * for (CategoryModel categoryModel : categories) {
	 * 
	 * if(categoryModel != null) {
	 * 
	 * List<Symptoms> modelSymptoms = categoryModel.getSymptoms();
	 * 
	 * 
	 * 
	 * if(modelSymptoms != null) {
	 * symptoms.addAll(modelSymptoms.stream().map(Symptoms::getSymptomID).collect(
	 * Collectors.toList()));
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * 
	 * }
	 */

	/*
	 * private void mergeIllness(IllnessModel illnessModel) { Illness dbIllness =
	 * illnessRepository.findByIcd10Code(illnessModel.getIdIcd10Code().toUpperCase()
	 * ); Illness illness = illnessRequestMapper.prepareIllness(illnessModel);
	 * if(dbIllness == null) { Set<Integer> versions = new HashSet<Integer>();
	 * versions.add(illnessModel.getVersion()); illness.setVersion(null);
	 * illness.setState("FINAL"); illness.setMergeCount(1);
	 * illness.setMergedVersions(versions); dbIllness =
	 * illnessRepository.save(illness); } else{ logger.
	 * info("Illness reocrd already available with the Final state. Need to merge with approved record....... "
	 * ); logger.info("Final record id number "+ dbIllness.getId());
	 * illnessVersionMergeMapper.mergeIllnessData(dbIllness,illness,
	 * symptomTemplateRepository); illnessRepository.save(dbIllness);
	 * 
	 * }
	 * 
	 * 
	 * }
	 */
	/**
	 * 
	 * @param illness
	 */
	// @Transactional
	private void deleteIllnessData(List<Long> ids) {

		if (ids != null && !ids.isEmpty()) {
			illnessRepository.deleteIllnessDataModifier(ids);
			illnessRepository.deleteIllnesDataStoreSources(ids);
			illnessRepository.deleteIllnessToDataStore(ids);
			illnessRepository.deleteIllnessToSymptom(ids);
			illnessRepository.deleteIllnessToCategory(ids);
			illnessRepository.deleteIllness(ids);
		}

	}

	@Override
	public IllnessUserData getIllnessesForGroups() {
		IllnessUserData userData = new IllnessUserData();
		List<IllnessModel> illnesses = new ArrayList<IllnessModel>();
		Iterable<User> users = userRepository.findAll(8);
		Iterable<SymptomGroup> groups = symptomGroupRepository.findAll(1);
		Set<Section> dbSection = sectionRepository.getPainSwellingSection();
		if (users != null) {
			Iterator<User> usrsItr = users.iterator();
			while (usrsItr.hasNext()) {
				User user = usrsItr.next();
				if (user != null) {
					Set<Illness> dbIllnesse = user.getIllnesses();
					if (dbIllnesse != null && !dbIllnesse.isEmpty()) {
						Iterator<Illness> dbIllnessItr = dbIllnesse.iterator();
						while (dbIllnessItr.hasNext()) {
							Illness dbIllness = dbIllnessItr.next();
							IllnessModel modeIillness = illnessResponseMapper.createIllnessModelByGroup(dbIllness,
									groups, dbSection);
							modeIillness.setUserID(user.getUserID());
							illnesses.add(modeIillness);
						}

					}
				}
			}
			userData.setUserData(illnesses);

		}

		return userData;

	}

	@Override
	public List<IllnessModel> getIllnessesBySymptomsIDs(List<String> symptomIDs) {
		Iterable<Illness> illnessList = illnessRepository.findIllnessBySymptomsID(symptomIDs);
		return illnessResponseMapper.getIllnessFromItr(illnessList);
	}

	@Override
	@Transactional
	public IllnessStatusModel illnessStatusUpdate(IllnessStatusModel illnessStatusModel)
			throws MICAApplicationException {

		ObjectMapper mapper = new ObjectMapper();
		try {
			logger.info(" Respone from AnglurJS App :::"
					+ mapper.writerWithDefaultPrettyPrinter().writeValueAsString(illnessStatusModel));
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}

		IllnessStatusModel responseModel = new IllnessStatusModel();
		Set<Illness> illnesses = new HashSet<Illness>();
		Set<String> icdCodes = new HashSet<String>();
		responseModel.setStatus("No records illness found to update");

		Boolean flag = false;

		Set<String> targetIllnessCodes = checktargetState(illnessStatusModel);
		if (targetIllnessCodes != null && !targetIllnessCodes.isEmpty()) {
			responseModel.setStatus("Illnesses already exists with target state :: " + targetIllnessCodes);
			logger.info("Illnesses already exists with target state :: " + targetIllnessCodes);
			responseModel.getIcd10CodesStatus().addAll(targetIllnessCodes);
			return responseModel;
		}

		try {
			List<String> icd10Codes = illnessStatusModel.getIcd10Codes();
			for (int i = 0; i < icd10Codes.size(); i++) {
				String icdCode = icd10Codes.get(i);
				Iterable<Illness> dbFromIllnesses = illnessRepository
						.findByIllnessStatusAndICD10Code(illnessStatusModel.getFromState(), icdCode);
				if (dbFromIllnesses != null) {
					illnessRepository.deleteIllness(illnessStatusModel.getFromState(), icdCode);
					Iterator<Illness> dbIllnessItr = dbFromIllnesses.iterator();
					while (dbIllnessItr.hasNext()) {
						Illness dbillness = dbIllnessItr.next();
						dbillness.setState(illnessStatusModel.getToState());
						if (illnessStatusModel.getRejectionReason() != null) {
							dbillness.setRejectionReason(illnessStatusModel.getRejectionReason());
						}
						illnesses.add(dbillness);
						icdCodes.add(icdCode);

					}
				}
			}
			User dbUser = userRepository.findByUserID(illnessStatusModel.getUserID(), 1);
			{
				if (!illnesses.isEmpty()) {
					if (dbUser == null) {
						dbUser = new User();
						dbUser.setUserID(illnessStatusModel.getUserID());
						dbUser.setIllnesses(illnesses);
						flag = true;
					} else {
						if (dbUser.getIllnesses() != null && !dbUser.getIllnesses().isEmpty()) {
							dbUser.getIllnesses().addAll(illnesses);
							flag = true;
						} else {
							dbUser.setIllnesses(illnesses);
							flag = true;
						}
					}
				}
				if (flag) {
					userRepository.save(dbUser);
					userRepository.deleteUser(); // Delete disconnected users from the database.
					responseModel.setCount(icdCodes.size());
					responseModel.getIcd10CodesStatus().addAll(icdCodes);
					responseModel.setStatus("User assigned to illnessess successfully..");
					logger.info("User assigned to illnessess successfully.." + icdCodes);
				}

			}
		} catch (Exception e) {

			throw new MICAApplicationException(e.getMessage());
		}
		return responseModel;

	}

	private Set<String> checktargetState(IllnessStatusModel illnessStatusModel) {
		List<String> icd10Codes = new ArrayList<String>(illnessStatusModel.getIcd10Codes());
		Set<String> icdCodes = new HashSet<String>();
		for (int i = 0; i < icd10Codes.size(); i++) {
			String icdCode = icd10Codes.get(i);
			Iterable<Illness> dbFromIllnesses = illnessRepository
					.findByIllnessStatusAndICD10Code(illnessStatusModel.getToState(), icdCode);
			if (dbFromIllnesses != null) {
				Iterator<Illness> dbIllnessItr = dbFromIllnesses.iterator();
				while (dbIllnessItr.hasNext()) {
					Illness dbIllness = dbIllnessItr.next();
					if (dbIllness != null) {
						icdCodes.add(icdCode);
					}
				}
			}
		}
		return icdCodes;

	}

	/*
	 * private Map<String,
	 * List<Integer>>checktargetStateForIllness(List<IllnessStatusModel> listModel)
	 * { // List<String> icd10Codes = illnessStatusModel.getIdIcd10Codes();
	 * Map<String, List<Integer>> idIcd10CodesVersion=null;
	 * 
	 * 
	 * if(listModel != null && ! listModel.isEmpty()) { idIcd10CodesVersion= new
	 * Hashtable<String, List<Integer>>(); for (int i = 0; i < listModel.size();
	 * i++) { IllnessStatusModel illnessStatusModel = listModel.get(i);
	 * Iterable<Illness> dbFromIllnesses = testDataFrameRepository
	 * .findIllnessByStatusAndICD10CodeAndVersion( illnessStatusModel.getToState(),
	 * illnessStatusModel.getIcd10Code(),illnessStatusModel.getVersion()); if
	 * (dbFromIllnesses != null) { Iterator<Illness> dbIllnessItr =
	 * dbFromIllnesses.iterator(); while(dbIllnessItr.hasNext()){ Illness dbIllness
	 * = dbIllnessItr.next(); if(dbIllness !=null ) { if(!
	 * idIcd10CodesVersion.isEmpty()){ List<Integer> versionList =
	 * idIcd10CodesVersion.get(illnessStatusModel.getIcd10Code());
	 * versionList.add(illnessStatusModel.getVersion());
	 * idIcd10CodesVersion.put(illnessStatusModel.getIcd10Code(), versionList);
	 * 
	 * } else{ List<Integer> versionList = new ArrayList<Integer>();
	 * versionList.add(illnessStatusModel.getVersion());
	 * idIcd10CodesVersion.put(illnessStatusModel.getIcd10Code(), versionList); }
	 * 
	 * 
	 * } } } } } return idIcd10CodesVersion;
	 * 
	 * }
	 */

	/*
	 * private Map<String,
	 * List<Integer>>checkIllnessVersions(List<IllnessStatusModel> listModel) { //
	 * List<String> icd10Codes = illnessStatusModel.getIdIcd10Codes(); Map<String,
	 * List<Integer>> idIcd10CodesVersion=null;
	 * 
	 * 
	 * if(listModel != null && ! listModel.isEmpty()) { idIcd10CodesVersion= new
	 * Hashtable<String, List<Integer>>(); for (int i = 0; i < listModel.size();
	 * i++) { IllnessStatusModel illnessStatusModel = listModel.get(i);
	 * Iterable<Illness> dbFromIllnesses = illnessRepository
	 * .findIllnessByICD10CodeAndVersion(illnessStatusModel.getIcd10Code(),
	 * illnessStatusModel.getVersion()); if (dbFromIllnesses != null) {
	 * Iterator<Illness> dbIllnessItr = dbFromIllnesses.iterator();
	 * while(dbIllnessItr.hasNext()){ Illness dbIllness = dbIllnessItr.next();
	 * if(dbIllness !=null ) { if(! idIcd10CodesVersion.isEmpty()){ List<Integer>
	 * versionList = idIcd10CodesVersion.get(illnessStatusModel.getIcd10Code());
	 * if(versionList == null ) { versionList = new ArrayList<Integer>(); }
	 * versionList.add(illnessStatusModel.getVersion());
	 * idIcd10CodesVersion.put(illnessStatusModel.getIcd10Code(), versionList);
	 * 
	 * } else{ List<Integer> versionList = new ArrayList<Integer>();
	 * versionList.add(illnessStatusModel.getVersion());
	 * idIcd10CodesVersion.put(illnessStatusModel.getIcd10Code(), versionList); }
	 * 
	 * 
	 * } } } } } return idIcd10CodesVersion;
	 * 
	 * }
	 */

	@Override
	public void getIllnessesDeleteBycode(Long id) {
		List<Long> ids = new ArrayList<Long>();
		ids.add(id);
		deleteIllnessData(ids);

	}

	// No threading

	/*
	 * public IllnessUserData findByIllnessByUserAndStatus( IllnessStatusModel
	 * illnessStatusModel) { Iterable<SymptomGroup> groups =
	 * symptomGroupRepository.findAll(1); List<IllnessModel> illnesses= new
	 * ArrayList<IllnessModel>(); IllnessUserData userData = new IllnessUserData();
	 * Iterable<Illness> dbIllnesse =
	 * illnessRepository.getIllnessDataByUserIcd10CodeAndState(illnessStatusModel.
	 * getUserID(), illnessStatusModel.getState());
	 * 
	 * if(dbIllnesse != null ){ Iterator<Illness> dbIllnessItr =
	 * dbIllnesse.iterator(); while(dbIllnessItr.hasNext()) { Illness dbIllness =
	 * dbIllnessItr.next(); if(dbIllness !=null) { IllnessModel modeIillness =
	 * illnessResponseMapper.createIllnessModelByGroup(dbIllness,groups,
	 * sectionRepository); modeIillness.setUserID(illnessStatusModel.getUserID());
	 * illnesses.add(modeIillness); } }
	 * 
	 * } userData.setUserData(illnesses); return userData; }
	 */

	/**
	 * With Multithreading
	 */
	public IllnessUserData findByIllnessByUserAndStatus(IllnessStatusModel illnessStatusModel) {
		Iterable<SymptomGroup> groups = symptomGroupRepository.findAll(1);
		List<IllnessModel> m_illnesses = new ArrayList<IllnessModel>();
		List<Callable<IllnessModel>> callableTasks = new ArrayList<Callable<IllnessModel>>();
		IllnessUserData userData = new IllnessUserData();
		List<Illness> dbIllnesses = illnessRepository
				.getIllnessDataByUserIcd10CodeAndState(illnessStatusModel.getUserID(), illnessStatusModel.getState());
		if (dbIllnesses != null && !dbIllnesses.isEmpty()) {
			Set<Section> dbSection = sectionRepository.getPainSwellingSection();
			ExecutorService exec = Executors.newFixedThreadPool(dbIllnesses.size());
			for (Illness dbIllness : dbIllnesses) {
				Callable<IllnessModel> callable = (() -> {
					// Perform some computation
					// Thread.sleep(2000);
					// IllnessModel modeIillness =
					// illnessResponseMapper.createIllnessModelByGroup(dbIllness,groups,sectionRepository);
					IllnessModel modeIillness = illnessResponseMapper.createIllnessModelByGroup(dbIllness, groups,
							dbSection);
					modeIillness.setUserID(illnessStatusModel.getUserID());
					return modeIillness;
				});

				callableTasks.add(callable);
			}
			try {
				Collection<Future<IllnessModel>> futures = exec.invokeAll(callableTasks);
				for (Future<IllnessModel> f : futures) {
					if (f.isDone()) {
						m_illnesses.add(f.get());
					}
				}
				exec.shutdown();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		userData.setUserData(m_illnesses);
		return userData;
	}

	@Override
	public void deleteSymptomByID(Long id) {
		Symptom dbsymptom = symptomRepository.findById(id, 3).get();
		Set<SymptomDataStore> dataStores = dbsymptom.getSymptomDataStore();
		symptomRepository.delete(dbsymptom);
		if (dataStores != null && !dataStores.isEmpty()) {
			Iterator<SymptomDataStore> dataStoreItr = dataStores.iterator();
			while (dataStoreItr.hasNext()) {
				SymptomDataStore dbDataStore = dataStoreItr.next();
				Set<DataStoreModifier> dbModifiers = dbDataStore.getModifierValues();
				symptomDataStoreRepository.delete(dbDataStore);
				if (dbModifiers != null) {
					dataStoreModifierRepository.deleteAll(dbModifiers);

				}
			}

		}
	}

	@Override
	public IllnessStatusModel deleteIllnessesByIDS(IllnessStatusModel illnessStatusModel)
			throws MICAApplicationException {
		// TODO Auto-generated method stub
		IllnessStatusModel responseModel = new IllnessStatusModel();
		List<Long> ids = illnessStatusModel.getIds();
		deleteIllnessData(ids);
		responseModel.setIds(ids);
		/*
		 * for (int i = 0; i < ids.size(); i++) { Illness dbIllness =
		 * testDataFrameRepository.findOne(ids.get(i)); if(dbIllness != null ) {
		 * deleteIllnessData(dbIllness.getId()); //
		 * ecwIllnessRepository.delete(dbIllness);
		 * responseModel.getIds().add(ids.get(i)); } logger.info(ids.get(i) +
		 * " has been deleted....." );
		 * 
		 * }
		 */
		responseModel.setStatus("Deleted successfully.");
		return responseModel;
	}

	@Override
	public IllnessStatusModel cloneIllness(IllnessStatusModel illnessStatusModel) throws MICAApplicationException {
		Set<Illness> saveIllness = new HashSet<Illness>();

		Iterable<Illness> dbIllnesse = illnessRepository.getIllnessFromIDs(illnessStatusModel.getIds());
		if (dbIllnesse != null) {
			Iterator<Illness> dbIllnessItr = dbIllnesse.iterator();
			while (dbIllnessItr.hasNext()) {
				Illness dbIllness = dbIllnessItr.next();
				if (dbIllness != null) {
					Illness dbNewIllness = illnessRequestMapper.cloneIllness(dbIllness, illnessStatusModel);

					saveIllness.add(dbNewIllness);
				}

				Integer userId = illnessStatusModel.getUserID();
				if (userId != null) {
					User user = userRepository.findByUserID(userId, 1);

					if (user.getIllnesses() != null) {
						user.getIllnesses().addAll(saveIllness);
					} else {
						user.setIllnesses(saveIllness);
					}
					userRepository.save(user);
				} else {
					illnessRepository.saveAll(saveIllness);
				}
			}
		}
		return illnessStatusModel;

	}

	@Override
	public ICD10CodesModel getIllnessFromICDCodes(IllnessStatusModel illnessStatusModel) {
		ICD10CodesModel illnessData;
		List<String> icdCodes = illnessStatusModel.getIcd10Codes();
		Iterable<Illness> dbIllness = null;
		if (icdCodes != null && icdCodes.size() > 0) {
			List<String> icd10Upper = icdCodes.stream().map(String::toUpperCase).collect(Collectors.toList());
			dbIllness = illnessRepository.getIllnessesForGivenICDcodes(icd10Upper);
		} else {
			dbIllness = illnessRepository.getIllnesses();
		}
		illnessData = illnessResponseMapper.createICD10CodeMapper(dbIllness);
		return illnessData;
	}

	/**
	 * 
	 */
	@Override
	public ICD10CodeModel updateIllnessRecord(ICD10CodeModel iCD10CodeModel) {
		ICD10CodeModel updateRecord = new ICD10CodeModel();
		String icdCode = iCD10CodeModel.getIcd10Code();
		Integer crticality = iCD10CodeModel.getCriticality();
		Double prior = iCD10CodeModel.getPrior();
		if (icdCode != null) {
			Set<Illness> updateIllness = new HashSet<Illness>();
			Set<Illness> dbIllnesses = illnessRepository.findIllnessByCode(icdCode, iCD10CodeModel.getVersion());

			if (dbIllnesses != null && !dbIllnesses.isEmpty()) {
				Iterator<Illness> dbIllnessItr = dbIllnesses.iterator();
				while (dbIllnessItr.hasNext()) {
					Illness dbIllness = dbIllnessItr.next();
					if (crticality != null) {
						dbIllness.setCriticality(crticality);
					}
					if (prior != null) {
						dbIllness.setPrior(prior);
					}

					updateIllness.add(dbIllness);
				}

			}
			Iterable<Illness> dbIllness = illnessRepository.save(updateIllness, 1);
			if (dbIllness != null) {
				updateRecord.setIcd10Code(iCD10CodeModel.getIcd10Code());
				updateRecord.setStatus("Illness updated..");
			} else {
				updateRecord.setIcd10Code(iCD10CodeModel.getIcd10Code());
				updateRecord.setStatus("Illness record not found..");
			}
		}

		// TODO Auto-generated method stub
		return updateRecord;
	}

	@Override
	public void deleteBySymptomID(String symptomID) {
		Set<Symptom> symptoms = symptomRepository.findByCode(symptomID);
		if (symptoms != null) {
			Iterator<Symptom> symptomItr = symptoms.iterator();
			while (symptomItr.hasNext()) {
				Symptom dbSymptom = symptomItr.next();
				deleteSymptomByID(dbSymptom.getId());
			}

		}

	}

	@Override
	public IllnessUserData getIllnessByIcd10Code(String icd10Code, String state) {
		Iterable<SymptomGroup> groups = symptomGroupRepository.findAll(1);
		List<IllnessModel> illnesses = new ArrayList<IllnessModel>();
		IllnessUserData userData = new IllnessUserData();
		List<String> states = new ArrayList<String>();

		if (state != null) {
			states.add(StringUtils.upperCase(state));
		} else {
			states.add(MICAConstants.APPROVED);
			states.add(MICAConstants.COMPLETE);
			states.add(MICAConstants.READYFORREVIEW);
		}

		Iterable<Illness> dbIllnesse = illnessRepository
				.getIllnessDataByIcd10CodeAndState(StringUtils.upperCase(icd10Code), states);
		Set<Section> dbSection = sectionRepository.getPainSwellingSection();
		if (dbIllnesse != null) {
			Iterator<Illness> dbIllnessItr = dbIllnesse.iterator();
			while (dbIllnessItr.hasNext()) {
				Illness dbIllness = dbIllnessItr.next();
				if (dbIllness != null) {
					IllnessModel modeIillness = illnessResponseMapper.createIllnessModelByGroup(dbIllness, groups,
							dbSection);
					illnesses.add(modeIillness);
				}
			}

		}
		userData.setUserData(illnesses);
		return userData;
	}

	@Override
	public IllnessStatusModel illnessStatusUpdateWithVersion(List<IllnessStatusModel> illnessList)
			throws MICAApplicationException {

		ObjectMapper mapper = new ObjectMapper();
		try {
			logger.info(" Respone from AnglurJS App :::"
					+ mapper.writerWithDefaultPrettyPrinter().writeValueAsString(illnessList));
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		IllnessStatusModel responseModel = new IllnessStatusModel();
		Set<Illness> illnesses = new HashSet<Illness>();
		Set<String> icdCodes = new HashSet<String>();
		Map<String, List<Integer>> idIcd10CodesVersion = new Hashtable<String, List<Integer>>();

		responseModel.setStatus("No records illness found to update");
		// Integer userId = null;

		Boolean flag = false;

		// Map<String, List<Integer>> targetIllnessCodes =
		// checkIllnessVersions(illnessList);

		// Map<String, List<Integer>> targetIllnessCodes =
		// checktargetStateForIllness(illnessList);

		/*
		 * if(targetIllnessCodes !=null && !targetIllnessCodes.isEmpty() &&
		 * targetIllnessCodes.size() > 1){
		 * responseModel.setStatus("Illnesses already exists with same status :: "+
		 * targetIllnessCodes);
		 * logger.info("Illnesses already exists with same status :: " +
		 * targetIllnessCodes); responseModel.setIcd10CodesVersion(targetIllnessCodes);
		 * return responseModel; } else{
		 * responseModel.setIcd10CodesVersion(targetIllnessCodes); }
		 */
		try {
			// List<String> icd10Codes = illnessStatusModel.getIdIcd10Codes();
			for (int i = 0; i < illnessList.size(); i++) {
				List<String> states = new ArrayList<String>();
				IllnessStatusModel illnessStatusModel = illnessList.get(i);

				states.add(illnessStatusModel.getFromState());
				states.add(illnessStatusModel.getToState());

				String icdCode = illnessStatusModel.getIcd10Code();
				Iterable<Illness> dbFromIllnesses = illnessRepository.findIllnessByStatusAndICD10CodeAndVersion(states,
						icdCode, illnessStatusModel.getVersion());
				if (dbFromIllnesses != null) {

					List<Integer> users = illnessRepository.findUserByStatusAndICD10CodeAndVersion(states, icdCode,
							illnessStatusModel.getVersion());

					illnessRepository.deleteIllnessRelation(states, icdCode, illnessStatusModel.getVersion());

					Iterator<Illness> dbIllnessItr = dbFromIllnesses.iterator();
					while (dbIllnessItr.hasNext()) {
						Illness dbillness = dbIllnessItr.next();
						dbillness.setState(illnessStatusModel.getToState());

						// to retain the borrowed illness in the dataframe, dfstatus will be used in
						// dataframe to pull all the approved records.

						if (illnessStatusModel.getFromState().toUpperCase().equals(MICAConstants.APPROVED)
								&& illnessStatusModel.getToState().toUpperCase().equals(MICAConstants.READYFORREVIEW)) {
							dbillness.setDfstatus(illnessStatusModel.getFromState());
						}

						if (users != null && !users.isEmpty()) {
							dbillness.setOldUserID(users.get(0));
						}
						// dbillness.setOldUserID(user);
						if (illnessStatusModel.getRejectionReason() != null) {
							dbillness.setRejectionReason(illnessStatusModel.getRejectionReason());
						}
						illnesses.add(dbillness);
						if (!idIcd10CodesVersion.isEmpty()) {
							List<Integer> versionList = idIcd10CodesVersion.get(illnessStatusModel.getIcd10Code());
							if (versionList == null) {
								versionList = new ArrayList<Integer>();
							}
							versionList.add(illnessStatusModel.getVersion());
							idIcd10CodesVersion.put(illnessStatusModel.getIcd10Code(), versionList);
						} else {
							List<Integer> versionList = new ArrayList<Integer>();
							versionList.add(illnessStatusModel.getVersion());
							idIcd10CodesVersion.put(illnessStatusModel.getIcd10Code(), versionList);
						}

					}
				}
			}
			User dbUser = userRepository.findByUserID(illnessList.get(0).getUserID(), 1);

			if (!illnesses.isEmpty()) {
				if (dbUser == null) {
					dbUser = new User();
					dbUser.setUserID(illnessList.get(0).getUserID());
					dbUser.setIllnesses(illnesses);
					flag = true;
				} else {
					if (dbUser.getIllnesses() != null && !dbUser.getIllnesses().isEmpty()) {
						dbUser.getIllnesses().addAll(illnesses);
						flag = true;
					} else {
						dbUser.setIllnesses(illnesses);
						flag = true;
					}
				}
			}
			if (flag) {
				// userRepository.save(dbUser, 6);
				userRepository.save(dbUser);
				userRepository.deleteUser(); // Delete disconnected users from the database.
				responseModel.setCount(idIcd10CodesVersion.size());
				responseModel.setIcd10CodesVersion(idIcd10CodesVersion);
				responseModel.setStatus("User assigned to illnessess successfully..");
				logger.info("User assigned to illnessess successfully.." + icdCodes);

			}

		} catch (Exception e) {
			logger.error("ERROR", e);
			throw new MICAApplicationException(e.getMessage());
		}
		// responseModel.setCount(idIcd10CodesVersion.size());

		return responseModel;
	}

	/*
	 * @Override public void updateSymptomRank() { String groupID ="physical";
	 * Iterable<Map<String, Object>> dbResults =
	 * ecwIllnessRepository.getSymptomRanksForGroups(groupID); for (Map<String,
	 * Object> row : dbResults) { String symptomID = (String) row.get("symptomID");
	 * Integer count = (Integer) row.get("count") ;
	 * symptomTemplateRepository.updateDisplayOrder(symptomID, count); }
	 * 
	 * }
	 */

	@Override
	public Iterable<BadIllness> getInvalidIllnesses() {
		// TODO Auto-generated method stub
		return badIllnessRepository.findAll(3);
	}

	@Override
	public IllnessStatusModel updateRollebackUser(List<IllnessStatusModel> illnessList)
			throws MICAApplicationException {
		return null;
	}

	private Boolean mitaUserStateChange(MitaValueObject cvObj, String userType, String status) {

		RestTemplate restTemplate = new RestTemplate();

		// and do I need this JSON media type for my use case?
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		/*
		 * MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new
		 * MappingJackson2HttpMessageConverter();
		 * mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(
		 * MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
		 * restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
		 */

		Boolean responseBool = null;
		try {
			StringBuffer base_url = new StringBuffer(mitaBaseUrl).append("/RESTfulIllness/mica/state/").append(userType)
					.append("/").append(status);
			logger.info("Mita URL :: " + base_url);
			if (cvObj.getCollector() != null) {
				logger.info("cvObj.getCollector() != null " + cvObj.getCollector());
				// responseBool = restTemplate.postForObject(base_url.toString(),
				// cvObj.getCollector(), Boolean.class);
				HttpEntity<Object> entity = new HttpEntity<Object>(cvObj.getCollector(), headers);
				ResponseEntity<Boolean> response = restTemplate.exchange(base_url.toString(), HttpMethod.POST, entity,
						Boolean.class);
				logger.info("Collector Mita Response :: http code:" + response.getStatusCode() + " response:"
						+ response.getBody());
				responseBool = response.getBody();
			}
			if (cvObj.getReviewer() != null) {
				logger.info("cvObj.getReviewer() != null " + cvObj.getReviewer());
				// responseBool = restTemplate.postForObject(base_url.toString(),
				// cvObj.getReviewer(), Boolean.class);
				HttpEntity<Object> entity = new HttpEntity<Object>(cvObj.getReviewer(), headers);
				ResponseEntity<Boolean> response = restTemplate.exchange(base_url.toString(), HttpMethod.POST, entity,
						Boolean.class);
				logger.info("Reviewer Mita Response :: http code:" + response.getStatusCode() + " response:"
						+ response.getBody());
				responseBool = response.getBody();
			}
		} catch (HttpServerErrorException e) {
			throw new ServiceNotAvailableException("MITA services are down: " + e);
		}
		logger.info("Mita Response :: " + responseBool);
		return responseBool;
	}

	@Override
	public Map<String, List<Integer>> findByicd10CodeByUserAndStatus(Integer userID, String state) {
		Iterable<Illness> dbIllnesses = illnessRepository.findByicd10CodeByUserAndStatus(userID, state);
		Map<String, List<Integer>> idIcd10CodesVersion = new Hashtable<String, List<Integer>>();
		for (Illness illness : dbIllnesses) {
			List<Integer> versionList = idIcd10CodesVersion.get(illness.getIcd10Code());
			if (versionList == null) {
				versionList = new ArrayList<Integer>();
			}
			versionList.add(illness.getVersion());
			idIcd10CodesVersion.put(illness.getIcd10Code(), versionList);
		}
		return idIcd10CodesVersion;
	}

	@Override
	public List<UserICD10CodeModel> getIllnessInformation(Integer userID, String source) {
		List<UserICD10CodeModel> icd10DataModel = new ArrayList<UserICD10CodeModel>();
		Set<User> userInfo = new HashSet<User>();

		if (userID != null) {
			userInfo = userRepository.findIllnessInfoByUserAndSource(userID, source);
		} else {
			userInfo = userRepository.findIllnessInfoBysource(source);
		}

		if (userInfo != null) {
			icd10DataModel = illnessResponseMapper.createICD10CodeMapperWithUser(userInfo);
		}
		return icd10DataModel;
	}

	/**
	 * 
	 */
	@Override
	public List<String> getUniqueIllnesses(String icd10Code, Boolean time, Integer version) {

		List<String> icd10Codes = new ArrayList<String>();

		List<IllnessDataQueryResultEnitity> targetIllnesses = illnessRepository.getIllnessDataForGivenIcd10(icd10Code,
				version);

		List<IllnessDataQueryResultEnitity> illnessData = null;

		if (targetIllnesses != null && !targetIllnesses.isEmpty()) {
			List<String> targetSymptoms = targetIllnesses.stream().map(IllnessDataQueryResultEnitity::getSymptomID)
					.collect(Collectors.toList());

			if (targetSymptoms != null && !targetSymptoms.isEmpty()) {
				illnessData = illnessRepository.getUniqueApprovedIllnesses();
			}
		}

		if (illnessData != null && !illnessData.isEmpty()) {
			icd10Codes = getIdenticalIllnesses(targetIllnesses, illnessData, time);
		}
		return icd10Codes;
	}

	private List<String> getIdenticalIllnesses(List<IllnessDataQueryResultEnitity> targetIllnesses,
			List<IllnessDataQueryResultEnitity> illnessData, Boolean time) {

		List<String> icd10Codes = new ArrayList<String>();

		Map<String, List<Integer>> indenticalTargert = null;
		Map<String, List<Integer>> indenticalillnessData = null;

		if (time != null && time.booleanValue()) {
			indenticalTargert = getIdenticalSymptomsWithTime(targetIllnesses);
			indenticalillnessData = getIdenticalSymptomsWithTime(illnessData);
		} else {
			indenticalTargert = getIdenticalSymptomsWithOutTime(targetIllnesses);
			indenticalillnessData = getIdenticalSymptomsWithOutTime(illnessData);
		}

		if (indenticalTargert != null) {
			Entry<String, List<Integer>> entry = indenticalTargert.entrySet().iterator().next();
			// String key = entry.getKey();
			List<Integer> targetListKeys = entry.getValue();
			// logger.info(key + " ::: Targer Illnesses "+ targetListKeys );

			if (targetListKeys != null && !targetListKeys.isEmpty()) {
				Collections.sort(targetListKeys);
				indenticalillnessData.entrySet().forEach(illnessDataentry -> {
					String icd10Code = illnessDataentry.getKey();
					List<Integer> sourceListKeys = illnessDataentry.getValue();
					if (sourceListKeys != null && !sourceListKeys.isEmpty()) {
						// logger.info(icd10Code + " ::: Source Illnesses "+ sourceListKeys );
						Collections.sort(sourceListKeys);
						if (Objects.equals(targetListKeys, sourceListKeys)) {
							icd10Codes.add(icd10Code);
						}
					}
				});
			}
		}

		return icd10Codes;
	}

	/*
	 * private void calculateTime(IllnessDataQueryResultEnitity symptomData) { Long
	 * common= 0L; String timeUnit = symptomData.getTimeUnit();
	 * if(timeUnit.equalsIgnoreCase("hours")) { common =
	 * secondsConvertionMap.get(MICAUtil.HOURS); }else if
	 * (timeUnit.equalsIgnoreCase("days")) { common =
	 * secondsConvertionMap.get(MICAUtil.DAYS); }else if
	 * (timeUnit.equalsIgnoreCase("weeks")) { common =
	 * secondsConvertionMap.get(MICAUtil.WEEKS);
	 * 
	 * }else if (timeUnit.equalsIgnoreCase("months")) { common =
	 * secondsConvertionMap.get(MICAUtil.MONTHS);
	 * 
	 * }else if (timeUnit.equalsIgnoreCase("years")) { if(start > 0) { start = start
	 * -1; } common = secondsConvertionMap.get(MICAUtil.YEARS); }
	 * 
	 * symptomData.setFinalTimeValue((common*symptomData.getTimeValue())-(common*
	 * symptomData.getTimeStart()));
	 * 
	 * }
	 */

	/**
	 * This method returns the unique record for each duplicate records.
	 * 
	 * @param illnessData
	 * @return
	 */
	private Map<String, List<Integer>> getIdenticalSymptomsWithTime(List<IllnessDataQueryResultEnitity> illnessData) {

		Map<String, List<Integer>> identicalSymptoms = new Hashtable<String, List<Integer>>();
		for (int i = 0; i < illnessData.size(); i++) {
			IllnessDataQueryResultEnitity illness = illnessData.get(i);

			/*
			 * if(illness.getTimeStart() != null && illness.getTimeValue() != null &&
			 * illness.getTimeUnit() != null ) { calculateTime(illness); }
			 */

			List<Integer> symptomPatterns = identicalSymptoms.get(illness.getIcd10Code());
			if (symptomPatterns == null) {
				symptomPatterns = new ArrayList<Integer>();
			}
			symptomPatterns.add(Objects.hash(illness.symptomID, illness.dsLikelihood, illness.dmLikelihood,
					illness.multiplier, illness.bodyParts, illness, illness.timeFrame));
			identicalSymptoms.put(illness.getIcd10Code(), symptomPatterns);

		}

		return identicalSymptoms;
	}

	/**
	 * This method returns the unique record for each duplicate records.
	 * 
	 * @param illnessData
	 * @return
	 */
	private Map<String, List<Integer>> getIdenticalSymptomsWithOutTime(
			List<IllnessDataQueryResultEnitity> illnessData) {

		Map<String, List<Integer>> identicalSymptoms = new Hashtable<String, List<Integer>>();
		for (int i = 0; i < illnessData.size(); i++) {
			IllnessDataQueryResultEnitity illness = illnessData.get(i);

			List<Integer> symptomPatterns = identicalSymptoms.get(illness.getIcd10Code());
			if (symptomPatterns == null) {
				symptomPatterns = new ArrayList<Integer>();
			}
			symptomPatterns.add(Objects.hash(illness.symptomID, illness.dsLikelihood, illness.dmLikelihood,
					illness.multiplier, illness.bodyParts));
			identicalSymptoms.put(illness.getIcd10Code(), symptomPatterns);

		}

		return identicalSymptoms;
	}

	public static <T> Set<T> getCommonElements(Collection<? extends Collection<T>> collections) {

		Set<T> common = new LinkedHashSet<T>();
		if (!collections.isEmpty()) {
			Iterator<? extends Collection<T>> iterator = collections.iterator();
			common.addAll(iterator.next());
			while (iterator.hasNext()) {
				common.retainAll(iterator.next());
			}
		}
		return common;
	}

	@Override
	public PaginationModel getIllnessWithPagingForGivenSymptom(Integer page, Integer size, String source,
			String symptomID, String symptomName) {

		if (symptomName != null) {
			if (symptomName.length() < 3) {
				throw new DataInvalidException("Illness name should be greater than 3 characters.");
			}
		}

		if (symptomID != null) {
			if (symptomID.length() < 3) {
				throw new DataInvalidException("Illness code should be greater than 3 characters.");
			}
		}

		PaginationModel illnessPages = new PaginationModel();
		Page<Illness> illnessDbPages = getPagenationContent(illnessPages, page, size, source, symptomID, symptomName);
		if (illnessDbPages != null) {
			List<Illness> slices = illnessDbPages.getContent();
			if (slices != null) {
				List<ICD10CodeModel> content = new ArrayList<ICD10CodeModel>();
				for (Illness slice : slices) {
					String icd10Code = slice.getIcd10Code();
					Integer version = slice.getVersion();
					ICD10CodeModel iCD10CodeModel = new ICD10CodeModel();
					iCD10CodeModel.setIcd10Code(icd10Code);
					iCD10CodeModel.setName(slice.getName());
					iCD10CodeModel.setVersion(version);
					// iCD10CodeModel.setUserID(userRepository.getUserID(source, icd10Code,
					// version));
					content.add(iCD10CodeModel);
				}
				illnessPages.setContent(content);
			}

			illnessPages.setFirst(illnessDbPages.isFirst());
			illnessPages.setLast(illnessDbPages.isLast());
			illnessPages.setPagenumber(illnessDbPages.getNumber());
			illnessPages.setElementsInPage(illnessDbPages.getContent().size());
			illnessPages.setPageSize(illnessDbPages.getSize());
		}

		// TODO Auto-generated method stub
		return illnessPages;
	}

	private Page<Illness> getPagenationContent(PaginationModel illnessPages, Integer page, Integer size, String source,
			String symptomID, String symptomName) {
		Page<Illness> illnessDbPages = null;
		Integer totalRecords = null;

		if (page == null) {
			page = 1;
		}
		if (size == null) {
			size = 20;
		}
		if (source != null) {
			/*
			 * if(symptomID != null && symptomName != null){ PageRequest pageable =
			 * PageRequest.of(page - 1, size, Sort.Direction.ASC, "illness.icd10Code");
			 * illnessDbPages =
			 * illnessRepository.findByIllnessSourceAndSymptomCodeAndName(source.toUpperCase
			 * (),symptomID.toUpperCase(),symptomName.toUpperCase(), pageable); totalRecords
			 * = illnessRepository.findIllnessCountBySourceAndSymptomCodeAndName(source.
			 * toUpperCase(),symptomID.toUpperCase(),symptomName.toUpperCase(), pageable);
			 * 
			 * }
			 */
			if (symptomID != null) {
				PageRequest pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "illness.icd10Code");
				illnessDbPages = illnessRepository.findApprovedIllnessBySymptomCode(source.toUpperCase(),
						symptomID.toUpperCase(), pageable);
				totalRecords = illnessRepository.findApprovedIllnessBySymptomCodeCount(source.toUpperCase(),
						symptomID.toUpperCase());

			} else if (symptomName != null) {
				PageRequest pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "illness.icd10Code");
				List<String> symptoms = symptomTemplateRepository
						.findAllSymptomIDForGivenName(symptomName.toUpperCase());
				illnessDbPages = illnessRepository.findApprovedIllnessBySymptomName(source.toUpperCase(), symptoms,
						pageable);
				totalRecords = illnessRepository.findApprovedIllnessBySymptomNameCount(source.toUpperCase(), symptoms);
			} else {
				PageRequest pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "illness.icd10Code");
				illnessDbPages = illnessRepository.findApprovedIllnessBySource(source, pageable);
				totalRecords = illnessRepository.findApprovedIllnessBySourceCount(source.toUpperCase());
			}
		}

		illnessPages.setTotalElements(totalRecords);
		return illnessDbPages;
	}

	@Override
	public Iterable<Coding_Rules> getDiseaseCodingRules() {
		return illnessRepository.getDiseaseCodingRules();
	}

	@Override
	public Boolean chiefcomplaintcheck(String icd10Code, Integer version) {

		if (icd10Code != null && version != null) {
			Predicate<Boolean> p1 = s -> s.booleanValue() == Boolean.TRUE;
			IllnessDataQueryResultEnitity illnesses = illnessRepository.chiefcomplaintcheck(icd10Code.toUpperCase(),
					version);
			if (illnesses != null) {
				List<Boolean> disPlaysymptoms = illnesses.getDisplaySymptoms();
				if (disPlaysymptoms != null && !disPlaysymptoms.isEmpty()) {
					return disPlaysymptoms.stream().anyMatch(p1);
				}
			}
		}
		return false;
	}

	/**
	 * Returns the list of symptoms with minimal diagnostic creatiria set to true
	 * for all the given illnesses.
	 * 
	 */
	@Override
	public Map<String, List<String>> getMDCSymptomsByIllnesses(List<String> icd10Codes, boolean mdcFlag) {
		Map<String, List<String>> mdcSymptoms = new HashMap<String, List<String>>();
		List<IllnessDataQueryResultEnitity> queryResults = illnessRepository.getMDCSymptomsByIllnesses(icd10Codes,
				mdcFlag);
		for (String icd10Code : icd10Codes) {
			mdcSymptoms.put(icd10Code, null);
			if (queryResults != null && !queryResults.isEmpty()) {
				IllnessDataQueryResultEnitity illnessResult = queryResults.stream()
						.filter(s -> s.getIcd10Code().equalsIgnoreCase(icd10Code)).findAny().orElse(null);
				if (illnessResult != null && illnessResult.getSymptoms() != null
						&& !illnessResult.getSymptoms().isEmpty()) {
					mdcSymptoms.put(icd10Code, illnessResult.getSymptoms());
				}
			}
		}

		return mdcSymptoms;
	}

	@Override
	public Map<String, List<String>> getBiasSymptomsByIllnesses(List<String> icd10Codes, boolean bias) {
		Map<String, List<String>> biasSymptoms = new HashMap<String, List<String>>();
		List<IllnessDataQueryResultEnitity> queryResults = illnessRepository
				.getNegativeBiasSymptomsByIllnesses(icd10Codes, bias);
		for (String icd10Code : icd10Codes) {
			biasSymptoms.put(icd10Code, null);
			if (queryResults != null && !queryResults.isEmpty()) {
				IllnessDataQueryResultEnitity illnessResult = queryResults.stream()
						.filter(s -> s.getIcd10Code().equalsIgnoreCase(icd10Code)).findAny().orElse(null);
				if (illnessResult != null && illnessResult.getSymptoms() != null
						&& !illnessResult.getSymptoms().isEmpty()) {
					biasSymptoms.put(icd10Code, illnessResult.getSymptoms());
				}
			}
		}

		return biasSymptoms;
	}

	@Override
	public List<SymptomSourceInfo> getIllnessSymptomsSources(String icd10code, String state, Integer version) {
		return symptomSourceInfoRepository.getIllnessSymptomsSources(icd10code, state, version);
	}

	@Override
	public List<SymptomSourceInfo> getIllnessSymptomsSourcesForGivenUser(Integer userID, List<String> state) {
		return symptomSourceInfoRepository.getIllnessSymptomsSourcesForGivenUser(userID, state);
	}

	/**
	 * 
	 */
	@Override
	public String updateIllnessSources(IllnessSourcesModel illnessSourcesModel) {

		Collection<Map<String, Object>> selfRemoveWithMultiplier = new ArrayList<Map<String, Object>>();
		Collection<Map<String, Object>> selfRemoveWithNoMultiplier = new ArrayList<Map<String, Object>>();
		Collection<Map<String, Object>> illnessRemove = new ArrayList<Map<String, Object>>();
		Collection<Map<String, Object>> allRemove = new ArrayList<Map<String, Object>>();
		Collection<Map<String, Object>> addSourcesWithMultiplier = new ArrayList<Map<String, Object>>();
		Collection<Map<String, Object>> addSourcesWithNoMultiplier = new ArrayList<Map<String, Object>>();

		if (illnessSourcesModel != null) {
			List<MultiplierSources> sources = illnessSourcesModel.getSymptomSources();
			if (sources != null && !sources.isEmpty()) {
				for (MultiplierSources multiplierSource : sources) {
					List<AuditSourceModel> sourceList = multiplierSource.getSourceInfo();
					if (sourceList != null && !sourceList.isEmpty()) {
						for (AuditSourceModel symptomSourceModel : sourceList) {
							if (symptomSourceModel != null) {
								Integer sourceId = symptomSourceModel.getSourceID();
								String action = symptomSourceModel.getAction();
								if (sourceId != null && action != null) {
									Map<String, Object> sourceInfo = new Hashtable<String, Object>();
									sourceInfo.put("icd10Code", illnessSourcesModel.getIcd10Code());
									sourceInfo.put("version", illnessSourcesModel.getVersion());
									sourceInfo.put("state", illnessSourcesModel.getState());
									sourceInfo.put("symptomID", illnessSourcesModel.getSymptomID());
									sourceInfo.put("sourceID", symptomSourceModel.getSourceID());
									String multiplier = multiplierSource.getMultiplier();

									if (symptomSourceModel.getAction().equalsIgnoreCase("Symptom")) {
										sourceInfo.put("action", "Unmap");
										if (multiplier != null) {
											sourceInfo.put("multiplier", multiplier);
											selfRemoveWithMultiplier.add(sourceInfo);
										} else {
											selfRemoveWithNoMultiplier.add(sourceInfo);
										}
									}

									if (symptomSourceModel.getAction().equalsIgnoreCase("Illness")) {
										sourceInfo.put("action", "Unmap");
										illnessRemove.add(sourceInfo);
									}
									if (symptomSourceModel.getAction().equalsIgnoreCase("All")) {
										sourceInfo.put("action", "Unmap/Deleted");
										allRemove.add(sourceInfo);
									}

									if (symptomSourceModel.getAction().equalsIgnoreCase("Add")) {
										sourceInfo.put("action", "Added");
										if (multiplier != null) {
											sourceInfo.put("multiplier", multiplier);
											addSourcesWithMultiplier.add(sourceInfo);
										} else {
											addSourcesWithNoMultiplier.add(sourceInfo);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (!selfRemoveWithMultiplier.isEmpty()) {
			symptomSourceInfoRepository.unMapSymptomSourcesWithMultipliers(selfRemoveWithMultiplier);
		}
		if (!selfRemoveWithNoMultiplier.isEmpty()) {
			symptomSourceInfoRepository.unMapSymptomSourcesWithNoMultipliers(selfRemoveWithNoMultiplier);
		}

		if (!addSourcesWithMultiplier.isEmpty()) {
			symptomSourceInfoRepository.mapSymptomSourcesWithMultipliers(addSourcesWithMultiplier);
		}

		if (!addSourcesWithNoMultiplier.isEmpty()) {
			symptomSourceInfoRepository.mapSymptomSourcesWithNoMultipliers(addSourcesWithNoMultiplier);
		}

		if (!illnessRemove.isEmpty()) {
			symptomSourceInfoRepository.unMapIllnessSourcesWithMultipliers(illnessRemove);
			symptomSourceInfoRepository.unMapIllnessSourcesWithNoMultipliers(illnessRemove);
		}

		if (!allRemove.isEmpty()) {
			symptomSourceInfoRepository.unMapAllSourcesWithMultipliers(allRemove);
			symptomSourceInfoRepository.unMapAllSourcesWithNoMultipliers(allRemove);

		}

		return "Updated sources";
	}
}
