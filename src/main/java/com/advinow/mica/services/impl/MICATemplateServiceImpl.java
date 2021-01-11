package com.advinow.mica.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.resilience4j.retry.annotation.Retry;

import com.advinow.mica.domain.DataKeys;
import com.advinow.mica.domain.DataStore;
import com.advinow.mica.domain.LogicalSymptomGroups;
import com.advinow.mica.domain.LogicalSymptomGroupsRef;
import com.advinow.mica.domain.RCodeDataStore;
import com.advinow.mica.domain.SnomedCodes;
import com.advinow.mica.domain.Symptom;
import com.advinow.mica.domain.SymptomCategory;
import com.advinow.mica.domain.SymptomGroup;
import com.advinow.mica.domain.SymptomLabOrders;
import com.advinow.mica.domain.SymptomTemplate;
import com.advinow.mica.domain.queryresult.GenericQueryResultEntity;
import com.advinow.mica.domain.queryresult.SymptomGroupResult;
import com.advinow.mica.exception.DataCreateException;
import com.advinow.mica.exception.DataInvalidException;
import com.advinow.mica.exception.DataNotFoundException;
import com.advinow.mica.exception.MICAApplicationException;
import com.advinow.mica.mapper.SymptomGroupRequestMapper;
import com.advinow.mica.mapper.SymptomGroupResponseMapper;
import com.advinow.mica.model.AdtionalSettings;
import com.advinow.mica.model.CategoryModel;
import com.advinow.mica.model.GenericCategoryModel;
import com.advinow.mica.model.GenericSymptomGroups;
import com.advinow.mica.model.GenericSymptomModel;
import com.advinow.mica.model.GenericSymptomPagination;
import com.advinow.mica.model.LogicalSymptomGroupsModel;
import com.advinow.mica.model.MICASymptomsGroup;
import com.advinow.mica.model.PaginationModel;
import com.advinow.mica.model.SnomedCodeModel;
import com.advinow.mica.model.SymptomGroups;
import com.advinow.mica.model.SymptomTemplateDTO;
import com.advinow.mica.model.SymptomTemplateModel;
import com.advinow.mica.model.SymptomTemplateModelV1;
import com.advinow.mica.model.SymptomTemplateValidate;
import com.advinow.mica.model.Symptoms;
import com.advinow.mica.model.SymptomsMainModel;
import com.advinow.mica.model.SymptomsTmplModel;
import com.advinow.mica.model.cache.CacheGroupsTime;
import com.advinow.mica.model.enums.TimeType;
import com.advinow.mica.repositories.DataKeysRepository;
import com.advinow.mica.repositories.DataStoreRepository;
import com.advinow.mica.repositories.LogicalSymptomGroupsRefRepositoty;
import com.advinow.mica.repositories.SymptomCategoryRepository;
import com.advinow.mica.repositories.SymptomGroupRepository;
import com.advinow.mica.repositories.SymptomRepository;
import com.advinow.mica.repositories.SymptomTemplateRepository;
import com.advinow.mica.services.MICACacheService;
import com.advinow.mica.services.MICAIllnessService;
import com.advinow.mica.services.MICATemplateService;
import com.advinow.mica.util.MICAConstants;
import com.advinow.mica.util.MICAUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Service
@Retry(name = "neo4j")
public class MICATemplateServiceImpl implements MICATemplateService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	DataKeysRepository dataKeysRepository;

	@Autowired
	SymptomGroupRepository symptomGroupRepository;

	@Autowired
	SymptomCategoryRepository symptomCategoryRepository;

	@Autowired
	SymptomTemplateRepository symptomTemplateRepository;

	@Autowired
	SymptomRepository symptomRepository;

	@Autowired
	MICAIllnessService mICARestService;

	@Autowired
	DataStoreRepository dataStoreRepository;

	SymptomGroupRequestMapper symptomGroupRequestMapper = new SymptomGroupRequestMapper();

	SymptomGroupResponseMapper symptomGroupResponseMapper = new SymptomGroupResponseMapper();

	@Autowired
	MICACacheService mICACacheService;

	@Value("${server.cache}")
	Boolean serverCache;

	/*
	 * @Value("${snomed.rest.url}") String snomedBaseUrl;
	 */

	@Autowired
	LogicalSymptomGroupsRefRepositoty logicalSymptomGroupsRefRepositoty;

	/*
	 * @Override public SymptomGroups getSymptomsByGroup(String groupID,String
	 * lCode) {
	 * 
	 * 
	 * logger.info("Before calling database" + new Date()); Set<SymptomTemplate>
	 * rCodeSymTemplates = symptomTemplateRepository.getRcodeSymptomTemplates();
	 * Set<DataStore> dataStores = dataStoreRepository.findAllDataStores();
	 * SymptomGroup symptomGroup =null; if(groupID.equals("pain")){ symptomGroup=
	 * symptomGroupRepository.findByGroupPainCode(groupID); }else{ symptomGroup=
	 * symptomGroupRepository.findByGroupCode(groupID); }
	 * logger.info("After calling database" + new Date()); SymptomGroups
	 * responseGroup =
	 * symptomGroupResponseMapper.prepareSymptomsGroupByName(symptomGroup,
	 * dataKeysRepository,dataStores,rCodeSymTemplates,lCode);
	 * 
	 * logger.info("After parsing " + new Date()); return responseGroup; }
	 * 
	 * 
	 * @Override public MICASymptomsGroup getSymtomGroups(String lCode) {
	 * MICASymptomsGroup mICASymptomsGroup= new MICASymptomsGroup();
	 * List<SymptomGroups> modelGroups = new ArrayList<SymptomGroups>();
	 * Iterable<SymptomGroup> groups = symptomGroupRepository.findAll(2);
	 * Set<SymptomTemplate> rCodeSymTemplates =
	 * symptomTemplateRepository.getRcodeSymptomTemplates(); Set<DataStore>
	 * dataStores = dataStoreRepository.findAllDataStores(); Iterator<SymptomGroup>
	 * groupIter = groups.iterator(); while(groupIter.hasNext()){ String groupID =
	 * groupIter.next().getGroupID(); SymptomGroup symptomGroup =null;
	 * if(groupID.equals("pain")){ symptomGroup=
	 * symptomGroupRepository.findByGroupPainCode(groupID); }else{ symptomGroup=
	 * symptomGroupRepository.findByGroupCode(groupID); } SymptomGroups
	 * responseGroup =
	 * symptomGroupResponseMapper.prepareSymptomsGroupByName(symptomGroup,
	 * dataKeysRepository,dataStores,rCodeSymTemplates,lCode);
	 * 
	 * modelGroups.add(responseGroup);
	 * mICASymptomsGroup.setSymptomGroups(modelGroups);
	 * 
	 * } return mICASymptomsGroup;
	 * 
	 * 
	 * 
	 * }
	 */

	/*
	 * private Boolean isLoadableFromCache(String groupID,Map<String, Long>
	 * changedGroups ) { Boolean loadFromCache= true; if(changedGroups != null &&
	 * !changedGroups.isEmpty() ) { if(changedGroups.containsKey(groupID)) {
	 * loadFromCache = false; } } return loadFromCache; }
	 */

	@Override
	public SymptomGroups getSymptomsByGroup(String groupID, String lCode) {
		SymptomGroups symptomGroups = null;
		boolean loadFromCache = true;
		Set<SymptomGroup> dbGroups = symptomGroupRepository.findGroups();
		if (serverCache) {
			Map<String, Long> dbGroupMap = dbGroups.stream().collect(Collectors.toMap(SymptomGroup::getGroupID,
					SymptomGroup::getUpdatedDate, (oldValue, newValue) -> newValue));
			Map<String, Long> cachedGroups = mICACacheService.getChachedGroups(lCode);
			// check for cached groups from the file, if no groups found create with
			// incoming group id
			if (cachedGroups != null && !cachedGroups.isEmpty()) {
				Long dbDate = dbGroupMap.get(groupID);
				Long cachedDate = cachedGroups.get(groupID);
				if (cachedDate == null) {
					loadFromCache = false;
				} else {
					Boolean dateCompare = Objects.equals(dbDate, cachedDate);
					if (dateCompare) {
						loadFromCache = true;
					} else {
						loadFromCache = false;
					}
				}
			} else {
				cachedGroups = new Hashtable<String, Long>();
				loadFromCache = false;
			}

			if (loadFromCache) {
				symptomGroups = mICACacheService.loadSymptomsFromCache(groupID, lCode);
			} else {
				symptomGroups = getSymptomGroupsData(groupID, lCode);
				Long updatedDate = dbGroupMap.get(groupID);
				cachedGroups.put(groupID, updatedDate);
			}

			if (symptomGroups == null) {
				Long updatedDate = dbGroupMap.get(groupID);
				cachedGroups.put(groupID, updatedDate);
				symptomGroups = getSymptomGroupsData(groupID, lCode);
				loadFromCache = false;
			}

			if (!loadFromCache) {
				CacheGroupsTime dbGroupsTime = new CacheGroupsTime();
				dbGroupsTime.setGroupsTimeMap(cachedGroups);
				mICACacheService.createGroupTimeMap(dbGroupsTime, lCode);
				mICACacheService.createSymptomCache(symptomGroups, groupID, lCode);

			}

		} else {
			symptomGroups = getSymptomGroupsData(groupID, lCode);
		}

		return symptomGroups;
	}

	/*
	 * @Override public SymptomGroups getSymptomsByGroup(String groupID,String
	 * lCode) { SymptomGroups symptomGroups = null; Set<SymptomGroup> dbGroups =
	 * symptomGroupRepository.findGroups(); if(serverCache) { Map<String, Long>
	 * dbGroupMap =
	 * dbGroups.stream().collect(Collectors.toMap(SymptomGroup::getGroupID,
	 * SymptomGroup::getUpdatedDate, (oldValue, newValue) -> newValue));
	 * 
	 * Map<String, Long> changedGroups =
	 * mICACacheService.getGroupsChanged(dbGroupMap,lCode);
	 * logger.info("changedGroups :: "+ changedGroups);
	 * logger.info("groupID  from URL:: "+ groupID); Boolean loadFromCache=
	 * isLoadableFromCache(groupID, changedGroups); logger.info("loadFromCache :: "+
	 * loadFromCache); if(loadFromCache){ symptomGroups=
	 * mICACacheService.loadSymptomsFromCache(groupID,lCode); } if(!loadFromCache ||
	 * symptomGroups ==null) { symptomGroups = getSymptomGroupsData(groupID,lCode);
	 * mICACacheService.createSymptomCache(symptomGroups, groupID,lCode); }
	 * 
	 * if(!changedGroups.isEmpty()) { CacheGroupsTime dbGroupsTime = new
	 * CacheGroupsTime(); dbGroupsTime.setGroupsTimeMap(dbGroupMap);
	 * mICACacheService.createGroupTimeMap(dbGroupsTime,lCode); }
	 * 
	 * } else{ symptomGroups = getSymptomGroupsData(groupID,lCode); }
	 * 
	 * return symptomGroups; }
	 */

	private SymptomGroups getSymptomGroupsData(String groupID, String lCode) {
		Set<SymptomTemplate> rCodeSymTemplates = symptomTemplateRepository.getRcodeSymptomTemplates();
		Set<DataStore> dataStores = dataStoreRepository.findAllDataStores();
		List<GenericQueryResultEntity> logicalGroups = symptomTemplateRepository.getLogicalGroups();
		List<GenericQueryResultEntity> deGroups = symptomTemplateRepository.getDEGroups();

		// Map<Integer, Object> classifications = getClassfications();

		SymptomGroup symptomGroup = null;
		if (groupID.equals("pain")) {
			symptomGroup = symptomGroupRepository.findByGroupPainCode(groupID);
		} /*
			 * else if(groupID.equals("nlp")){ symptomGroup=
			 * symptomGroupRepository.findGroupByNLP(groupID); }
			 */else {
			symptomGroup = symptomGroupRepository.findByGroupCode(groupID);
		}

		SymptomGroups responseGroup = symptomGroupResponseMapper.prepareSymptomsGroupByName(symptomGroup,
				dataKeysRepository, dataStores, rCodeSymTemplates, lCode, groupID, logicalGroups, deGroups);

		return responseGroup;
	}

	/*
	 * @Override public MICASymptomsGroup getSymtomGroups(String lCode) {
	 * MICASymptomsGroup mICASymptomsGroup= new MICASymptomsGroup();
	 * List<SymptomGroups> modelGroups = new ArrayList<SymptomGroups>();
	 * Set<SymptomGroup> groups = symptomGroupRepository.findGroups(); Map<String,
	 * Long> dbGroupMap =
	 * groups.stream().collect(Collectors.toMap(SymptomGroup::getGroupID,
	 * SymptomGroup::getUpdatedDate, (oldValue, newValue) -> newValue));
	 * 
	 * logger.info("getSymtomGroups ::  groups from database :"+dbGroupMap);
	 * 
	 * if(serverCache) { Map<String, Long> changedGroups =
	 * mICACacheService.getGroupsChanged(dbGroupMap,lCode); for (Map.Entry<String,
	 * Long> entry : dbGroupMap.entrySet()) { String groupID = entry.getKey();
	 * SymptomGroups symptomGroups = null; Boolean loadFromCache
	 * =isLoadableFromCache(groupID, changedGroups); if(loadFromCache){
	 * symptomGroups= mICACacheService.loadSymptomsFromCache(groupID,lCode); }
	 * logger.info("getSymtomGroups ::   loadFromCache :: "+ loadFromCache);
	 * if(!loadFromCache || symptomGroups ==null) { symptomGroups =
	 * getSymptomGroupsData(groupID,lCode);
	 * mICACacheService.createSymptomCache(symptomGroups, groupID,lCode); }
	 * modelGroups.add(symptomGroups); } if(!changedGroups.isEmpty()) {
	 * CacheGroupsTime dbGroupsTime = new CacheGroupsTime();
	 * dbGroupsTime.setGroupsTimeMap(dbGroupMap);
	 * mICACacheService.createGroupTimeMap(dbGroupsTime,lCode); }
	 * 
	 * 
	 * } else{ dbGroupMap.forEach((groupID,timestamp)->{
	 * 
	 * SymptomGroups symptomGroups =getSymptomGroupsData(groupID,lCode);
	 * 
	 * modelGroups.add(symptomGroups);
	 * 
	 * });
	 * 
	 * } mICASymptomsGroup.setSymptomGroups(modelGroups); return mICASymptomsGroup;
	 * 
	 * }
	 */

	@Override
	public MICASymptomsGroup getSymtomGroups(String lCode) {
		MICASymptomsGroup mICASymptomsGroup = new MICASymptomsGroup();
		List<SymptomGroups> modelGroups = new ArrayList<SymptomGroups>();

		Set<SymptomGroup> groups = symptomGroupRepository.findGroups();
		Map<String, Long> dbGroupMap = groups.stream().collect(Collectors.toMap(SymptomGroup::getGroupID,
				SymptomGroup::getUpdatedDate, (oldValue, newValue) -> newValue));

		List<Callable<SymptomGroups>> callableTasks = new ArrayList<Callable<SymptomGroups>>();

		ExecutorService exec = Executors.newFixedThreadPool(dbGroupMap.size());

		dbGroupMap.forEach((groupID, timestamp) -> {
			Callable<SymptomGroups> callable = (() -> {
				// Perform some computation
				// Thread.sleep(2000);
				return getSymptomsByGroup(groupID, lCode);
			});

			callableTasks.add(callable);

		});

		try {
			Collection<Future<SymptomGroups>> futures = exec.invokeAll(callableTasks);
			for (Future<SymptomGroups> f : futures) {
				if (f.isDone()) {
					modelGroups.add(f.get());
				}
			}
		} catch (Exception ex) {
			logger.error("Error: getSymtomGroups(" + lCode + ")");
			ex.printStackTrace();
		} finally {
			exec.shutdown();
		}

		/*
		 * dbGroupMap.forEach((groupID,timestamp)->{
		 * 
		 * SymptomGroups symptomGroups =getSymptomsByGroup(groupID,lCode);
		 * 
		 * modelGroups.add(symptomGroups);
		 * 
		 * });
		 */
		mICASymptomsGroup.setSymptomGroups(modelGroups);
		return mICASymptomsGroup;

	}

	@Transactional
	public Iterable<SymptomGroup> createSymtoms(MICASymptomsGroup micaSymptomsGroup) {

		List<SymptomGroups> groups = micaSymptomsGroup.getSymptomGroups();
		Set<SymptomGroup> groupSet = new HashSet<SymptomGroup>();

		for (int i = 0; i < groups.size(); i++) {
			SymptomGroups group = groups.get(i);
			SymptomGroup symptomGroup = symptomGroupRepository.findByName(group.getName(), 2);
			symptomGroupRequestMapper.createsymptomGroup(symptomGroup, group, symptomTemplateRepository);
			groupSet.add(symptomGroup);
		}

		Iterable<SymptomGroup> symptomGroup = symptomGroupRepository.save(groupSet, 6);
		return symptomGroup;

	}

	@Transactional
	public SymptomGroups createsymptomsByGroup(SymptomGroups modelGroup, String groupId) {
		String lCode = "en";
		if (!modelGroup.getName().equalsIgnoreCase(groupId)) {
			throw new MICAApplicationException("Group ID is not valid. Please check the request parameters.");
		}
		Set<DataStore> dataStores = dataStoreRepository.findAllDataStores();
		// Map<Integer, Object> classifications = getClassfications();
		Set<SymptomTemplate> rCodeSymTemplates = symptomTemplateRepository.getRcodeSymptomTemplates();
		List<GenericQueryResultEntity> logicalGroups = symptomTemplateRepository.getLogicalGroups();
		List<GenericQueryResultEntity> deGroups = symptomTemplateRepository.getDEGroups();

		List<CategoryModel> categories = modelGroup.getCategories();
		for (int i = 0; i < categories.size(); i++) {
			CategoryModel category = categories.get(i);
			if (category != null) {
				SymptomCategory symptomCategory = symptomCategoryRepository.findByCode(category.getCategoryID(), 1);
				if (symptomCategory.getSymptomTemplates() != null) {
					symptomCategory.getSymptomTemplates().addAll(
							symptomGroupRequestMapper.createSymptomTemplateList(category, symptomTemplateRepository));
					symptomCategoryRepository.save(symptomCategory, 2);
				}
			}
		}

		SymptomGroup symptomGroup = symptomGroupRepository.findByGroupID(groupId, 5);
		SymptomGroups responseGroup = symptomGroupResponseMapper.prepareSymptomsGroupByName(symptomGroup,
				dataKeysRepository, dataStores, rCodeSymTemplates, lCode, groupId, logicalGroups, deGroups);
		return responseGroup;
	}

	/*
	 * @Override
	 * 
	 * @Transactional public SymptomCategory createSymptomsByCategory(Category
	 * category) throws MICAApplicationException { SymptomCategory symptomCategory =
	 * null; if(category != null){ SymptomTemplate
	 * dbSymtomTeplate=symptomTemplateRepository.findByCode(category.getSymptoms().
	 * get(0).getSymptomID(), 1); if(dbSymtomTeplate == null){ dbSymtomTeplate = new
	 * SymptomTemplate(); symptomCategory =
	 * symptomCategoryRepository.findByCode(category.getCategoryID(),1);
	 * symptomGroupRequestMapper.createSymptomTemplate(category.getSymptoms().get(0)
	 * ,dbSymtomTeplate); symptomTemplateRepository.save(dbSymtomTeplate);
	 * if(symptomCategory.getSymptomTemplates() != null ) {
	 * symptomCategory.getSymptomTemplates().add(dbSymtomTeplate);
	 * symptomCategoryRepository.save(symptomCategory); }
	 * 
	 * } else{
	 * symptomGroupRequestMapper.createSymptomTemplate(category.getSymptoms().get(0)
	 * ,dbSymtomTeplate); symptomTemplateRepository.save(dbSymtomTeplate);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * return symptomCategory;
	 * 
	 * }
	 */

	@Override
	@Transactional
	public SymptomCategory createSymptomsByCategory(CategoryModel category) throws MICAApplicationException {
		SymptomCategory symptomCategory = null;
		if (category != null) {
			symptomCategory = symptomCategoryRepository.findByCode(category.getCategoryID(), 1);
			if (symptomCategory == null) {
				throw new MICAApplicationException("Category ID does not exists in database...");
			}
			List<Symptoms> modelSymptoms = category.getSymptoms();
			for (int i = 0; i < modelSymptoms.size(); i++) {
				Symptoms modelSymptom = modelSymptoms.get(i);
				SymptomTemplate dbSymtomTeplate = symptomTemplateRepository.findByCode(modelSymptom.getSymptomID(), 1);
				if (dbSymtomTeplate == null) {
					dbSymtomTeplate = new SymptomTemplate();
					symptomGroupRequestMapper.createSymptomTemplate(modelSymptom, dbSymtomTeplate);
					symptomTemplateRepository.save(dbSymtomTeplate);
					if (symptomCategory.getSymptomTemplates() != null) {
						symptomCategory.getSymptomTemplates().add(dbSymtomTeplate);
						symptomCategoryRepository.save(symptomCategory);
					} else {
						Set<SymptomTemplate> symptomTemplates = new HashSet<SymptomTemplate>();
						symptomTemplates.add(dbSymtomTeplate);
						symptomCategory.setSymptomTemplates(symptomTemplates);
						symptomCategoryRepository.save(symptomCategory);
					}

				} else if (symptomCategory.getSymptomTemplates() != null
						&& symptomCategory.getSymptomTemplates().contains(dbSymtomTeplate)) {
					symptomGroupRequestMapper.createSymptomTemplate(modelSymptom, dbSymtomTeplate);
					symptomTemplateRepository.save(dbSymtomTeplate);

				} else {
					throw new MICAApplicationException(
							"Failed to Add/Modify the symptom. This symptom being used by other category.");
				}
			}
		}
		return symptomCategory;
	}

	@Override
	@Transactional
	public String deleteSymptom(String symptomID) throws MICAApplicationException {
		SymptomTemplate dbSymtomTeplate = symptomTemplateRepository.findByCode(symptomID, 1);
		Set<Symptom> symptoms = symptomRepository.findByCode(symptomID);

		if (dbSymtomTeplate == null && symptoms == null) {
			throw new MICAApplicationException("SymptomID does not exists !");
		}

		if (symptoms != null) {
			Iterator<Symptom> symptomItr = symptoms.iterator();
			while (symptomItr.hasNext()) {
				Symptom dbSymptom = symptomItr.next();
				logger.info("Deleted symptom ID : " + dbSymptom.getId());
				mICARestService.deleteSymptomByID(dbSymptom.getId());
			}
		}

		if (dbSymtomTeplate != null) {
			symptomTemplateRepository.delete(dbSymtomTeplate);
		}
		return "Symptom deleted successfully.";
	}

	@Override
	public SymptomTemplateModel getSymptomByID(String symptomID, String lCode) {

		SymptomTemplateModel symptomTemplateModel = new SymptomTemplateModel();
		SymptomTemplate dbSymtomTeplate = symptomTemplateRepository.findByCode(symptomID, 1);

		if (dbSymtomTeplate == null) {
			throw new DataNotFoundException("No symptom found for the SymptomID: " + symptomID);
		}

		Map<String, Float> antithesis = new HashMap<String, Float>();
		Map<String, Boolean> displayListValues = new HashMap<String, Boolean>();

		// key = data store code, value = icd10Code
		Map<String, String> icdListRCodes = new HashMap<String, String>();
		Map<String, String> snonmedListCodes = new HashMap<String, String>();

		// symptom level rCodes
		List<String> rCodes = new ArrayList<String>();
		List<Long> sCodes = new ArrayList<Long>();

		Map<String, String> icd10RCodes = new HashMap<String, String>();
		Map<String, String> snomedCodes = new HashMap<String, String>();

		getRCodeListValues(dbSymtomTeplate, rCodes, icdListRCodes);

		getSnomedCodeListValues(dbSymtomTeplate, sCodes, snonmedListCodes);

		symptomTemplateModel.setSymptomID(symptomID);
		symptomTemplateModel.setCriticality(dbSymtomTeplate.getCriticality());
		symptomTemplateModel.setName(dbSymtomTeplate.getName());
		symptomTemplateModel.setQuestion(dbSymtomTeplate.getQuestionText());
		if (dbSymtomTeplate.getTimeType() != null) {
			symptomTemplateModel.setTimeType(TimeType.fromText(dbSymtomTeplate.getTimeType()));
		}
		symptomTemplateModel.setGenderGroup(dbSymtomTeplate.getGenderGroup());

		if (MICAConstants.SPANISH.equals(lCode)) {
			symptomTemplateModel.setEs_name(dbSymtomTeplate.getEs_name());
			symptomTemplateModel.setEs_question(dbSymtomTeplate.getEs_questionText());
		}

		symptomTemplateModel.setQuestion(dbSymtomTeplate.getQuestionText());
		symptomTemplateModel.setTreatable(dbSymtomTeplate.getTreatable());
		symptomTemplateModel.setCardinality(dbSymtomTeplate.getCardinality());
		symptomTemplateModel.setKioskName(dbSymtomTeplate.getKioskName());
		symptomTemplateModel.setFormalName(dbSymtomTeplate.getFormalName());
		symptomTemplateModel.setDefinition(dbSymtomTeplate.getDefinition());
		symptomTemplateModel.setDisplayDrApp(dbSymtomTeplate.getDisplayDrApp());
		String mutliplier = dbSymtomTeplate.getMultipleValues();
		antithesis.put("root", dbSymtomTeplate.getAntithesis());
		icd10RCodes.put("root", null);
		snomedCodes.put("root", null);

		if (rCodes != null && !rCodes.isEmpty()) {
			icd10RCodes.put("root", String.join(",", rCodes));
		}

		displayListValues.put("root", dbSymtomTeplate.getDisplaySymptom());

		if (sCodes != null && !sCodes.isEmpty()) {
			snomedCodes.put("root", getCodesWithCommaDelimt(sCodes));
		}

		if (mutliplier != null && !mutliplier.isEmpty()) {
			populateRequiredListData(dbSymtomTeplate.getMultipleValues(), antithesis, icd10RCodes, displayListValues,
					icdListRCodes, snonmedListCodes, snomedCodes);
		}
		symptomTemplateModel.setAntithesis(antithesis);

		symptomTemplateModel.setIcd10RCodes(icd10RCodes);

		symptomTemplateModel.setSnomedCodes(snomedCodes);

		symptomTemplateModel.setDisplayListValues(displayListValues);

		return symptomTemplateModel;
	}

	private void getSnomedCodeListValues(SymptomTemplate snomedSymptomMap, List<Long> sCodes,
			Map<String, String> snonmedListCodes) {

		if (snomedSymptomMap != null) {
			Set<SnomedCodes> dbDataStoreSet = snomedSymptomMap.getSnomedCodes();

			if (dbDataStoreSet != null) {
				Iterator<SnomedCodes> dataStoreItr = dbDataStoreSet.iterator();
				while (dataStoreItr.hasNext()) {
					SnomedCodes dbDataStore = dataStoreItr.next();
					if (dbDataStore != null) {
						if (dbDataStore.getListValueCode() == null && dbDataStore.getConceptID() != null) {
							sCodes.addAll(dbDataStore.getConceptID());
						} else if (dbDataStore.getListValueCode() != null && dbDataStore.getConceptID() != null) {
							snonmedListCodes.put(dbDataStore.getListValueCode(),
									getCodesWithCommaDelimt(dbDataStore.getConceptID()));
						}
					}
				}
			}
		}
	}

	private void getRCodeListValues(SymptomTemplate symptomTemplate, List<String> rCodes,
			Map<String, String> icdListRCodes) {

		Map<String, List<String>> icdDBListRCodes = new Hashtable<String, List<String>>();

		if (symptomTemplate != null) {
			Set<RCodeDataStore> dbDataStoreSet = symptomTemplate.getrCodeDataStores();
			if (dbDataStoreSet != null) {
				Iterator<RCodeDataStore> dataStoreItr = dbDataStoreSet.iterator();
				while (dataStoreItr.hasNext()) {
					RCodeDataStore dbDataStore = dataStoreItr.next();
					if (dbDataStore != null) {
						if ((dbDataStore.getDsCode() == null || dbDataStore.getDsCode().equalsIgnoreCase("null"))
								&& dbDataStore.getM_icd10RCode() != null) {
							rCodes.add(dbDataStore.getM_icd10RCode());
						} else if (dbDataStore.getDsCode() != null && dbDataStore.getM_icd10RCode() != null) {
							// icdListRCodes.put(dbDataStore.getDsCode(), dbDataStore.getM_icd10RCode());
							List<String> listRcodes = icdDBListRCodes.get(dbDataStore.getDsCode());
							if (listRcodes == null) {
								listRcodes = new ArrayList<String>();
							}

							listRcodes.add(dbDataStore.getM_icd10RCode());
							icdDBListRCodes.put(dbDataStore.getDsCode(), listRcodes);
						}
					}
				}
			}
		}

		if (icdDBListRCodes != null && !icdDBListRCodes.isEmpty()) {
			icdDBListRCodes.forEach((key, icr10codes) -> {
				icdListRCodes.put(key, getCodesWithCommaDelimtStr(icr10codes));
			});
		}
	}

	private void populateRequiredListData(String multipleValues, Map<String, Float> antithesis,
			Map<String, String> icd10RCodes, Map<String, Boolean> displayListValue, Map<String, String> icdListRCodes,
			Map<String, String> snonmedListCodes, Map<String, String> snomedCodes) {
		DataKeys dataKeys = dataKeysRepository.findByName(multipleValues, 2);
		if (dataKeys != null) {
			Set<DataStore> dbDataStoreSet = dataKeys.getDataStoreList();
			if (dbDataStoreSet != null) {
				Iterator<DataStore> dataStoreItr = dbDataStoreSet.iterator();
				while (dataStoreItr.hasNext()) {
					DataStore dbDataStore = dataStoreItr.next();
					antithesis.put(dbDataStore.getName(), dbDataStore.getM_antithesis());
					icd10RCodes.put(dbDataStore.getName(), icdListRCodes.get(dbDataStore.getCode()));
					snomedCodes.put(dbDataStore.getName(), snonmedListCodes.get(dbDataStore.getCode()));
					displayListValue.put(dbDataStore.getName(), dbDataStore.getDisplayListValue());
				}
			}
		}
	}

	public SymptomTemplateModel updateSymptom(SymptomTemplateModel symptomTemplateModel, String languagecode) {

		String symptomID = symptomTemplateModel.getSymptomID();

		SymptomTemplate dbSymptomTemplate = symptomTemplateRepository.findByCode(symptomID, 2);
		SymptomTemplateModel symptom = new SymptomTemplateModel();
		Boolean updateFlag = false;
		if (dbSymptomTemplate == null) {
			throw new DataNotFoundException("No symptom data found for the SymptomID:" + symptomID);
		}
		Map<String, Float> antithesis = symptomTemplateModel.getAntithesis();

		Map<String, String> icd10RCodes = symptomTemplateModel.getIcd10RCodes();

		Map<String, String> snomedCodes = symptomTemplateModel.getSnomedCodes();

		// key = data store code, value = icd10Code
		Map<String, List<String>> icdListRCodes = new HashMap<String, List<String>>();
		Map<String, List<Long>> snomedListCodes = new HashMap<String, List<Long>>();

		// symptom level rCodes
		String[] rCodes = null;
		// Symptom level snomed codes
		List<Long> sCodes = null;

		Map<String, Boolean> displayListValues = symptomTemplateModel.getDisplayListValues();

		if (symptomTemplateModel.getCriticality() != null) {
			dbSymptomTemplate.setCriticality(symptomTemplateModel.getCriticality());
		}

		if (symptomTemplateModel.getQuestion() != null) {
			dbSymptomTemplate.setQuestionText(symptomTemplateModel.getQuestion());
		}

		if (symptomTemplateModel.getTimeType() != null) {
			dbSymptomTemplate.setTimeType(symptomTemplateModel.getTimeType().getText());
		}

		if (MICAConstants.SPANISH.equals(languagecode)) {
			dbSymptomTemplate.setEs_questionText(symptomTemplateModel.getEs_question());
		}

		if (symptomTemplateModel.getCardinality() != null) {
			dbSymptomTemplate.setCardinality(symptomTemplateModel.getCardinality());
		}

		if (symptomTemplateModel.getTreatable() != null) {
			dbSymptomTemplate.setTreatable(symptomTemplateModel.getTreatable());
		}

		if (symptomTemplateModel.getDefinition() != null) {
			dbSymptomTemplate.setDefinition(symptomTemplateModel.getDefinition());
		}

		// dbSymptomTemplate.setGenderGroup(symptomTemplateModel.getGenderGroup());

		dbSymptomTemplate.setGenderGroup(String.valueOf(symptomTemplateModel.getGenderGroup()));

		if (symptomTemplateModel.getDisplayDrApp() != null) {
			dbSymptomTemplate.setDisplayDrApp(symptomTemplateModel.getDisplayDrApp());
		}

		dbSymptomTemplate.setUpdatedDate(new Date().getTime());

		String multiplier = dbSymptomTemplate.getMultipleValues();

		if (antithesis != null && !antithesis.isEmpty() && antithesis.get("root") != null) {
			dbSymptomTemplate.setAntithesis(antithesis.get("root"));
		}

		if (icd10RCodes != null && !icd10RCodes.isEmpty() && icd10RCodes.get("root") != null) {
			String strIcd10Code = icd10RCodes.get("root");
			rCodes = strIcd10Code.replaceAll("\\s", "").split(",");
		}

		if (snomedCodes != null && !snomedCodes.isEmpty() && snomedCodes.get("root") != null) {
			String strSCodes = snomedCodes.get("root");
			// sCodes =
			// Arrays.stream(strSCodes.split(",")).map(Integer::parseInt).collect(Collectors.toList());
			sCodes = Arrays.stream(strSCodes.split(",")).map(Long::parseLong).collect(Collectors.toList());
		}

		if (displayListValues != null && !displayListValues.isEmpty() && displayListValues.get("root") != null) {
			dbSymptomTemplate.setDisplaySymptom(displayListValues.get("root"));
		}

		if (multiplier != null && !multiplier.isEmpty()) {
			updateFlag = updateDataStore(multiplier, antithesis, icd10RCodes, displayListValues, icdListRCodes,
					snomedCodes, snomedListCodes);
		}

		updateFlag = updateRCodes(symptomID, rCodes, icdListRCodes, dbSymptomTemplate);
		updateFlag = updateSnomedCodescodes(symptomID, sCodes, snomedListCodes, dbSymptomTemplate);

		SymptomTemplate afterSaveSymptomTemplate = symptomTemplateRepository.save(dbSymptomTemplate, 2);

		if (afterSaveSymptomTemplate != null) {
			symptom.setSymptomID(dbSymptomTemplate.getCode());
			;
			symptom.setStatus("Symptom updated successfully.");
		}

		if (updateFlag || afterSaveSymptomTemplate != null) {
			updateGroupsTimeStamp(dbSymptomTemplate.getCode());

		}

		return symptom;

	}

	private Boolean updateSnomedCodescodes(String symptomID, List<Long> sCodes, Map<String, List<Long>> snomedListCodes,
			SymptomTemplate dbSymptomTemplate) {
		symptomTemplateRepository.deleteSnomdedCodes(symptomID);
		final Set<SnomedCodes> listSnomedCodes = new HashSet<SnomedCodes>();
		if ((sCodes != null && !sCodes.isEmpty()) || (snomedListCodes != null && !snomedListCodes.isEmpty())) {
			SnomedCodes snomedCode = new SnomedCodes();
			snomedCode.setConceptID(sCodes);
			listSnomedCodes.add(snomedCode);
		}

		if (snomedListCodes != null && !snomedListCodes.isEmpty()) {
			snomedListCodes.forEach((key, snomeCodes) -> {
				SnomedCodes snomedCode = new SnomedCodes();
				snomedCode.setListValueCode(key);
				snomedCode.setConceptID(snomeCodes);
				listSnomedCodes.add(snomedCode);
			});
		}

		if (dbSymptomTemplate != null) {
			dbSymptomTemplate.setSnomedCodes(listSnomedCodes);
			return true;
		}
		return false;
	}

	private Boolean updateRCodes(String symptomID, String[] rCodes, Map<String, List<String>> icdListRCodes,
			SymptomTemplate dbSymptomTemplate) {
		symptomTemplateRepository.deleteRCodeDataStore(symptomID);
		final Set<RCodeDataStore> rCodeDataStores = new HashSet<RCodeDataStore>();
		if (rCodes != null && rCodes.length > 0) {
			for (int i = 0; i < rCodes.length; i++) {
				RCodeDataStore rCodeDataStore = new RCodeDataStore();
				rCodeDataStore.setM_icd10RCode(rCodes[i]);
				rCodeDataStores.add(rCodeDataStore);
			}
		}

		if (icdListRCodes != null && !icdListRCodes.isEmpty()) {
			icdListRCodes.forEach((key, icr10codes) -> {
				for (int i = 0; i < icr10codes.size(); i++) {
					RCodeDataStore rCodeDataStore = new RCodeDataStore();
					rCodeDataStore.setM_icd10RCode(icr10codes.get(i));
					rCodeDataStore.setDsCode(key);
					rCodeDataStores.add(rCodeDataStore);
				}
			});
		}
		if (dbSymptomTemplate != null) {
			dbSymptomTemplate.setrCodeDataStores(rCodeDataStores);
			return true;
		}

		return false;

	}

	/**
	 * Set the updatedTime for the group retlated to symptomID
	 * 
	 * @param code
	 */
	private void updateGroupsTimeStamp(String code) {
		SymptomGroup generalGroups = symptomGroupRepository.updateGroupTime(code);
		if (generalGroups == null) {
			symptomGroupRepository.updateGroupPainSwellingTime(code);
		}
	}

	private boolean updateDataStore(String multiplier, Map<String, Float> antithesisMap,
			Map<String, String> icd10RCodes, Map<String, Boolean> displayListValues,
			Map<String, List<String>> icdListRCodes, Map<String, String> snomedCodes,
			Map<String, List<Long>> snomedListCodes) {
		DataKeys dataKeys = dataKeysRepository.findByName(multiplier, 1);

		Boolean updateFlag = false;
		Boolean dataStoreFlag = false;
		if (dataKeys != null) {
			Set<DataStore> dataStoreList = dataKeys.getDataStoreList();
			if (dataStoreList != null) {
				Iterator<DataStore> dataStoreItr = dataStoreList.iterator();
				while (dataStoreItr.hasNext()) {
					DataStore dbDataStore = dataStoreItr.next();

					if (!antithesisMap.isEmpty()) {
						Float antheiis = antithesisMap.get(dbDataStore.getName());
						if (antheiis != null) {
							dbDataStore.setM_antithesis(antheiis);
							dataStoreFlag = true;
						}
					}

					if (!icd10RCodes.isEmpty()) {
						String icd10RCode = icd10RCodes.get(dbDataStore.getName());
						if (icd10RCode != null) {
							List<String> dbRcodesCodes = Arrays.stream(icd10RCode.split(","))
									.collect(Collectors.toList());
							// snomedListCodes.put(dbDataStore.getCode(), dbSnomeCodes);
							icdListRCodes.put(dbDataStore.getCode(), dbRcodesCodes);
							updateFlag = true;
						}
					}

					if (!displayListValues.isEmpty()) {
						Boolean displayFlag = displayListValues.get(dbDataStore.getName());
						if (displayFlag != null) {
							dbDataStore.setDisplayListValue(displayFlag);
							updateFlag = true;
						}
					}

					if (!snomedCodes.isEmpty()) {
						String snomedCode = snomedCodes.get(dbDataStore.getName());
						if (snomedCode != null) {
							// List<Integer> dbSnomeCodes =
							// Arrays.stream(snomedCode.split(",")).map(Integer::parseInt).collect(Collectors.toList());
							List<Long> dbSnomeCodes = Arrays.stream(snomedCode.split(",")).map(Long::parseLong)
									.collect(Collectors.toList());
							snomedListCodes.put(dbDataStore.getCode(), dbSnomeCodes);
							updateFlag = true;
						}

					}
				}
			}
		}

		if (dataStoreFlag) {
			dataKeysRepository.save(dataKeys, 1);
		}
		return updateFlag;
	}

	@Override
	public Symptoms getSymptomByCode(String symptomID) {
		Set<String> dataStoreRefList = new HashSet<String>();
		SymptomTemplate dbSymtomTeplate = symptomTemplateRepository.findByCode(symptomID, 1);
		Symptoms symptom = symptomGroupResponseMapper.createSymtoms(dbSymtomTeplate, dataStoreRefList);
		return symptom;
	}

	@Override
	public List<SymptomTemplateValidate> validateTemplates(String languagecode) {
		List<SymptomTemplateValidate> badSymptoms = new ArrayList<SymptomTemplateValidate>();
		Iterable<SymptomTemplate> dbBadSymptomsNonMultiplier = symptomTemplateRepository
				.getInvalidAtrForNonMultiplier();
		populateBadSymptoms(badSymptoms, dbBadSymptomsNonMultiplier, languagecode);
		Iterable<SymptomTemplate> dbBadSymptomsMultiplier = symptomTemplateRepository.getInvalidAtrForMultiplier();
		populateBadSymptoms(badSymptoms, dbBadSymptomsMultiplier, languagecode);

		return badSymptoms;
	}

	private void populateBadSymptoms(List<SymptomTemplateValidate> badSymptoms, Iterable<SymptomTemplate> dbBadSymptoms,
			String languagecode) {
		if (dbBadSymptoms != null) {
			Iterator<SymptomTemplate> dbSymptomItr = dbBadSymptoms.iterator();
			while (dbSymptomItr.hasNext()) {
				SymptomTemplate dbSymptom = dbSymptomItr.next();
				if (dbSymptom != null) {
					boolean isResultRange = false;

					if (dbSymptom.getRange() != null) {
						isResultRange = dbSymptom.getRange();
					}

					List<String> dataInvalidAttributes = new ArrayList<String>();
					SymptomTemplateValidate modelSymptom = new SymptomTemplateValidate();
					modelSymptom.setSymptomID(dbSymptom.getCode());
					modelSymptom.setName(dbSymptom.getName());
					if (dbSymptom.getQuestionText() == null) {
						dataInvalidAttributes.add("Question");
					} else if (dbSymptom.getQuestionText().isEmpty()) {
						dataInvalidAttributes.add("Question");
					}
					if (MICAConstants.SPANISH.equals(languagecode)) {

						if (dbSymptom.getEs_questionText() == null) {
							dataInvalidAttributes.add("ES_Question");
						} else if (dbSymptom.getEs_questionText().isEmpty()) {
							dataInvalidAttributes.add("ES_Question");
						}
					}

					if (dbSymptom.getTimeType() == null) {
						dataInvalidAttributes.add("TimeType");
					}

					if (isResultRange) {

						if (dbSymptom.getMaxRange() == null) {
							dataInvalidAttributes.add("maxRange");
						}

						if (dbSymptom.getMinRange() == null) {
							dataInvalidAttributes.add("minRange");
						}
					}

					if (dbSymptom.getTreatable() == null) {
						dataInvalidAttributes.add("Treatable");
					}

					if (dbSymptom.getDisplaySymptom() == null) {
						dataInvalidAttributes.add("DisplaySymptom");
					}

					if (dbSymptom.getCriticality() == null) {
						dataInvalidAttributes.add("Criticality");
					} else if (dbSymptom.getCriticality() != null
							&& (dbSymptom.getCriticality() < 0 || dbSymptom.getCriticality() > 9)) {
						dataInvalidAttributes.add("Criticality");
					}

					if (dbSymptom.getAntithesis() == null) {
						dataInvalidAttributes.add("Antithesis");
					}
					if (dbSymptom.getMultipleValues() != null) {
						populateBadMultipliers(dbSymptom, modelSymptom, isResultRange);
						if (dbSymptom.getCardinality() == null) {
							dataInvalidAttributes.add("Multiple Answers");
						}
					}
					modelSymptom.setDataInvalidAttributes(dataInvalidAttributes);
					badSymptoms.add(modelSymptom);
				}

			}
		}

	}

	private void populateBadMultipliers(SymptomTemplate dbSymptom, SymptomTemplateValidate modelSymptom,
			boolean isResultRange) {
		List<String> badStrings = new ArrayList<String>();
		if (isResultRange) {
			badStrings.addAll(dataStoreRepository.findBadRangeMultiplier(dbSymptom.getCode()));
		} else {
			badStrings.addAll(dataStoreRepository.findBadMultiplier(dbSymptom.getCode()));
		}
		if (badStrings != null && !badStrings.isEmpty()) {
			Map<String, List<String>> multiplier = new Hashtable<String, List<String>>();
			multiplier.put(dbSymptom.getMultipleValues(), badStrings);
			modelSymptom.setMultiplier(multiplier);
		}

	}

	@Override
	public Set<SymptomTemplate> getAllSymptomTemplates() {
		return symptomTemplateRepository.getAllSymptomTemplates();
	}

	@Override
	public List<SymptomTemplateModel> getAllSymptomDefinitions() {
		List<SymptomTemplate> dbTemplates = symptomTemplateRepository.getAllSymptomDefinitions();
		List<SymptomTemplateModel> modelTemplates = new ArrayList<SymptomTemplateModel>();
		if (dbTemplates != null) {
			for (SymptomTemplate symptomTemplate : dbTemplates) {
				SymptomTemplateModel model = new SymptomTemplateModel();
				model.setCode(symptomTemplate.getCode());
				model.setDefinition(symptomTemplate.getDefinition());
				modelTemplates.add(model);
			}
		}

		return modelTemplates;
	}

	private String getCodesWithCommaDelimt(List<Long> codes) {
		StringBuffer strCodes = new StringBuffer();
		if (codes.size() > 0) {
			strCodes.append(codes.get(0));
			for (int i = 1; i < codes.size(); i++) {
				strCodes.append("," + codes.get(i));
			}
		}
		return strCodes.toString();
	}

	private String getCodesWithCommaDelimtStr(List<String> codes) {
		StringBuffer strCodes = new StringBuffer();
		if (codes.size() > 0) {
			strCodes.append(codes.get(0));
			for (int i = 1; i < codes.size(); i++) {
				strCodes.append("," + codes.get(i));
			}
		}
		return strCodes.toString();
	}

	/**
	 * Returns the symptom ID, name and active. This API being used in MITA to
	 * choose symptoms for Data frame.
	 * 
	 **/
	@Override
	public PaginationModel getSymptomsWithPaging(Integer page, Integer size, String source, String code, String name,
			String group) {

		if (name != null) {
			if (name.length() < 3) {
				throw new DataInvalidException("Symptom name should be greater than 3 characters.");
			}
		}

		if (code != null) {
			if (code.length() < 3) {
				throw new DataInvalidException("SymptomID  should be greater than 3 characters.");
			}
		}

		if (group != null) {
			if (group.length() < 3) {
				throw new DataInvalidException("Symptom group ID should be greater than 3 characters.");
			}
		}

		PaginationModel illnessPages = new PaginationModel();
		Page<SymptomTemplate> symptomsDbPages = getPagenationContent(illnessPages, page, size, source, code, name,
				group);
		if (symptomsDbPages != null) {
			List<SymptomTemplate> slices = symptomsDbPages.getContent();
			if (slices != null) {
				List<SymptomTemplateModel> symptomContents = new ArrayList<SymptomTemplateModel>();
				for (SymptomTemplate slice : slices) {
					String symptomID = slice.getCode();
					SymptomTemplateModel templateModel = new SymptomTemplateModel();
					templateModel.setSymptomID(symptomID);
					templateModel.setName(slice.getName());
					templateModel.setActive(slice.getActive());
					// iCD10CodeModel.setUserID(userRepository.getUserID(source, icd10Code,
					// version));
					symptomContents.add(templateModel);
				}
				illnessPages.setSymptomContent(symptomContents);
			}

			illnessPages.setFirst(symptomsDbPages.isFirst());
			illnessPages.setLast(symptomsDbPages.isLast());
			illnessPages.setPagenumber(symptomsDbPages.getNumber());
			illnessPages.setElementsInPage(symptomsDbPages.getNumberOfElements());
			illnessPages.setPageSize(symptomsDbPages.getSize());

		}

		return illnessPages;

	}

	private Page<SymptomTemplate> getPagenationContent(PaginationModel illnessPages, Integer page, Integer size,
			String source, String code, String name, String group) {
		Page<SymptomTemplate> templateDbPages = null;
		Integer totalRecords = null;

		if (page == null) {
			page = 1;
		}
		if (size == null) {
			size = 20;
		}
		if (source != null) {
			if (code != null && group != null && name != null) {
				PageRequest pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "st.code");
				if ((group.equalsIgnoreCase("pain") || (group.equalsIgnoreCase("swelling")))) {
					group = "pain";
					templateDbPages = symptomTemplateRepository.findPainSymptomsBySourceAndCodeAndNameAndGroup(
							source.toUpperCase(), group, code.toUpperCase(), name.toUpperCase(), pageable);
					totalRecords = symptomTemplateRepository.findPainSymptomsCountBySourceAndCodeAndNameAndGroup(
							source.toUpperCase(), group, code.toUpperCase(), name.toUpperCase());
				} else {

					templateDbPages = symptomTemplateRepository.findSymptomsBySourceAndCodeAndNameAndGroup(
							source.toUpperCase(), group, code.toUpperCase(), name.toUpperCase(), pageable);
					totalRecords = symptomTemplateRepository.findSymptomsCountBySourceAndCodeAndNameAndGroup(
							source.toUpperCase(), group, code.toUpperCase(), name.toUpperCase());
				}

			} else if (group != null && code != null) {
				PageRequest pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "st.code");
				if ((group.equalsIgnoreCase("pain") || (group.equalsIgnoreCase("swelling")))) {
					group = "pain";
					templateDbPages = symptomTemplateRepository.findPainSymptomsBySourceAndCodeAndGroup(
							source.toUpperCase(), group, code.toUpperCase(), pageable);
					totalRecords = symptomTemplateRepository.findPainSymptomsCountBySourceAndCodeAndGroup(
							source.toUpperCase(), group, code.toUpperCase());
				} else {

					templateDbPages = symptomTemplateRepository.findSymptomsBySourceAndCodeAndGroup(
							source.toUpperCase(), group, code.toUpperCase(), pageable);
					totalRecords = symptomTemplateRepository
							.findSymptomsCountBySourceAndCodeAndGroup(source.toUpperCase(), group, code.toUpperCase());
				}

			} else if (group != null && name != null) {
				PageRequest pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "st.code");

				if ((group.equalsIgnoreCase("pain") || (group.equalsIgnoreCase("swelling")))) {
					group = "pain";
					templateDbPages = symptomTemplateRepository.findPainSymptomsBySourceAndNameAndGroup(
							source.toUpperCase(), group, name.toUpperCase(), pageable);
					totalRecords = symptomTemplateRepository.findPainSymptomsCountBySourceAndNameAndGroup(
							source.toUpperCase(), group, name.toUpperCase());
				} else {

					templateDbPages = symptomTemplateRepository.findSymptomsBySourceAndNameAndGroup(
							source.toUpperCase(), group, name.toUpperCase(), pageable);
					totalRecords = symptomTemplateRepository
							.findSymptomsCountBySourceAndNameAndGroup(source.toUpperCase(), group, name.toUpperCase());

				}

			} else if (code != null && name != null) {
				PageRequest pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "st.code");

				templateDbPages = symptomTemplateRepository.findSymptomsBySourceAndCodeAndName(source.toUpperCase(),
						code.toUpperCase(), name.toUpperCase(), pageable);
				totalRecords = symptomTemplateRepository.findSymptomsCountBySourceAndCodeAndName(source.toUpperCase(),
						code.toUpperCase(), name.toUpperCase());

			} else if (group != null) {
				PageRequest pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "st.code");
				if ((group.equalsIgnoreCase("pain") || (group.equalsIgnoreCase("swelling")))) {
					group = "pain";
					templateDbPages = symptomTemplateRepository.findPainSymptomsBySourceAndGroup(source.toUpperCase(),
							group, pageable);
					totalRecords = symptomTemplateRepository.findPainSymptomsCountBySourceAndGroup(source.toUpperCase(),
							group);
				} else {

					templateDbPages = symptomTemplateRepository.findSymptomsBySourceAndGroup(source.toUpperCase(),
							group, pageable);
					totalRecords = symptomTemplateRepository.findSymptomsCountBySourceAndGroup(source.toUpperCase(),
							group);
				}

			} else if (code != null) {
				PageRequest pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "st.code");
				templateDbPages = symptomTemplateRepository.findSymptomsBySourceAndCode(source.toUpperCase(),
						code.toUpperCase(), pageable);
				totalRecords = symptomTemplateRepository.findSymptomsCountBySourceAndCode(source.toUpperCase(),
						code.toUpperCase());

			} else if (name != null) {
				PageRequest pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "st.code");

				templateDbPages = symptomTemplateRepository.findSymptomsBySourceAndName(source.toUpperCase(),
						name.toUpperCase(), pageable);
				totalRecords = symptomTemplateRepository.findSymptomsCountBySourceAndName(source.toUpperCase(),
						name.toUpperCase());

			} else {
				PageRequest pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "code");
				templateDbPages = symptomTemplateRepository.findAll(pageable);
				totalRecords = symptomTemplateRepository.geSymptomsCountBySource(source.toUpperCase());
			}
		}
		illnessPages.setTotalElements(totalRecords);
		return templateDbPages;
	}

	@Override
	public SymptomTemplateModelV1 getSymptomByIDV1(String symptomID, String lCode) {

		// boolean labSymptom = false;

		boolean isResultRange = false;

		SymptomTemplateModelV1 symptomTemplateModel = new SymptomTemplateModelV1();
		SymptomTemplate dbSymtomTeplate = symptomTemplateRepository.findByCode(symptomID, 1);
		List<AdtionalSettings> additionalInfoList = new ArrayList<AdtionalSettings>();

		if (dbSymtomTeplate == null) {
			throw new DataNotFoundException("No symptom found for the SymptomID: " + symptomID);
		}
		symptomTemplateModel.setSymptomID(symptomID);
		symptomTemplateModel.setCriticality(dbSymtomTeplate.getCriticality());
		symptomTemplateModel.setName(dbSymtomTeplate.getName());
		symptomTemplateModel.setQuestion(dbSymtomTeplate.getQuestionText());
		if (dbSymtomTeplate.getTimeType() != null) {
			symptomTemplateModel.setTimeType(TimeType.fromText(dbSymtomTeplate.getTimeType()));
		}
		symptomTemplateModel.setGenderGroup(dbSymtomTeplate.getGenderGroup());

		if (MICAConstants.SPANISH.equals(lCode)) {
			symptomTemplateModel.setEs_name(dbSymtomTeplate.getEs_name());
			symptomTemplateModel.setEs_question(dbSymtomTeplate.getEs_questionText());

		}
		symptomTemplateModel.setTreatable(dbSymtomTeplate.getTreatable());
		symptomTemplateModel.setCardinality(dbSymtomTeplate.getCardinality());
		symptomTemplateModel.setKioskName(dbSymtomTeplate.getKioskName());
		symptomTemplateModel.setFormalName(dbSymtomTeplate.getFormalName());
		symptomTemplateModel.setDefinition(dbSymtomTeplate.getDefinition());
		symptomTemplateModel.setDisplayDrApp(dbSymtomTeplate.getDisplayDrApp());

		SymptomTemplate islabSymptom = symptomTemplateRepository.findLabSymptoms(symptomID);

		if (islabSymptom != null) {
			symptomTemplateModel.setLabSymptom(true);

			if (islabSymptom.getRange() != null) {
				isResultRange = islabSymptom.getRange().booleanValue();
			}

			Set<SymptomLabOrders> labOrders = dbSymtomTeplate.getLabsOrdered();

			if (labOrders != null && !labOrders.isEmpty()) {
				symptomTemplateModel.setLabsOrdered(
						labOrders.stream().map(SymptomLabOrders::getOrderID).collect(Collectors.toList()));
			}

			if (isResultRange) {
				symptomTemplateModel.setMaxRange(dbSymtomTeplate.getMaxRange());
				symptomTemplateModel.setMinRange(dbSymtomTeplate.getMinRange());
				// symptomTemplateModel.setLabResultsRange(isResultRange);
			}

			symptomTemplateModel.setLabResultsRange(isResultRange);

		} else {
			symptomTemplateModel.setLabSymptom(false);
		}

		Set<LogicalSymptomGroups> logicalSymptomGroups = dbSymtomTeplate.getLogicalGroups();

		if (logicalSymptomGroups != null && !logicalSymptomGroups.isEmpty()) {

			symptomTemplateModel.setGroupID(
					logicalSymptomGroups.stream().map(LogicalSymptomGroups::getGroupID).collect(Collectors.toList()));
		}

		AdtionalSettings root = getSymptomAdditionalInfo(dbSymtomTeplate);
		String mutliplier = dbSymtomTeplate.getMultipleValues();
		additionalInfoList.add(root);
		if (mutliplier != null && !mutliplier.isEmpty()) {
			populateDataStore(mutliplier, dbSymtomTeplate, additionalInfoList, lCode, isResultRange);

		}
		symptomTemplateModel.setAdditionalInfo(additionalInfoList);

		return symptomTemplateModel;
	}

	private AdtionalSettings getSymptomAdditionalInfo(SymptomTemplate dbSymtomTeplate) {
		Set<SnomedCodes> snomedCodesMap = dbSymtomTeplate.getSnomedCodes();
		Set<RCodeDataStore> rCodesMap = dbSymtomTeplate.getrCodeDataStores();
		List<SnomedCodeModel> snomdModelCodes = null;
		List<String> icd10rCodes = null;
		AdtionalSettings root = new AdtionalSettings();
		root.setAntithesis(dbSymtomTeplate.getAntithesis());
		root.setDisplaySymptom(dbSymtomTeplate.getDisplaySymptom());
		if (snomedCodesMap != null) {
			List<SnomedCodes> symptomSnomedCodes = snomedCodesMap.stream().filter(
					s -> s.getListValueCode() == null && s.getConceptID() != null && !s.getConceptID().isEmpty())
					.distinct().collect(Collectors.toList());
			if (symptomSnomedCodes != null && !symptomSnomedCodes.isEmpty()) {
				snomdModelCodes = new ArrayList<SnomedCodeModel>();
				for (SnomedCodes snomedCode : symptomSnomedCodes) {
					SnomedCodeModel snc = new SnomedCodeModel();
					snc.setSnomedCodes(snomedCode.getConceptID());
					snc.setSnomedName(snomedCode.getName());
					snomdModelCodes.add(snc);

				}

			}
		}

		if (rCodesMap != null && !rCodesMap.isEmpty()) {
			icd10rCodes = new ArrayList<String>();
			List<RCodeDataStore> roCodes = rCodesMap.stream().filter(s -> s.getDsCode() == null).distinct()
					.collect(Collectors.toList());
			for (RCodeDataStore rCodeDataStore : roCodes) {

				if (!StringUtils.isEmpty(rCodeDataStore.getM_icd10RCode())) {
					icd10rCodes.add(rCodeDataStore.getM_icd10RCode());
				}
			}
		}

		root.setIcd10RCodes(icd10rCodes);

		root.setSnomedCodes(snomdModelCodes);

		return root;
	}

	private void populateDataStore(String multipleValues, SymptomTemplate dbSymtomTeplate,
			List<AdtionalSettings> additionalInfoList, String lCode, boolean isResultRange) {
		DataKeys dataKeys = dataKeysRepository.findByName(multipleValues, 2);
		Set<SnomedCodes> snomedCodesMap = dbSymtomTeplate.getSnomedCodes();
		Set<RCodeDataStore> rCodesMap = dbSymtomTeplate.getrCodeDataStores();
		List<SnomedCodeModel> snomdModelCodes = null;
		List<String> icd10rCodes = null;
		if (dataKeys != null) {
			Set<DataStore> dbDataStoreSet = dataKeys.getDataStoreList();
			if (dbDataStoreSet != null) {
				Iterator<DataStore> dataStoreItr = dbDataStoreSet.iterator();
				while (dataStoreItr.hasNext()) {
					AdtionalSettings settings = new AdtionalSettings();
					DataStore dbDataStore = dataStoreItr.next();
					// antithesis.put(dbDataStore.getName(), dbDataStore.getM_antithesis());
					settings.setAntithesis(dbDataStore.getM_antithesis());
					settings.setOptionCode(dbDataStore.getCode());
					settings.setOptionDescription(dbDataStore.getName());
					settings.setDisplaySymptom(dbDataStore.getDisplayListValue());
					settings.setAnyOption(dbDataStore.getAnyOption());
					if (MICAConstants.SPANISH.equals(lCode)) {
						settings.setEs_optionDescription(dbDataStore.getEs_name());
						settings.setEs_kioskName(dbDataStore.getEs_kioskName());
					}

					if (isResultRange) {
						settings.setLowerLimit(dbDataStore.getLowerLimit());
						settings.setLowerLimitCondition(dbDataStore.getLowerLimitCondition());
						settings.setUpperLimit(dbDataStore.getUpperLimit());
						settings.setUpperLimitCondition(dbDataStore.getUpperLimitCondition());
						settings.setIsNormal(dbDataStore.getNormal());
					}

					List<SnomedCodes> symptomSnomedCodes = null;

					if (snomedCodesMap != null) {
						symptomSnomedCodes = snomedCodesMap.stream()
								.filter(s -> s.getListValueCode() != null
										&& s.getListValueCode().equalsIgnoreCase(dbDataStore.getCode()))
								.distinct().collect(Collectors.toList());
					}

					if (symptomSnomedCodes != null && !symptomSnomedCodes.isEmpty()) {
						snomdModelCodes = new ArrayList<SnomedCodeModel>();

						for (SnomedCodes snomedCode : symptomSnomedCodes) {
							SnomedCodeModel snc = new SnomedCodeModel();
							snc.setSnomedCodes(snomedCode.getConceptID());
							snc.setSnomedName(snomedCode.getName());
							snomdModelCodes.add(snc);
						}

					}

					if (rCodesMap != null && !rCodesMap.isEmpty()) {
						icd10rCodes = new ArrayList<String>();
						List<RCodeDataStore> roCodes = rCodesMap.stream().filter(
								s -> s.getDsCode() != null && s.getDsCode().equalsIgnoreCase(dbDataStore.getCode()))
								.distinct().collect(Collectors.toList());
						for (RCodeDataStore rCodeDataStore : roCodes) {
							icd10rCodes.add(rCodeDataStore.getM_icd10RCode());
						}
					}
					settings.setIcd10RCodes(icd10rCodes);
					settings.setSnomedCodes(snomdModelCodes);
					additionalInfoList.add(settings);

				}
			}

		}

	}

	@Override
	public SymptomTemplateModelV1 updateSymptomV1(SymptomTemplateModelV1 symptomTemplateModel, String languagecode) {
		String symptomID = symptomTemplateModel.getSymptomID();
		SymptomTemplate dbSymptomTemplate = symptomTemplateRepository.findByCode(symptomID, 2);
		SymptomTemplate islabSymptom = symptomTemplateRepository.findLabSymptoms(symptomID);
		// boolean labSymptom = false;
		boolean isResultRange = false;

		if (islabSymptom != null) {
			// labSymptom = true;
			if (islabSymptom.getRange() != null) {
				isResultRange = islabSymptom.getRange().booleanValue();
				dbSymptomTemplate.setRange(isResultRange);
			}
		}

		SymptomTemplateModelV1 symptom = new SymptomTemplateModelV1();
		Boolean updateFlag = false;
		if (dbSymptomTemplate == null) {
			throw new DataNotFoundException("No symptom data found for the SymptomID:" + symptomID);
		}

		Set<RCodeDataStore> rCodeDataStores = new HashSet<RCodeDataStore>();
		Set<SnomedCodes> snomedCodes = new HashSet<SnomedCodes>();
		// Set<String> invalidSCodes = new HashSet<String>();
		Set<String> invalidRCodes = new HashSet<String>();

		List<AdtionalSettings> additionalInfo = symptomTemplateModel.getAdditionalInfo();

		List<AdtionalSettings> symptomSettings = additionalInfo.stream().filter(s -> s.getOptionCode() == null)
				.distinct().collect(Collectors.toList());
		List<AdtionalSettings> dropDownSettings = additionalInfo.stream().filter(s -> s.getOptionCode() != null)
				.distinct().collect(Collectors.toList());

		// update symptom settings

		// updateSymptomSettings(symptomSettings,dbSymptomTemplate,rCodeDataStores,snomedCodes,invalidRCodes,invalidSCodes);

		updateSymptomSettings(symptomSettings, dbSymptomTemplate, rCodeDataStores, snomedCodes, invalidRCodes);

		// Map<String, Boolean> displayListValues =
		// symptomTemplateModel.getDisplayListValues();

		if (symptomTemplateModel.getCriticality() != null) {
			dbSymptomTemplate.setCriticality(symptomTemplateModel.getCriticality());
		}

		if (symptomTemplateModel.getQuestion() != null) {
			dbSymptomTemplate.setQuestionText(symptomTemplateModel.getQuestion());
		}

		if (symptomTemplateModel.getTimeType() != null) {
			dbSymptomTemplate.setTimeType(symptomTemplateModel.getTimeType().getText());
		}

		if (MICAConstants.SPANISH.equals(languagecode)) {
			dbSymptomTemplate.setEs_questionText(symptomTemplateModel.getEs_question());
		}

		if (symptomTemplateModel.getCardinality() != null) {
			dbSymptomTemplate.setCardinality(symptomTemplateModel.getCardinality());
		}

		if (symptomTemplateModel.getTreatable() != null) {
			dbSymptomTemplate.setTreatable(symptomTemplateModel.getTreatable());
		}

		if (symptomTemplateModel.getDefinition() != null) {
			dbSymptomTemplate.setDefinition(symptomTemplateModel.getDefinition());
		}

		if (isResultRange) {
			if (symptomTemplateModel.getMaxRange() != null) {
				dbSymptomTemplate.setMaxRange(symptomTemplateModel.getMaxRange());
			}

			if (symptomTemplateModel.getMinRange() != null) {
				dbSymptomTemplate.setMinRange(symptomTemplateModel.getMinRange());
			}

		}

		// if(symptomTemplateModel.getGroupID() != null ) {
		// dbSymptomTemplate.setLogicalGroupID(symptomTemplateModel.getGroupID());
		// }

		// dbSymptomTemplate.setGenderGroup(symptomTemplateModel.getGenderGroup());

		dbSymptomTemplate.setGenderGroup(String.valueOf(symptomTemplateModel.getGenderGroup()));

		if (symptomTemplateModel.getDisplayDrApp() != null) {
			dbSymptomTemplate.setDisplayDrApp(symptomTemplateModel.getDisplayDrApp());
		}

		dbSymptomTemplate.setUpdatedDate(new Date().getTime());

		String multiplier = dbSymptomTemplate.getMultipleValues();

		if (multiplier != null && !multiplier.isEmpty()) {
			// updateFlag=
			// updateDataStoreV1(multiplier,dropDownSettings,rCodeDataStores,snomedCodes,invalidRCodes,invalidSCodes);
			updateFlag = updateDataStoreV1(multiplier, dropDownSettings, rCodeDataStores, snomedCodes, invalidRCodes,
					isResultRange);
		}

		StringBuffer errorMessage = new StringBuffer();

		/*
		 * if(!invalidSCodes.isEmpty()) { errorMessage.append(invalidSCodes +
		 * " is/are invalid SnomedCodes, Please enter correct codes. \n" ); }
		 */

		if (!invalidRCodes.isEmpty()) {
			errorMessage.append(invalidRCodes + " is/are invalid icd10RCodes, Please enter correct codes.\n");

		}

		if (errorMessage.length() > 0) {

			throw new DataCreateException(errorMessage.toString());
		}

		// if(! rCodeDataStores.isEmpty()) {
		updateFlag = saveRCodes(symptomID, rCodeDataStores, dbSymptomTemplate);
		// }
		// if( !snomedCodes.isEmpty()) {
		updateFlag = saveSnomedCodes(symptomID, snomedCodes, dbSymptomTemplate);

		// }

		updateFlag = saveLogicalSymptomsGroups(symptomID, symptomTemplateModel.getGroupID(), dbSymptomTemplate);

		updateFlag = saveLabOrders(symptomID, symptomTemplateModel.getLabsOrdered(), dbSymptomTemplate);

		SymptomTemplate afterSaveSymptomTemplate = symptomTemplateRepository.save(dbSymptomTemplate, 2);

		if (afterSaveSymptomTemplate != null) {
			symptom.setSymptomID(dbSymptomTemplate.getCode());
			;
			symptom.setStatus("Symptom updated successfully.");
		}

		if (updateFlag || afterSaveSymptomTemplate != null) {
			updateGroupsTimeStamp(dbSymptomTemplate.getCode());

		}

		return symptom;

	}

	private Boolean saveLabOrders(String symptomID, List<Integer> labOrders, SymptomTemplate dbSymptomTemplate) {

		/*
		 * SymptomTemplate st = symptomTemplateRepository.findLabSymptoms(symptomID);
		 * 
		 * if(st==null) { throw new DataNotFoundException(symptomID +
		 * " is not a lab symptom. "); }
		 */

		symptomTemplateRepository.deleteSymptomLabOrder(symptomID);

		if (labOrders != null && !labOrders.isEmpty()) {
			Set<SymptomLabOrders> labOrderstoSave = new HashSet<SymptomLabOrders>();
			for (int i = 0; i < labOrders.size(); i++) {
				SymptomLabOrders laborder = new SymptomLabOrders();
				laborder.setOrderID(labOrders.get(i));
				labOrderstoSave.add(laborder);
			}
			if (!labOrderstoSave.isEmpty()) {
				dbSymptomTemplate.setLabsOrdered(labOrderstoSave);
				return true;
			}
			return false;

		}
		return false;

		/*
		 * 
		 * 
		 * SymptomTemplate st = symptomTemplateRepository.findLabSymptoms(symptomID);
		 * 
		 * if(st==null) { throw new DataNotFoundException(symptomID +
		 * " is not a lab symptom. "); }
		 * symptomTemplateRepository.deleteSymptomLabOrder(symptomID); if(labOrders !=
		 * null && ! labOrders.isEmpty()) { Set<SymptomLabOrders> labsOrderedModel = new
		 * HashSet<SymptomLabOrders>(); for (String labOrderStr : labOrders) {
		 * SymptomLabOrders labOrder = new SymptomLabOrders();
		 * labOrder.setName(labOrderStr); labsOrderedModel.add(labOrder); }
		 * if(!labsOrderedModel.isEmpty()){
		 * dbSymptomTemplate.setLabsOrdered(labsOrderedModel); return true; } return
		 * false;
		 * 
		 * } return false;
		 * 
		 */}

	private Boolean saveSnomedCodes(String symptomID, Set<SnomedCodes> snomedCodes, SymptomTemplate dbSymptomTemplate) {
		symptomTemplateRepository.deleteSnomdedCodes(symptomID);
		if (dbSymptomTemplate != null && !snomedCodes.isEmpty()) {
			dbSymptomTemplate.setSnomedCodes(snomedCodes);
			return true;
		}
		return false;
	}

	/*
	 * private Boolean saveLogicalSymptomsGroups(String symptomID, List<Integer>
	 * logicalGroups , SymptomTemplate dbSymptomTemplate) {
	 * symptomTemplateRepository.deleteLogicalSymptomsGroups(symptomID);
	 * if(logicalGroups != null && ! logicalGroups.isEmpty()) {
	 * Set<LogicalSymptomGroups> logicalGroupsToSave = new
	 * HashSet<LogicalSymptomGroups>(); for (int i = 0; i < logicalGroups.size();
	 * i++) { LogicalSymptomGroups logicalGroup = new LogicalSymptomGroups();
	 * logicalGroup.setGroupID(logicalGroups.get(i));
	 * logicalGroupsToSave.add(logicalGroup); } if(!logicalGroupsToSave.isEmpty()){
	 * dbSymptomTemplate.setLogicalGroups(logicalGroupsToSave); return true; }
	 * return false;
	 * 
	 * } return false;
	 * 
	 * }
	 */

	private Boolean saveLogicalSymptomsGroups(String symptomID, List<Integer> logicalGroups,
			SymptomTemplate dbSymptomTemplate) {
		Set<LogicalSymptomGroups> logicalDbGroups = dbSymptomTemplate.getLogicalGroups();
		if (logicalGroups != null && !logicalGroups.isEmpty()) {
			Set<LogicalSymptomGroups> logicalGroupsToSave = new HashSet<LogicalSymptomGroups>();
			for (int i = 0; i < logicalGroups.size(); i++) {
				LogicalSymptomGroups logicalGroup = new LogicalSymptomGroups();
				Integer groupID = logicalGroups.get(i);
				logicalGroup.setGroupID(groupID);
				if (logicalDbGroups != null && !logicalDbGroups.isEmpty()) {
					LogicalSymptomGroups logicalDbGroup = logicalDbGroups.stream()
							.filter(s -> s.getGroupID() == groupID).findAny().orElse(null);
					if (logicalDbGroup != null) {
						logicalGroup.setGroupFlag(logicalDbGroup.getGroupFlag());
					}
				}
				logicalGroupsToSave.add(logicalGroup);
			}
			if (!logicalGroupsToSave.isEmpty()) {
				symptomTemplateRepository.deleteLogicalSymptomsGroups(symptomID);
				dbSymptomTemplate.setLogicalGroups(logicalGroupsToSave);
				return true;
			}
			return false;

		}
		return false;

	}

	private Boolean saveRCodes(String symptomID, Set<RCodeDataStore> rCodeDataStores,
			SymptomTemplate dbSymptomTemplate) {
		symptomTemplateRepository.deleteRCodeDataStore(symptomID);
		if (dbSymptomTemplate != null && !rCodeDataStores.isEmpty()) {
			dbSymptomTemplate.setrCodeDataStores(rCodeDataStores);
			return true;
		}

		return false;

	}

	/*
	 * private Boolean updateDataStoreV1(String multiplier, List<AdtionalSettings>
	 * dropDownSettings,Set<RCodeDataStore> rCodeDataStores, Set<SnomedCodes>
	 * snomedCodes, Set<String> invalidRCodes, Set<String> invalidSCodes) {
	 */
	private Boolean updateDataStoreV1(String multiplier, List<AdtionalSettings> dropDownSettings,
			Set<RCodeDataStore> rCodeDataStores, Set<SnomedCodes> snomedCodes, Set<String> invalidRCodes,
			boolean isResultRange) {
		DataKeys dataKeys = dataKeysRepository.findByName(multiplier, 1);
		Boolean updateFlag = false;
		Boolean dataStoreFlag = false;
		if (dataKeys != null) {
			Set<DataStore> dataStoreList = dataKeys.getDataStoreList();
			if (dataStoreList != null) {
				Iterator<DataStore> dataStoreItr = dataStoreList.iterator();
				while (dataStoreItr.hasNext()) {
					DataStore dbDataStore = dataStoreItr.next();
					String dsCode = dbDataStore.getCode();
					AdtionalSettings drodDrownsetting = dropDownSettings.stream()
							.filter(s -> s.getOptionCode().equalsIgnoreCase(dsCode)).findAny().orElse(null);
					if (drodDrownsetting != null) {
						if (drodDrownsetting.getAntithesis() != null) {
							dbDataStore.setM_antithesis(drodDrownsetting.getAntithesis());
							dataStoreFlag = true;
						}

						List<String> listRCodes = drodDrownsetting.getIcd10RCodes();
						if (listRCodes != null && !listRCodes.isEmpty()) {
							Set<String> invalidIcd10Codes = validateRCodes(listRCodes);
							if (!invalidIcd10Codes.isEmpty()) {
								invalidRCodes.addAll(invalidIcd10Codes);
							}

							if (invalidIcd10Codes.isEmpty()) {
								for (String listRCode : listRCodes) {
									RCodeDataStore rCodeDataStore = new RCodeDataStore();
									rCodeDataStore.setM_icd10RCode(listRCode);
									rCodeDataStore.setDsCode(dsCode);
									rCodeDataStores.add(rCodeDataStore);
								}
							}
						}

						if (drodDrownsetting.getDisplaySymptom() != null) {
							dbDataStore.setDisplayListValue(drodDrownsetting.getDisplaySymptom());
							updateFlag = true;
						}

						if (drodDrownsetting.getAnyOption() != null) {
							dbDataStore.setAnyOption(drodDrownsetting.getAnyOption());
							updateFlag = true;
						}

						if (isResultRange) {
							dbDataStore.setLowerLimit(drodDrownsetting.getLowerLimit());
							dbDataStore.setLowerLimitCondition(drodDrownsetting.getLowerLimitCondition());
							dbDataStore.setUpperLimit(drodDrownsetting.getUpperLimit());
							dbDataStore.setUpperLimitCondition(drodDrownsetting.getUpperLimitCondition());
							dbDataStore.setNormal(drodDrownsetting.getIsNormal());
							updateFlag = true;
						}

						List<SnomedCodeModel> modelSnomedCodes = drodDrownsetting.getSnomedCodes();

						if (modelSnomedCodes != null && !modelSnomedCodes.isEmpty()) {
							for (SnomedCodeModel snomedCodeModel : modelSnomedCodes) {
								// List<Long> snomeds = snomedCodeModel.getSnomedCodes();
								// Set<String> invalidSnomeds = validateSnomedCodes(snomeds);
								// if(invalidSnomeds.isEmpty()) {
								SnomedCodes snomedCode = new SnomedCodes();
								snomedCode.setListValueCode(dsCode);
								snomedCode.setConceptID(snomedCodeModel.getSnomedCodes());
								snomedCode.setName(snomedCodeModel.getSnomedName());
								snomedCodes.add(snomedCode);
								/*
								 * } else{ invalidSCodes.addAll(invalidSnomeds); }
								 */

							}
						}
					}

				}
			}
		}

		if (dataStoreFlag) {
			dataKeysRepository.save(dataKeys, 1);
		}
		return updateFlag;
	}

	/*
	 * private void updateSymptomSettings(List<AdtionalSettings> symptomSettings,
	 * SymptomTemplate dbSymptomTemplate, Set<RCodeDataStore> rCodeDataStores,
	 * Set<SnomedCodes> snomedCodes, Set<String> invalidRCodes, Set<String>
	 * invalidSCodes) {
	 */
	private void updateSymptomSettings(List<AdtionalSettings> symptomSettings, SymptomTemplate dbSymptomTemplate,
			Set<RCodeDataStore> rCodeDataStores, Set<SnomedCodes> snomedCodes, Set<String> invalidRCodes) {
		if (symptomSettings != null && !symptomSettings.isEmpty()) {
			AdtionalSettings symptomSetting = symptomSettings.get(0);
			if (symptomSetting.getAntithesis() != null) {
				dbSymptomTemplate.setAntithesis(symptomSetting.getAntithesis());
			}
			if (symptomSetting.getDisplaySymptom() != null) {
				dbSymptomTemplate.setDisplaySymptom(symptomSetting.getDisplaySymptom());
			}

			List<String> symptomRCodes = symptomSetting.getIcd10RCodes();
			if (symptomRCodes != null && !symptomRCodes.isEmpty()) {
				Set<String> invalidIcd10Codes = validateRCodes(symptomRCodes);
				if (!invalidIcd10Codes.isEmpty()) {
					invalidRCodes.addAll(invalidIcd10Codes);
				}

				if (invalidIcd10Codes.isEmpty()) {
					for (String rCode : symptomRCodes) {
						RCodeDataStore rCodeDataStore = new RCodeDataStore();
						rCodeDataStore.setM_icd10RCode(rCode);
						rCodeDataStores.add(rCodeDataStore);
					}
				}
			}

			List<SnomedCodeModel> modelSnomedCodes = symptomSetting.getSnomedCodes();
			if (modelSnomedCodes != null && !modelSnomedCodes.isEmpty()) {
				for (SnomedCodeModel snomedCodeModel : modelSnomedCodes) {
					// List<Long> snomeds = snomedCodeModel.getSnomedCodes();
					// Set<String> invalidSnomeds = validateSnomedCodes(snomeds);
					// if(invalidSnomeds.isEmpty()) {
					SnomedCodes snomedCode = new SnomedCodes();
					snomedCode.setConceptID(snomedCodeModel.getSnomedCodes());
					snomedCode.setName(snomedCodeModel.getSnomedName());
					snomedCodes.add(snomedCode);
					/*
					 * } else{ invalidSCodes.addAll(invalidSnomeds); }
					 */
				}

			}

		}

	}

	/*
	 * private Set<String> validateSnomedCodes(List<Long> snmedCodes) { Set<String>
	 * invalidSCodes = new HashSet<String>(); if(snmedCodes != null && !
	 * snmedCodes.isEmpty()) { for (Long scode : snmedCodes) {
	 * if(!validateSnomedCode(scode)) { invalidSCodes.add(Long.toString(scode)); }
	 * 
	 * } } return invalidSCodes;
	 * 
	 * }
	 */

	private Set<String> validateRCodes(List<String> symptomRCodes) {
		Set<String> invalidRCodes = new HashSet<String>();
		if (symptomRCodes != null && !symptomRCodes.isEmpty()) {
			for (String rCode : symptomRCodes) {
				if (!validateRCodeWithMasterCode(rCode)) {
					invalidRCodes.add(rCode);
				}
			}
		}
		logger.info("In valid Rcodes" + invalidRCodes);
		return invalidRCodes;
	}

	/*
	 * private Boolean validateSnomedCode(Long snomedCode){
	 * 
	 * RestTemplate restTemplate = new RestTemplate();
	 * 
	 * Boolean status = false;
	 * 
	 * ObjectConcept response = null; try { StringBuffer base_url = new
	 * StringBuffer(snomedBaseUrl).append("/snomed-rest/snomed/code?sctid=").append(
	 * snomedCode); logger.info("snomedBase URL :: " + base_url); response =
	 * restTemplate.getForObject(base_url.toString(), ObjectConcept.class); } catch
	 * (HttpServerErrorException e) { throw new
	 * ServiceNotAvailableException("Snomed services are down");
	 * 
	 * }
	 * 
	 * if(response != null ) {
	 * 
	 * Long sctid = Long.parseLong(response.getSctid());
	 * if(sctid.longValue()==snomedCode.longValue()){ status = true; } }
	 * 
	 * 
	 * logger.info("snomedBase Response :: " + response); return status; }
	 */

	private Boolean validateRCodeWithMasterCode(String rCode) {
		String masterRcode = symptomRepository.getMasterIcd10Code(rCode);
		logger.info("masterRcode :: " + masterRcode);
		if (masterRcode != null) {
			return true;
		}
		return false;

	}

	@Override
	public GenericSymptomPagination getSymptomTemplatesWithPaging(String groupName, String name, Integer skip,
			Integer limit) {

		GenericSymptomPagination symptomsPages = new GenericSymptomPagination();

		Integer totalRecords = null;
		List<SymptomTemplate> symptomsDbPages = null;

		// PageRequest pageable = null;
		List<String> categories = symptomTemplateRepository.getCategoriesByGroup(groupName.toUpperCase());

		if (skip != null && limit != null) {

			skip = skip - 1;
			// pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "st.code");
		} else {
			skip = 0;
			limit = 25;
			// pageable = PageRequest.of(0, 25, Sort.Direction.ASC, "st.code");
		}
		if (name != null) {
			if (name.length() < 3) {
				throw new DataInvalidException("Symptom name should be greater than 3 characters.");
			}
			symptomsDbPages = symptomTemplateRepository.findByCategoryAndName(categories, name.toUpperCase(), skip,
					limit);
			totalRecords = symptomTemplateRepository.findByCategoryAndNameCount(categories, name.toUpperCase());
		} else {
			symptomsDbPages = symptomTemplateRepository.findByCategory(categories, skip, limit);
			totalRecords = symptomTemplateRepository.findByCategoryCount(categories);
		}
		if (symptomsDbPages != null) {
			// List<SymptomTemplate> slices = symptomsDbPages.getContent();
			// if(slices != null ){
			List<GenericSymptomModel> content = new ArrayList<GenericSymptomModel>();
			for (SymptomTemplate slice : symptomsDbPages) {
				GenericSymptomModel genericSymptomModel = new GenericSymptomModel();
				genericSymptomModel.setName(slice.getName());
				genericSymptomModel.setCode(slice.getCode());
				// genericSymptomModel.setBodyPart(slice.getBodyPart());
				// genericSymptomModel.setSource(slice.getSource());
				genericSymptomModel.setName(slice.getName());
				genericSymptomModel.setDataStoreTemplates(slice.getDataStoreTemplates());
				genericSymptomModel.setCriticality(slice.getCriticality());
				genericSymptomModel.setTreatable(slice.getTreatable());
				// genericSymptomModel.setQuestionText(slice.getQuestionText());
				genericSymptomModel.setQuestion(slice.getQuestionText());
				if (slice.getTimeType() != null) {
					genericSymptomModel.setTimeType(TimeType.fromText(slice.getTimeType()));
				}
				genericSymptomModel.setAntithesis(slice.getAntithesis());
				genericSymptomModel.setDisplayOrder(slice.getDisplayOrder());
				genericSymptomModel.setDisplaySymptom(slice.getDisplaySymptom());
				genericSymptomModel.setDisplayDrApp(slice.getDisplayDrApp());
				genericSymptomModel.setBias(slice.getBias());

				content.add(genericSymptomModel);
			}
			symptomsPages.setContent(content);
			// }

			if (symptomsPages.getContent() != null && !symptomsPages.getContent().isEmpty()) {
				symptomsPages.setFirst(true);
				symptomsPages.setLast(false);
				symptomsPages.setPagenumber(skip);
				symptomsPages.setElementsInPage(limit);
				symptomsPages.setPageSize(limit);
				symptomsPages.setTotalElements(totalRecords);
			}
		}
		return symptomsPages;

	}

	@Override
	public GenericSymptomGroups symptomsByCategory(String groupId, String lCode) {
		GenericSymptomGroups groups = new GenericSymptomGroups();
		SymptomGroups dbGroupgs = getSymptomsByGroup(groupId, lCode);
		groups.setCode(dbGroupgs.getCode());
		groups.setName(dbGroupgs.getName());
		groups.setGroupID(dbGroupgs.getGroupID());
		groups.setUpdatedDate(dbGroupgs.getUpdatedDate());
		if (MICAConstants.SPANISH.equals(lCode)) {
			groups.setEs_name(dbGroupgs.getEs_name());
		}

		groups.setCategories(getCategoryList(dbGroupgs.getCategories(), lCode));

		// TODO Auto-generated method stub
		return groups;
	}

	private List<GenericCategoryModel> getCategoryList(List<CategoryModel> dbCategories, String lCode) {
		List<GenericCategoryModel> modelCategories = new ArrayList<GenericCategoryModel>();
		if (dbCategories != null && !dbCategories.isEmpty()) {
			for (CategoryModel dbCategory : dbCategories) {
				GenericCategoryModel modelCategory = new GenericCategoryModel();
				if (MICAConstants.SPANISH.equals(lCode)) {
					modelCategory.setEs_name(dbCategory.getEs_name());
				}
				modelCategory.setCategoryID(dbCategory.getCategoryID());
				modelCategory.setName(dbCategory.getName());
				modelCategory.setSymptoms(getSymptomTemplates(dbCategory.getSymptoms(), lCode));
				modelCategories.add(modelCategory);
			}
		}

		return modelCategories;
	}

	private List<SymptomsTmplModel> getSymptomTemplates(List<Symptoms> dBsymptoms, String lCode) {

		List<SymptomsTmplModel> modelSymptoms = new ArrayList<SymptomsTmplModel>();
		if (dBsymptoms != null && !dBsymptoms.isEmpty()) {
			for (Symptoms dbSymptom : dBsymptoms) {
				SymptomsTmplModel sym = new SymptomsTmplModel();
				if (MICAConstants.SPANISH.equals(lCode)) {
					sym.setEs_name(dbSymptom.getEs_name());
				}
				sym.setName(dbSymptom.getName());
				sym.setSymptomID(dbSymptom.getSymptomID());
				// sym.setQuestionText(dbSymptom.getQuestion());
				sym.setQuestion(dbSymptom.getQuestion());
				sym.setAntithesis(dbSymptom.getAntithesis());
				sym.setCriticality(dbSymptom.getCriticality());
				sym.setDisplayDrApp(dbSymptom.getDisplayDrApp());
				sym.setDisplaySymptom(dbSymptom.getDisplaySymptom());

				if (MICAConstants.SPANISH.equals(lCode)) {
					sym.setEs_question(dbSymptom.getEs_question());
				}
				sym.setTreatable(dbSymptom.getTreatable());

				// logger.info("Name :: " + dbSymptom.getName());
				// logger.info("Code :: " + dbSymptom.getSymptomID());
				modelSymptoms.add(sym);
			}

		}

		return modelSymptoms;
	}

	@Override
	public SymptomsMainModel getSymptomBasicInfo() {

		SymptomsMainModel symptomsMainModel = new SymptomsMainModel();
		symptomsMainModel.setSymptoms(getSymptomTeplatesWithSnomedCodes());
		symptomsMainModel.setSymptomOptions(getAllMultipliers());
		// TODO Auto-generated method stub
		return symptomsMainModel;
	}

	private List<SymptomTemplateDTO> getSymptomTeplatesWithSnomedCodes() {

		Map<Integer, Object> groups = getClassfications();
		List<SymptomTemplateDTO> dtoSymptoms = new ArrayList<SymptomTemplateDTO>();
		List<GenericQueryResultEntity> symptomsResults = symptomTemplateRepository.getSymptomsWithSnomedCodes();
		if (symptomsResults != null && !symptomsResults.isEmpty()) {
			for (GenericQueryResultEntity result : symptomsResults) {
				SymptomTemplateDTO symptomdto = new SymptomTemplateDTO();
				symptomdto.setCode(result.getSymptomID());
				symptomdto.setName(result.getSymptomName());
				symptomdto.setImageName(result.getImageName());
				symptomdto.setSubGroups(result.getSubGroups());

				// if(result.getGroupID() != null) {
				symptomdto.setLogicalGroupNames(getMasterGroupNames(result.getLogicalGroupIDList(), groups));
				// }

				symptomdto.setSnomedCodes(result.getLiteralMap());
				symptomdto.setOptionKey(result.getOptionKey());
				symptomdto.setSymptomType(updateTypes(result.getReferenceTypes(), result.getMultipleValues()));
				dtoSymptoms.add(symptomdto);
			}
		}
		return dtoSymptoms;

	}

	private List<Object> getMasterGroupNames(List<Integer> groupIds, Map<Integer, Object> groups) {
		if (groupIds != null && !groupIds.isEmpty() && !groups.isEmpty()) {
			List<Object> groupNames = groups.entrySet().stream()
					.filter(entry -> groupIds.contains(entry.getKey().intValue())).map(Map.Entry::getValue)
					.collect(Collectors.toList());
			return groupNames;
		}

		return null;
	}

	private String updateTypes(List<String> dataStoreValues, String multiplierKey) {
		if (dataStoreValues != null && !dataStoreValues.isEmpty()) {
			if (multiplierKey != null) {
				if (dataStoreValues.contains(multiplierKey)
						&& dataStoreValues.containsAll(MICAUtil.DATA_STORE_SIMPLE_TIME)) {
					return MICAUtil.LIST_TIME;
				} else if (dataStoreValues.contains(multiplierKey)
						&& dataStoreValues.containsAll(MICAUtil.DATA_STORE_SIMPLE)) {
					return MICAUtil.LIST;
				}
			} else if (dataStoreValues.containsAll(MICAUtil.DATA_STORE_SIMPLE_TIME)) {
				return MICAUtil.SIMPLE_TIME;

			} else {
				return MICAUtil.SIMPLE;

			}

		}

		return null;

	}

	/**
	 * 
	 * @param symptomTempaltes
	 * @return
	 */
	private Map<String, Collection<Map<String, Object>>> getAllMultipliers() {
		Map<String, Collection<Map<String, Object>>> multipliers = new HashMap<String, Collection<Map<String, Object>>>();
		List<GenericQueryResultEntity> symptomTempaltes = dataStoreRepository.getAllMultipliers();

		for (GenericQueryResultEntity genericQueryResultEntity : symptomTempaltes) {
			multipliers.put(genericQueryResultEntity.getOptionKey(), genericQueryResultEntity.getLiteralMap());
		}

		return multipliers;

	}

	@Override
	public List<SymptomTemplateDTO> getAllSymptomQuestions() {
		List<SymptomTemplate> dbTemplates = symptomTemplateRepository.getAllQuestions();
		List<SymptomTemplateDTO> modelTemplates = new ArrayList<SymptomTemplateDTO>();
		if (dbTemplates != null) {
			for (SymptomTemplate symptomTemplate : dbTemplates) {
				SymptomTemplateDTO model = new SymptomTemplateDTO();
				model.setCode(symptomTemplate.getCode());
				model.setName(symptomTemplate.getName());
				model.setEs_name(symptomTemplate.getEs_name());
				model.setQuestion(symptomTemplate.getQuestionText());
				model.setEs_question(symptomTemplate.getEs_questionText());
				modelTemplates.add(model);
			}
		}

		return modelTemplates;
	}

	@Override
	public SymptomTemplateDTO getSymptomQuestion(String symptomID) {
		SymptomTemplate symptomTemplate = symptomTemplateRepository.getSymptomQuestion(symptomID.toUpperCase());
		SymptomTemplateDTO model = new SymptomTemplateDTO();
		if (symptomTemplate != null) {
			model.setCode(symptomTemplate.getCode());
			model.setName(symptomTemplate.getName());
			model.setEs_name(symptomTemplate.getEs_name());
			model.setQuestion(symptomTemplate.getQuestionText());
			model.setEs_question(symptomTemplate.getEs_questionText());
		}

		return model;
	}

	/*
	 * @Override public List<GenericQueryResultEntity> searchSymptomsByName(String
	 * name) { List<GenericQueryResultEntity> finalSymptoms = new
	 * ArrayList<GenericQueryResultEntity>(); if(name != null){ String fuzzySearch =
	 * name.toUpperCase().concat("~").concat("0.7") ; List<GenericQueryResultEntity>
	 * symptoms= symptomTemplateRepository.searchSymptoms(fuzzySearch); // Sort
	 * symptoms based on score Comparator<GenericQueryResultEntity> scoreCommparator
	 * = Comparator.comparing(GenericQueryResultEntity::getScore);
	 * List<GenericQueryResultEntity> listSorted =
	 * symptoms.stream().sorted(scoreCommparator.reversed()).collect(Collectors.
	 * toList());
	 * 
	 * Map<String, String> symptomGroupsMap = new HashMap<String, String>();
	 * Set<String> symptomGrouIds = listSorted.stream().filter(s->s.getSymptomID()
	 * != null).map(s->s.getSymptomID()).distinct().collect(Collectors.toSet());
	 * if(symptomGrouIds != null && ! symptomGrouIds.isEmpty()) { symptomGroupsMap =
	 * getSymptomSubGroups(symptomGrouIds); } for (GenericQueryResultEntity
	 * genericQueryResultEntity : listSorted) { // Remove duplicates Set<String>
	 * symptomIds = finalSymptoms.stream().filter(s->s.getSymptomID() !=
	 * null).map(s->s.getSymptomID()).collect(Collectors.toSet()); if(symptomIds!=
	 * null && ! symptomIds.contains(genericQueryResultEntity.getSymptomID())) {
	 * String groupName =
	 * symptomGroupsMap.get(genericQueryResultEntity.getSymptomID()); if(groupName
	 * != null) { genericQueryResultEntity.setGroupName(groupName); }
	 * finalSymptoms.add(genericQueryResultEntity); } } } return finalSymptoms; }
	 */

	@Override
	public List<GenericQueryResultEntity> searchSymptomsByName(String name) {
		List<GenericQueryResultEntity> finalSymptoms = new ArrayList<GenericQueryResultEntity>();
		List<GenericQueryResultEntity> symptomGroups = null;
		if (name != null) {
			String fuzzySearch = name.toUpperCase().concat("~").concat("0.7");
			List<GenericQueryResultEntity> symptoms = symptomTemplateRepository.searchSymptoms(fuzzySearch);
			// Sort symptoms based on score
			Comparator<GenericQueryResultEntity> scoreCommparator = Comparator
					.comparing(GenericQueryResultEntity::getScore);
			List<GenericQueryResultEntity> listSorted = symptoms.stream().sorted(scoreCommparator.reversed())
					.collect(Collectors.toList());

			// Map<String, String> symptomGroupsMap = new HashMap<String, String>();
			Set<String> symptomGrouIds = listSorted.stream().filter(s -> s.getSymptomID() != null)
					.map(s -> s.getSymptomID()).distinct().collect(Collectors.toSet());

			if (symptomGrouIds != null && !symptomGrouIds.isEmpty()) {
				symptomGroups = symptomTemplateRepository.getSubGroupSymptoms(symptomGrouIds);
			}
			for (GenericQueryResultEntity genericQueryResultEntity : listSorted) {
				// Remove duplicates
				Set<String> symptomIds = finalSymptoms.stream().filter(s -> s.getSymptomID() != null)
						.map(s -> s.getSymptomID()).collect(Collectors.toSet());
				if (symptomIds != null && !symptomIds.contains(genericQueryResultEntity.getSymptomID())) {
					if (symptomGroups != null) {
						GenericQueryResultEntity symptomGroup = symptomGroups.stream()
								.filter(s -> s.getSymptomID().equals(genericQueryResultEntity.getSymptomID())).findAny()
								.orElse(null);
						if (symptomGroup != null) {
							if (symptomGroup.getSymptomGroupName().equalsIgnoreCase("pain")
									|| symptomGroup.getSymptomGroupName().equalsIgnoreCase("swelling")) {
								if (symptomGroup.getSubGroups().get(2) != "Specifics") {
									genericQueryResultEntity.setGroupName(symptomGroup.getCategoryName() + " "
											+ StringUtils.capitalize(symptomGroup.getSymptomGroupName()));
								}

								if (symptomGroup.getPainSwellingID() != null) {
									if (symptomGroup.getPainSwellingID() == 8
											|| symptomGroup.getPainSwellingID() == 600) {
										genericQueryResultEntity.setSymptomName(
												StringUtils.remove(genericQueryResultEntity.getSymptomName(), "Basic"));
									}
								}

							} else {
								if (symptomGroup.getSymptomGroup() != null) {
									genericQueryResultEntity.setGroupName(symptomGroup.getSymptomGroup());

								}

							}

						}
					}

					finalSymptoms.add(genericQueryResultEntity);
				}
			}
		}
		return finalSymptoms;
	}

	/*
	 * @Override public String SaveOrUpdateSymptomGroups( LogicalSymptomGroupsModel
	 * logicalSymptomGroupsModel) {
	 * 
	 * Integer groupID =logicalSymptomGroupsModel.getGroupID();
	 * 
	 * List<String> symptoms =logicalSymptomGroupsModel.getSymptoms();
	 * 
	 * if(groupID == null) { throw new
	 * DataNotFoundException("GroupId should not be null."); }
	 * 
	 * if(symptoms != null && symptoms.isEmpty() ) { throw new
	 * DataNotFoundException("No symptoms availble to update."); }
	 * 
	 * LogicalSymptomGroupsRef dbGroup =
	 * logicalSymptomGroupsRefRepositoty.findByGroupID(groupID); if(dbGroup != null
	 * ){ List<String> restSymptoms =
	 * symptomTemplateRepository.resetSymptomsGroupID(logicalSymptomGroupsModel.
	 * getGroupID()); List<String> finalSymptoms =
	 * symptomTemplateRepository.updateSymptomsGroupID(groupID, symptoms);
	 * if(finalSymptoms != null && restSymptoms != null) { List<String>
	 * updateTimeSymptoms =
	 * Stream.concat(restSymptoms.stream(),finalSymptoms.stream()) .map(x->x)
	 * .distinct() .collect(Collectors.toList());
	 * 
	 * // update timestamp for group changed symptoms
	 * symptomGroupRepository.updateSymptomGroupTime(updateTimeSymptoms);
	 * 
	 * return "Symptoms group mapping done successfully "; }
	 * 
	 * } else { throw new DataNotFoundException("No matching group found"); }
	 * 
	 * return null; }
	 */

	@Override
	public String SaveOrUpdateSymptomGroups(LogicalSymptomGroupsModel logicalSymptomGroupsModel) {

		Integer groupID = logicalSymptomGroupsModel.getGroupID();

		// List<String> symptoms =logicalSymptomGroupsModel.getSymptoms();

		List<SymptomGroupResult> symptoms = logicalSymptomGroupsModel.getSymptoms();

		if (groupID == null) {
			throw new DataNotFoundException("GroupId should not be null.");
		}

		if (symptoms != null && symptoms.isEmpty()) {
			throw new DataNotFoundException("No symptoms availble to update.");
		}

		LogicalSymptomGroupsRef dbGroup = logicalSymptomGroupsRefRepositoty.findByGroupID(groupID);

		if (dbGroup != null) {
			List<String> restSymptoms = symptomTemplateRepository
					.deleteLogicalSymptomGroups(logicalSymptomGroupsModel.getGroupID());

			List<String> finalSymptoms = saveorUpdateSymptomGroups(symptoms, dbGroup);
			if (finalSymptoms != null && restSymptoms != null) {
				List<String> updateTimeSymptoms = Stream.concat(restSymptoms.stream(), finalSymptoms.stream())
						.map(x -> x).distinct().collect(Collectors.toList());

				// update timestamp for group changed symptoms
				symptomGroupRepository.updateSymptomGroupTime(updateTimeSymptoms);

				return "Symptoms group mapping done successfully ";
			}

		} else {
			throw new DataNotFoundException("No matching group found");
		}

		return null;
	}

	@Override
	public LogicalSymptomGroupsModel getSymptomsForGivenGroup(Integer groupID) {
		LogicalSymptomGroupsRef dbGroup = logicalSymptomGroupsRefRepositoty.findByGroupID(groupID);
		LogicalSymptomGroupsModel logicalSymptomGroupsModel = new LogicalSymptomGroupsModel();
		logicalSymptomGroupsModel.setGroupID(groupID);
		logicalSymptomGroupsModel.setType(dbGroup.getType());
		if (dbGroup != null) {
			List<SymptomGroupResult> sysmptoms = symptomTemplateRepository
					.getSymptomsForGivenGroup(dbGroup.getGroupID());
			if (sysmptoms != null && !sysmptoms.isEmpty()) {
				// logicalSymptomGroupsModel.setGroupID(dbGroup.getGroupID());
				logicalSymptomGroupsModel.setSymptoms(sysmptoms);
			}
		}

		/*
		 * else { throw new DataNotFoundException("No symptoms found for the Group"); }
		 * 
		 * } else { throw new DataNotFoundException("No matching group found"); }
		 */
		return logicalSymptomGroupsModel;
	}

	/*
	 * @Override public PaginationModel searchSymptomsByName(Integer page, Integer
	 * size, String name) {
	 * 
	 * 
	 * 
	 * if(name != null) { if(name.length() < 3) { throw new
	 * DataInvalidException("Symptom name should be greater than 3 characters."); }
	 * }
	 * 
	 * 
	 * 
	 * 
	 * PaginationModel illnessPages = new PaginationModel(); Page<SymptomTemplate>
	 * symptomsDbPages = getPagenationContentByName(illnessPages, page, size, name);
	 * if(symptomsDbPages != null) { List<SymptomTemplate> slices =
	 * symptomsDbPages.getContent(); if(slices != null ){ List
	 * <SymptomTemplateModel> symptomContents = new
	 * ArrayList<SymptomTemplateModel>(); for (SymptomTemplate slice : slices) {
	 * String symptomID = slice.getCode(); SymptomTemplateModel templateModel = new
	 * SymptomTemplateModel(); templateModel.setSymptomID(symptomID);
	 * templateModel.setName(slice.getName());
	 * templateModel.setActive(slice.getActive());
	 * //iCD10CodeModel.setUserID(userRepository.getUserID(source, icd10Code,
	 * version)); symptomContents.add(templateModel); }
	 * illnessPages.setSymptomContent(symptomContents); }
	 * 
	 * illnessPages.setFirst(symptomsDbPages.isFirst());
	 * illnessPages.setLast(symptomsDbPages.isLast());
	 * illnessPages.setPagenumber(symptomsDbPages.getNumber());
	 * illnessPages.setElementsInPage(symptomsDbPages.getNumberOfElements());
	 * illnessPages.setPageSize(symptomsDbPages.getSize());
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * return illnessPages;
	 * 
	 * 
	 * }
	 */

	/*
	 * private Page<SymptomTemplate> getPagenationContentByName( PaginationModel
	 * illnessPages, Integer page, Integer size, String name) {
	 * Page<SymptomTemplate> templateDbPages = null; Integer totalRecords = null;
	 * 
	 * if(page ==null ){ page=1; } if(size == null) { size = 50; } if(name != null){
	 * String fuzzySearch = name.toUpperCase().concat("~") ; PageRequest pageable =
	 * PageRequest.of(page - 1, size); templateDbPages =
	 * symptomTemplateRepository.searchSymptoms(fuzzySearch, pageable); totalRecords
	 * = symptomTemplateRepository.searchSymptomsCount(name).size(); //
	 * logger.info("Total no of records :: " + templateDbPages.getSize());
	 * logger.info("Total no of records :: " + totalRecords);
	 * 
	 * }
	 * 
	 * illnessPages.setTotalElements(totalRecords); return templateDbPages; }
	 */

	/*
	 * private Map<Integer, String> getClassfications() { GenericQueryResultEntity
	 * groupsUpdatedDates=
	 * logicalSymptomGroupsRefRepositoty.getLogicalSymptomGroups(); Map<Integer,
	 * String> classifications = null; if(groupsUpdatedDates != null) {
	 * classifications =
	 * groupsUpdatedDates.getLiteralMap().stream().collect(Collectors.toMap(s ->
	 * (Integer) s.get("logicalGroupID"), s -> (String) s.get("name")));
	 * 
	 * 
	 * } return classifications; }
	 */

	private Map<Integer, Object> getClassfications() {
		List<LogicalSymptomGroupsRef> logicalGroups = logicalSymptomGroupsRefRepositoty.getLogicalSystemGroups();
		Map<Integer, Object> classifications = null;
		if (logicalGroups != null) {
			classifications = logicalGroups.stream().collect(Collectors.toMap(LogicalSymptomGroupsRef::getGroupID,
					LogicalSymptomGroupsRef::getName, (oldValue, newValue) -> newValue));
		}
		return classifications;
	}

	/*
	 * private Map<String, String> getSymptomSubGroups( Set<String> symptomIds) {
	 * GenericQueryResultEntity dbSymptomGroups=
	 * symptomTemplateRepository.getSymptomGroups(symptomIds); Map<String, String>
	 * symptomGroups = null; if(dbSymptomGroups != null) { symptomGroups =
	 * dbSymptomGroups.getLiteralMap().stream().collect(Collectors.toMap(s ->
	 * (String) s.get("symptomID"), s -> (String) s.get("groupName"))); } return
	 * symptomGroups; }
	 */

	/*
	 * private List<String> saveorUpdateSymptomGroups(List<String> symptoms, Integer
	 * groupID) { List<String> finalSymptoms = new ArrayList<String>();
	 * 
	 * List<SymptomTemplate> symptomTemplates =
	 * symptomTemplateRepository.getSymptomTemaplesByGroups(symptoms);
	 * 
	 * if(symptomTemplates != null) { for (int i = 0; i < symptomTemplates.size();
	 * i++) { SymptomTemplate template = symptomTemplates.get(i);
	 * LogicalSymptomGroups logicalGroup = new LogicalSymptomGroups();
	 * logicalGroup.setGroupID(groupID); Set<LogicalSymptomGroups>
	 * logicalGroupsToSave = new HashSet<LogicalSymptomGroups>();
	 * logicalGroupsToSave.add(logicalGroup);
	 * template.getLogicalGroups().addAll(logicalGroupsToSave);
	 * finalSymptoms.add(template.getCode()); } }
	 * 
	 * symptomTemplateRepository.saveAll(symptomTemplates);
	 * 
	 * return finalSymptoms; }
	 */

	private List<String> saveorUpdateSymptomGroups(List<SymptomGroupResult> symptomGroups,
			LogicalSymptomGroupsRef groups) {
		List<String> finalSymptoms = new ArrayList<String>();

		List<String> symptoms = symptomGroups.stream().map(SymptomGroupResult::getSymptomID)
				.collect(Collectors.toList());

		List<SymptomTemplate> symptomTemplates = symptomTemplateRepository.getSymptomTemaplesByGroups(symptoms);

		if (symptomTemplates != null) {
			for (int i = 0; i < symptomTemplates.size(); i++) {
				SymptomTemplate template = symptomTemplates.get(i);

				SymptomGroupResult groupSymptoms = symptomGroups.stream()
						.filter(s -> s.getSymptomID().equals(template.getCode())).findAny().orElse(null);
				if (groupSymptoms != null) {
					LogicalSymptomGroups logicalGroup = new LogicalSymptomGroups();
					logicalGroup.setGroupID(groups.getGroupID());
					logicalGroup.setGroupFlag(groupSymptoms.getGroupFlag());
					Set<LogicalSymptomGroups> logicalGroupsToSave = new HashSet<LogicalSymptomGroups>();
					logicalGroupsToSave.add(logicalGroup);
					template.getLogicalGroups().addAll(logicalGroupsToSave);
					finalSymptoms.add(template.getCode());
				}
			}
		}

		symptomTemplateRepository.saveAll(symptomTemplates);

		return finalSymptoms;
	}
}
