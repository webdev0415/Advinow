/**
 * 
 */
package com.advinow.mica.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.advinow.mica.AbstractTest;
import com.advinow.mica.domain.SymptomSourceInfo;
import com.advinow.mica.domain.TreatmentSourceInfo;
import com.advinow.mica.model.IllnessStatusModel;
import com.advinow.mica.model.TreatmentDocModel;
import com.advinow.mica.model.TreatmentMainDocModel;
import com.advinow.mica.model.TreatmentMainModel;
import com.advinow.mica.model.TreatmentModel;
import com.advinow.mica.model.TreatmentTypeRefDescModel;
import com.advinow.mica.model.TreatmentTypeRefGroups;
import com.advinow.mica.model.TreatmentTypeRefModel;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Govinda Reddy
 *
 */
public class MICATreatmentControllerTest   extends AbstractTest {

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Test
	public void testTreatmentTypes() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/treatment/types").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		TreatmentTypeRefGroups dataframeModel = super.mapFromJson(content, TreatmentTypeRefGroups.class);
		 List<TreatmentTypeRefModel> types = dataframeModel.getTreatmentTypes();
		assertNotNull(types);		
		assertFalse(types.isEmpty());
		
	}
	
	
	@Test
	public void testTreamentType() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/treatment/types/diet").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		TreatmentTypeRefModel dataframeModel = super.mapFromJson(content, TreatmentTypeRefModel.class);
	    List<TreatmentTypeRefDescModel> types = dataframeModel.getTreatmentTypeDesc();
		assertNotNull(types);		
		assertEquals(dataframeModel.getName().toUpperCase(),"diet".toUpperCase());
		assertFalse(types.isEmpty());
		
	}
	
	@Test
	public void verifyTreatmentIllnessByIllnesCode() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/treatment/illness/types/J00").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		TreatmentMainDocModel dataframeModel = super.mapFromJson(content, TreatmentMainDocModel.class);
	  		assertNotNull(dataframeModel);		
		assertEquals(dataframeModel.getIcd10Code(),"J00".toUpperCase());
		List<TreatmentDocModel> treatments = dataframeModel.getTreatments();
		assertNotNull(treatments);	
		assertFalse(treatments.isEmpty());
		
	}
	
	@Test
	public void verifyIllnessTreatmentJOOLong() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/treatment/illness/J00").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		TreatmentMainModel dataframeModel = super.mapFromJson(content, TreatmentMainModel.class);
	  		assertNotNull(dataframeModel);		
		assertEquals(dataframeModel.getIcd10Code(),"J00".toUpperCase());
		List<TreatmentModel> treatments = dataframeModel.getTreatments();
		assertNotNull(treatments);	
		assertFalse(treatments.isEmpty());
		
	}
	
	
	@Test
	public void verifyIllnessTreatmentJOOShort() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/treatment/illness/short/J00").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		TreatmentMainModel dataframeModel = super.mapFromJson(content, TreatmentMainModel.class);
	  		assertNotNull(dataframeModel);		
		assertEquals(dataframeModel.getIcd10Code(),"J00".toUpperCase());
		List<TreatmentModel> treatments = dataframeModel.getTreatments();
		assertNotNull(treatments);	
		assertFalse(treatments.isEmpty());
		
	}
	
	
	@Test
	public void verifyTreatmentIllnessBySymptomCode() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/treatment/symptom/types/SYMPT0000011").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		TreatmentMainDocModel dataframeModel = super.mapFromJson(content, TreatmentMainDocModel.class);
	  		assertNotNull(dataframeModel);		
		assertEquals(dataframeModel.getSymptomID().toUpperCase(),"SYMPT0000011".toUpperCase());
		List<TreatmentDocModel> treatments = dataframeModel.getTreatments();
		assertNotNull(treatments);	
		assertFalse(treatments.isEmpty());
		
	}
	
	
	@Test
	public void verifySymptomTreatment011Long() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/treatment/symptom/SYMPT0000011").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		TreatmentMainModel dataframeModel = super.mapFromJson(content, TreatmentMainModel.class);
	  		assertNotNull(dataframeModel);		
		assertEquals(dataframeModel.getSymptomID(),"SYMPT0000011".toUpperCase());
		List<TreatmentModel> treatments = dataframeModel.getTreatments();
		assertNotNull(treatments);	
		assertFalse(treatments.isEmpty());
		
	}
	
	
	@Test
	public void verifySymptomTreatment011Short() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/treatment/symptom/short/SYMPT0000011").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		TreatmentMainModel dataframeModel = super.mapFromJson(content, TreatmentMainModel.class);
	  	assertNotNull(dataframeModel);		
	  	assertEquals(dataframeModel.getSymptomID(),"SYMPT0000011".toUpperCase());
		List<TreatmentModel> treatments = dataframeModel.getTreatments();
		assertNotNull(treatments);	
		assertFalse(treatments.isEmpty());
		
	}
	
	
	@Test
	public void verifyTreatmentSources() throws Exception {
		
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/treatment//sources?icd10code=H92.02").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(200, status);
		TreatmentSourceInfo[] sources = super.mapFromJson(content, TreatmentSourceInfo[].class);
	assertNotNull(sources);	
	assertFalse(sources.length<0);
		
	}
	
	
	@Test
	public void verifyTreatments() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/treatment/v1/illness/short/H10.9").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		TreatmentMainModel dataframeModel = super.mapFromJson(content, TreatmentMainModel.class);
	  		assertNotNull(dataframeModel);		
	//	assertEquals(dataframeModel.getIcd10Code(),"J00".toUpperCase());
		List<TreatmentModel> treatments = dataframeModel.getTreatments();
		assertNotNull(treatments);	
		assertFalse(treatments.isEmpty());
		
		
	/*	try {
			
			 ObjectMapper mapper = new ObjectMapper();
				
			 String json = mapper.writeValueAsString(mapper);
	         System.out.println("JSON = " + json);
			
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
	}
	

}
