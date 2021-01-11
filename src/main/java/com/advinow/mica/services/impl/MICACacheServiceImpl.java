package com.advinow.mica.services.impl;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.github.resilience4j.retry.annotation.Retry;

import com.advinow.mica.model.SymptomGroups;
import com.advinow.mica.model.cache.CacheGroupsTime;
import com.advinow.mica.repositories.SymptomGroupRepository;
import com.advinow.mica.services.MICACacheService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
@Retry(name = "neo4j")
public class MICACacheServiceImpl implements MICACacheService {


	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${server.cache.path}")
	String cachePath;

	@Autowired
	SymptomGroupRepository   symptomGroupRepository;





	public Map<String,Long> getChachedGroups(String lCode) {
		CacheGroupsTime  cacheGroupsTime = readGroupTimeMap(lCode);
		Map<String, Long> cachedGroups = cacheGroupsTime.getGroupsTimeMap();
		return cachedGroups;
	}



	@Override
	public void createGroupTimeMap(CacheGroupsTime dbGroupsTime,String lCode) {
		try {
			Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).disableHtmlEscaping().setPrettyPrinting().create();
			Path path = Paths.get(getGroupsTimePath(lCode));
			String json = gson.toJson(dbGroupsTime);
			Files.write(path,json.getBytes());
		} catch (IOException e) {
			logger.error("Error in createGroupTimeMap");
			e.printStackTrace();
		}

	}


	@Override
	public CacheGroupsTime  readGroupTimeMap(String lCode) {
		CacheGroupsTime cacheGroupsTime  = new CacheGroupsTime();
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
			File path = Paths.get(getGroupsTimePath(lCode)).toFile();
			cacheGroupsTime = gson.fromJson(new FileReader(path), CacheGroupsTime.class);
		} catch (IOException e) {
			String pathStr = getGroupsTimePath(lCode);
			logger.info("readGroupTimeMap(" + lCode + ") create new group time map path=" + pathStr);
			createGroupTimeMap(cacheGroupsTime,lCode);
		}
		return cacheGroupsTime;

	}

	@Override
	public void createSymptomCache(SymptomGroups symptomGroups,String groupName,String lCode) {
		try {
			Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).disableHtmlEscaping().setPrettyPrinting().create();
			Path path = Paths.get(getGroupsPath(groupName, lCode));
			String json = gson.toJson(symptomGroups);
			Files.write(path,json.getBytes());
		} catch (IOException e) {
			logger.info("File path not found.. "+ e.getMessage());
		}

	}

	@Override
	public SymptomGroups  loadSymptomsFromCache(String groupName,String lCode) {
		SymptomGroups symptomGroups  = null;
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
			File path = Paths.get(getGroupsPath(groupName, lCode)).toFile();
			symptomGroups = gson.fromJson(new FileReader(path), SymptomGroups.class);

		} catch (IOException e) {
			//e.printStackTrace();
			logger.info("File path not found.. "+ e.getMessage());
		}
		return symptomGroups;
	}

	private  String getGroupsPath(String groupName,String lCode) {
		return cachePath + "/"+groupName+"_"+lCode+".json";
	}


	private  String getGroupsTimePath(String lCode) {
		return cachePath + "/groupstime_"+lCode+".json";
	}



	@Override
	public Map<String,Long> getDrAppChachedGroups(String drName) {
		CacheGroupsTime  cacheGroupsTime = readDrAppGroupTimeMap(drName);
		Map<String, Long> cachedGroups = cacheGroupsTime.getGroupsTimeMap();
		return cachedGroups;
	}


	private CacheGroupsTime  readDrAppGroupTimeMap(String drName) {
		CacheGroupsTime cacheGroupsTime  = new CacheGroupsTime();
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
			File path = Paths.get(getGroupsTimeDrAppPath(drName)).toFile();
			cacheGroupsTime = gson.fromJson(new FileReader(path), CacheGroupsTime.class);

		} catch (IOException e) {
			createDrAppGroupTimeMap(cacheGroupsTime,drName);
		}
		return cacheGroupsTime;

	}


	@Override
	public void createDrAppGroupTimeMap(CacheGroupsTime dbGroupsTime,String drName) {
		try {
			Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).disableHtmlEscaping().setPrettyPrinting().create();
			Path path = Paths.get(getGroupsTimeDrAppPath(drName));
			String json = gson.toJson(dbGroupsTime);
			Files.write(path,json.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public SymptomGroups  loadDrAppSymptomsFromCache(String groupName,String drName) {
		SymptomGroups symptomGroups  = null;
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
			File path = Paths.get(getGroupsDrAppPath(groupName,drName)).toFile();
			symptomGroups = gson.fromJson(new FileReader(path), SymptomGroups.class);

		} catch (IOException e) {
			//e.printStackTrace();
			logger.info("File path not found.. "+ e.getMessage());
		}
		return symptomGroups;
	}

	private  String getGroupsDrAppPath(String groupName,String drName) {
		return cachePath +"/" +drName + "/"+groupName+".json";
	}


	private  String getGroupsTimeDrAppPath(String drName) {
		return cachePath +"/" +drName + "/groupstime_"+drName+".json";
	}

	@Override
	public void createDrAppSymptomCache(SymptomGroups symptomGroups,String groupName,String drName) {
		try {
			Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).disableHtmlEscaping().setPrettyPrinting().create();
			Path path = Paths.get(getGroupsDrAppPath(groupName, drName));
			String json = gson.toJson(symptomGroups);
			Files.write(path,json.getBytes());
		} catch (IOException e) {
			logger.info("File path not found.. "+ e.getMessage());
		}

	}

}
