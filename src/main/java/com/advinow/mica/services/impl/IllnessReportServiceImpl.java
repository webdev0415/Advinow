package com.advinow.mica.services.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.github.resilience4j.retry.annotation.Retry;

import com.advinow.mica.model.dataframe.DuplicateTimeSymptoms;
import com.advinow.mica.model.dataframe.TimeSymptoms;
import com.advinow.mica.repositories.IllnessReportRepository;
import com.advinow.mica.services.IllnessReportService;
import com.advinow.mica.util.DF_MICAUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;




@Service
@Retry(name = "neo4j")
public class IllnessReportServiceImpl implements IllnessReportService {/*

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	IllnessReportRepository illnessReportRepository;


	@Value("${aws.access_key_id}")
	String awsId ;
	@Value("${aws.secret_access_key}")
	String awsKey ;
	@Value("${aws.region}")
	String region ;
	@Value("${aws.bucket_name}")
	String bucketName ;
	@Value("${dataframe.fileWrite}")
	Boolean writefile ;
	@Value("${dataframe.s3Upload}")
	Boolean uploadtoS3 ;

	 Includes all the illnesses and symptoms in the data frame for false, selected illnesses and symptoms includes for true.
	@Value("${dataframe.preConfigIllnessData}")
	Boolean preConfig ;
	@Value("${filelocation}")
	String jsonLocation;

	@Value("${dataframe.name}")
	String dataFrameName;

	@Value("${dataframe.s3.relativepath}")
	String dataFrameRelPath;




	@Override
	public void getDuplicateTimeSymptoms() {
		List<DuplicateTimeSymptoms> finalDuplicates = new ArrayList<DuplicateTimeSymptoms>();
		List<TimeSymptoms>  timeSymptoms=	getTimeSymptoms();
		List<String> allSymtpoms = timeSymptoms.stream().map(TimeSymptoms::getSymptomID).distinct().collect(Collectors.toList());
	for (int i = 0; i < allSymtpoms.size(); i++) {	
		String sysmptomID = allSymtpoms.get(i);
		Map<Integer, List<DuplicateTimeSymptoms>> mdcMap = new Hashtable<Integer,List<DuplicateTimeSymptoms>>();
		List<TimeSymptoms> symptomData = timeSymptoms.stream().filter(s->s.getSymptomID().equals(sysmptomID)).collect(Collectors.toList());
		for (TimeSymptoms timeSymptom : symptomData) {
		   Integer key = Objects.hash(timeSymptom.getIcd10Code(),timeSymptom.getSymptomID(),timeSymptom.getMultiplier(),timeSymptom.getTimeFrame());
		   DuplicateTimeSymptoms test = new DuplicateTimeSymptoms();
		   test.setDmId(timeSymptom.getDmId());
		   test.setLikelihood(timeSymptom.getLikelihood());
		   test.setIcd10Code(timeSymptom.getIcd10Code());
		   test.setSymptomID(timeSymptom.getSymptomID());
		   test.setMultiplier(timeSymptom.getMultiplier());
		   test.setTimeFrame(timeSymptom.getTimeFrame());
		   test.setVersion(timeSymptom.getVersion());
		   test.setDsd(timeSymptom.getDsId());
		  
		   List<DuplicateTimeSymptoms> duplicates =    mdcMap.get(key);
		   if(duplicates== null  ) {
			   duplicates = new ArrayList<DuplicateTimeSymptoms>();
			   duplicates.add(test);
		   } else {
				   duplicates.add(test);
		   }
		   mdcMap.putIfAbsent(key, duplicates);
	   }
	   
		
		mdcMap.forEach((k,v)->{
			if(v.size() > 1) {
			DuplicateTimeSymptoms v1 = v.stream().max(Comparator.comparingDouble(DuplicateTimeSymptoms::getLikelihood)).get();
			v1.setDelete(false);
			for (int j = 0; j < v.size(); j++) {
				finalDuplicates.add(v.get(j));
			}
				}
		 });
	}
	
			
	createDuplicateTimeJson(finalDuplicates);
		
	}
	
	
	@Override
	public void getChronicTimeSymptoms() {
		List<TimeSymptoms> finalChronic = new ArrayList<TimeSymptoms>();
		List<TimeSymptoms>  timeSymptoms=	getCTimeSymptoms();
		List<String> allSymtpoms = timeSymptoms.stream().map(TimeSymptoms::getSymptomID).distinct().collect(Collectors.toList());
	for (int i = 0; i < allSymtpoms.size(); i++) {	
		String sysmptomID = allSymtpoms.get(i);
		Map<Integer, List<TimeSymptoms>> mdcMap = new Hashtable<Integer,List<TimeSymptoms>>();
		List<TimeSymptoms> symptomData = timeSymptoms.stream().filter(s->s.getSymptomID().equals(sysmptomID)).collect(Collectors.toList());
		for (TimeSymptoms timeSymptom : symptomData) {
		   Integer key = Objects.hash(timeSymptom.getIcd10Code(),timeSymptom.getSymptomID(),timeSymptom.getMultiplier());
		   DuplicateTimeSymptoms test = new DuplicateTimeSymptoms();
		   test.setDmId(timeSymptom.getDmId());
		   test.setLikelihood(timeSymptom.getLikelihood());
		   List<TimeSymptoms> duplicates =    mdcMap.get(key);
		   if(duplicates== null  ) {
			   duplicates = new ArrayList<TimeSymptoms>();
			   duplicates.add(timeSymptom);
		   } else {
				   duplicates.add(timeSymptom);
		   }
		   mdcMap.putIfAbsent(key, duplicates);
	   }
	   
		
		mdcMap.forEach((k,v)->{
			
		if(v.size() > 1){
				TimeSymptoms v1 = v.stream().filter(s->s.getTimeFrame().equals("Longer")).findAny().orElse(null);
				if(v1 != null ) {
 			 Double lLikilood = v1.getLikelihood();
			
			 Double tLikilood = 0.2;
			for (int j = 0; j <  v.size(); j++) {
			
			TimeSymptoms types = v.get(j);
			
			Double	 oLikilood  =  types.getLikelihood();
			
		
			if( lLikilood.compareTo(oLikilood) == -1  ||  lLikilood.compareTo(tLikilood) == -1  ) {
				
				finalChronic.add(types);
				
			} else {
			
				if(types.getTimeFrame().equals("Longer")) {
				
					finalChronic.add(types);
				}
				
			}
			
			
			
				}
			
			//finalChronic.add(v1);
	
				}
				
				
			}
		
		
		
		 });
	}
	
	createChroAcuteTimeJson(finalChronic);
	}
	
	
	


	
	
	

	private void createChroAcuteTimeJson(List<TimeSymptoms> finalChronic) {
		try {
			Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).disableHtmlEscaping().serializeNulls().setPrettyPrinting().create();
			Path path = Paths.get(jsonLocation+ File.separator +  dataFrameRelPath  + File.separator +  DF_MICAUtil.getAdvinowDateFormat()+  File.separator + dataFrameName + "chronicAcute.Json");
			Files.createDirectories(path.getParent());
			String json = gson.toJson(finalChronic);
			byte[] utf8JsonString = json.getBytes(StandardCharsets.UTF_8);
			Files.write(path,utf8JsonString, StandardOpenOption.CREATE_NEW);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void createDuplicateTimeJson(List<DuplicateTimeSymptoms> duplicates ) {
		try {
			Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).disableHtmlEscaping().serializeNulls().setPrettyPrinting().create();
			Path path = Paths.get(jsonLocation+ File.separator +  dataFrameRelPath  + File.separator +  DF_MICAUtil.getAdvinowDateFormat()+  File.separator + dataFrameName + "duplicate.Json");
			Files.createDirectories(path.getParent());
			String json = gson.toJson(duplicates);
			byte[] utf8JsonString = json.getBytes(StandardCharsets.UTF_8);
			Files.write(path,utf8JsonString, StandardOpenOption.CREATE_NEW);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	private List<TimeSymptoms> getTimeSymptoms() {
		return illnessReportRepository.getTimeSymptoms();

	}
	
	
	private List<TimeSymptoms> getCTimeSymptoms() {
		return illnessReportRepository.getChronicTimeSymptoms();

	}

	
	private List<TimeSymptoms> getATimeSymptoms() {
		return illnessReportRepository.getAcuteTimeSymptoms();

	}	
	
	
	@Override
	public void getAcuteTimeSymptoms() {

		List<TimeSymptoms> finalChronic = new ArrayList<TimeSymptoms>();
		List<TimeSymptoms>  timeSymptoms=	getATimeSymptoms();
		List<String> allSymtpoms = timeSymptoms.stream().map(TimeSymptoms::getSymptomID).distinct().collect(Collectors.toList());
	for (int i = 0; i < allSymtpoms.size(); i++) {	
		String sysmptomID = allSymtpoms.get(i);
		Map<Integer, List<TimeSymptoms>> mdcMap = new Hashtable<Integer,List<TimeSymptoms>>();
		List<TimeSymptoms> symptomData = timeSymptoms.stream().filter(s->s.getSymptomID().equals(sysmptomID)).collect(Collectors.toList());
		for (TimeSymptoms timeSymptom : symptomData) {
		   Integer key = Objects.hash(timeSymptom.getIcd10Code(),timeSymptom.getSymptomID(),timeSymptom.getMultiplier());
		   DuplicateTimeSymptoms test = new DuplicateTimeSymptoms();
		   test.setDmId(timeSymptom.getDmId());
		   test.setLikelihood(timeSymptom.getLikelihood());
		   List<TimeSymptoms> duplicates =    mdcMap.get(key);
		   if(duplicates== null  ) {
			   duplicates = new ArrayList<TimeSymptoms>();
			   duplicates.add(timeSymptom);
		   } else {
				   duplicates.add(timeSymptom);
		   }
		   mdcMap.putIfAbsent(key, duplicates);
	   }
	   
		
		mdcMap.forEach((k,v)->{
			
			if(v.size() ==1 ) {
				
				finalChronic.add(v.get(0));
				
			}
			
			
			
		if(v.size() > 1){
				TimeSymptoms v1 = v.stream().filter(s->s.getTimeFrame().equals("Longer")).findAny().orElse(null);
				if(v1 != null ) {
 			 Double lLikilood = v1.getLikelihood();
			
			for (int j = 0; j <  v.size(); j++) {
			
			TimeSymptoms types = v.get(j);
			
			Double	 oLikilood  =  types.getLikelihood();
				if(Double.compare(lLikilood, oLikilood)  > 0  ) {
				
				finalChronic.add(types);
				
				
				if(types.getTimeFrame().equals("Longer")) {
					
				//	finalChronic.add(types);
				}
				
				
				if(types.getTimeFrame().equals("Longer")) {
					
					finalChronic.add(types);
				}
				
				
			} else {
			
				if(types.getTimeFrame().equals("Longer")) {
				
					finalChronic.add(types);
				}

			if(types.getTimeFrame().equals("Longer")) {
			
				finalChronic.add(types);
			}
			
			
			
				}
			
			//finalChronic.add(v1);
	
				}
				
				
			}
		
		
		
		 });
	}
	
	createChroAcuteTimeJson(finalChronic);
	
		
	}


*/}
