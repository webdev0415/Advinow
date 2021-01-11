package com.advinow.mica.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.advinow.mica.AbstractTest;
import com.advinow.mica.domain.SymptomSourceInfo;
import com.advinow.mica.model.IllnessStatusModel;
import com.advinow.mica.model.IllnessUserData;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MICAIllnessControllerTest  extends AbstractTest {
	

	protected Logger logger = LoggerFactory.getLogger(getClass());



	@Override
	@Before
	public void setUp() {
		super.setUp();
	}
	
	
	
	
	@Test
	public void verifyIllnessSources() throws Exception {
		try{
			 ObjectMapper objectMapper= new ObjectMapper();
				
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/mica/illness/sources?icd10code=D50.9&state=APPROVED&version=2").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();		
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		SymptomSourceInfo[] sources = super.mapFromJson(content, SymptomSourceInfo[].class);
		String jsonString = objectMapper.writeValueAsString(sources);
		
		logger.info("Sources" + jsonString);
		assertNotNull(sources);	
		assertFalse(sources.length<0);
		}catch (Exception e) {
		e.printStackTrace();
		}
	}
	
	
	@Test
	public void verifyUserIllnessSources() throws Exception {
		
		IllnessStatusModel illness = new IllnessStatusModel();
		illness.setUserID(138);
		 List<String> state= new ArrayList<String>();
		 state.add("APPROVED");
		illness.setState(state);
	
	MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/mica/illness/user/sources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(super.mapToJson(illness)))
	        .andReturn();

	String content = mvcResult.getResponse().getContentAsString();
    int status = mvcResult.getResponse().getStatus();
	assertEquals(200, status);
	SymptomSourceInfo[] sources = super.mapFromJson(content, SymptomSourceInfo[].class);
	logger.info("Sources" + sources.length);
	assertNotNull(sources);	
	assertFalse(sources.length<0);
		
	}
	
	
	@Test
	public void verifyAddIllness() throws Exception {
		 ObjectMapper objectMapper= new ObjectMapper();
		
		 IllnessUserData illness = objectMapper.readValue(new File(getClass().getClassLoader().getResource("illness_add.json").getFile()),IllnessUserData.class);
		
	
	MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/mica/api/illness")
            .contentType(MediaType.APPLICATION_JSON)
            .content(super.mapToJson(illness)))
	        .andReturn();

	String content = mvcResult.getResponse().getContentAsString();
    int status = mvcResult.getResponse().getStatus();
	assertEquals(201, status);
	IllnessStatusModel sources = super.mapFromJson(content, IllnessStatusModel.class);
	
	
	
	
	
	assertNotNull(sources);	
		
	}
	
	
		
	
	
}
