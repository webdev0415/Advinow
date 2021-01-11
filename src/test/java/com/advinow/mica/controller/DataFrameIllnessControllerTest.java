package com.advinow.mica.controller;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.advinow.mica.AbstractTest;
import com.advinow.mica.model.dataframe.DataFrameModel;


public class DataFrameIllnessControllerTest  extends AbstractTest {

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Test
	public void verifyActiveIllnesses() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/dataframe/illnesses/active").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		DataFrameModel dataframeModel = super.mapFromJson(content, DataFrameModel.class);
		Map<String, Integer> activeIllnesses = dataframeModel.getIcd10Codes();
		assertNotNull(activeIllnesses);		
		assertFalse(activeIllnesses.isEmpty());
		
	}
	
	
	@Test
	public void verifyActiveSymptoms() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/dataframe/symptoms/active").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		DataFrameModel dataframeModel = super.mapFromJson(content, DataFrameModel.class);
		Set<String> symptoms = dataframeModel.getSymptoms();
		assertNotNull(symptoms);		
		assertFalse(symptoms.isEmpty());
		
	}
	


	
	@Test
	public void verifyAcuteSymptoms() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/dataframe/geAcuteSymptoms").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println(content);
	
	}
	
	
	@Test
	public void generateDataFrame() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/dataframe/generate").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println(content);
	
	}
}
