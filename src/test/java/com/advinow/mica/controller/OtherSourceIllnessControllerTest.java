package com.advinow.mica.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.advinow.mica.AbstractTest;
import com.advinow.mica.domain.queryresult.IllnessDataQueryResultEnitity;
import com.advinow.mica.model.ICD10CodeModel;
import com.advinow.mica.model.IllnessModel;
import com.advinow.mica.model.IllnessUserData;
import com.advinow.mica.model.PaginationModel;
import com.advinow.mica.model.SymptomSource;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OtherSourceIllnessControllerTest   extends AbstractTest {

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Test
	public void testIllnessesByPagination() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/MICA/api/illness?status=APPROVED&page=1&size=10").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		PaginationModel pageModel = super.mapFromJson(content, PaginationModel.class);
		List<ICD10CodeModel> illnessContent = pageModel.getContent();
		
		assertNotNull(illnessContent);		
		assertFalse(illnessContent.isEmpty());
		
	}
	
	/*
	@Test
	public void testIllnessDataByUser() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/MICA/api/illness/user?userID=1").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		IllnessUserData illnessData = super.mapFromJson(content, IllnessUserData.class);
		assertNotNull(illnessData);		
		List<IllnessModel> userData = illnessData.getUserData();
		assertNotNull(userData);	
		assertFalse(userData.isEmpty());
		assertEquals(userData.get(0).getUserID().intValue(),1);
		
	}*/

	@Test
	public void verifyIllessBySourceAndIcd10Code() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/MICA/api/illness/J00").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		IllnessUserData illnessData = super.mapFromJson(content, IllnessUserData.class);
		assertNotNull(illnessData);		
		List<IllnessModel> userData = illnessData.getUserData();
		assertNotNull(userData);	
		assertFalse(userData.isEmpty());
		assertEquals(userData.get(0).getIdIcd10Code(),"J00");
		
	}
	
	
	@Test
	public void verifyIllessBySourceAndIcd10CodeAndState() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/MICA/api/illness/J00?state=APPROVED").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		IllnessUserData illnessData = super.mapFromJson(content, IllnessUserData.class);
		assertNotNull(illnessData);		
		List<IllnessModel> userData = illnessData.getUserData();
		assertNotNull(userData);	
		assertFalse(userData.isEmpty());
		assertEquals(userData.get(0).getIdIcd10Code(),"J00");
		
	}
	
	

	@Test
	public void verifyIllessSources() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/MICA/api/sourceinfo/J00?state=APPROVED").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		SymptomSource[] sources = super.mapFromJson(content, SymptomSource[].class);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(sources);
	    System.out.println(jsonString);
		assertNotNull(sources);	
		assertFalse(sources.length<0);
			
	}
	
	@Test
	public void verifyApprovedIllnessBySource() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/MICA/api/illnesses/approved").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		IllnessDataQueryResultEnitity[] illnessesData = super.mapFromJson(content, IllnessDataQueryResultEnitity[].class);
		assertNotNull(illnessesData);
		assertFalse(illnessesData.length<0);
		
	}
	
}
