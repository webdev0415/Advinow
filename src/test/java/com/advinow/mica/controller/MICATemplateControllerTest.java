package com.advinow.mica.controller;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.advinow.mica.AbstractTest;
import com.advinow.mica.model.SymptomTemplateModel;
import com.advinow.mica.model.SymptomTemplateModelV1;

public class MICATemplateControllerTest  extends AbstractTest {

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Test
	public void verifyEsTemplateSymptoms() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/es/template/v1/symptoms/SYMPT0002959").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		SymptomTemplateModelV1 symptoms = super.mapFromJson(content, SymptomTemplateModelV1.class);
	    assertEquals(symptoms.getName(), "Basic Abdomen Pain");
	    assertEquals(symptoms.getSymptomID(), "SYMPT0002959");
	}
	
	@Test
	public void verifyenTemplateSymptoms() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/en/template/v1/symptoms/SYMPT0002959").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		SymptomTemplateModelV1 symptoms = super.mapFromJson(content, SymptomTemplateModelV1.class);
	    assertEquals(symptoms.getName(), "Basic Abdomen Pain");
	    assertEquals(symptoms.getSymptomID(), "SYMPT0002959");
	}

	
	@Test
	public void verifyTemplateSymptoms() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/en/template/v1/symptoms/SYMPT0002959").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		SymptomTemplateModelV1 symptoms = super.mapFromJson(content, SymptomTemplateModelV1.class);
	    assertEquals(symptoms.getName(), "Basic Abdomen Pain");
	    assertEquals(symptoms.getSymptomID(), "SYMPT0002959");
	}
	
	@Test
	public void verifySymptomsDefinitions() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/template/symptoms/definitions").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		SymptomTemplateModel[] symptoms = super.mapFromJson(content, SymptomTemplateModel[].class);
        assertNotNull(symptoms);
        assertTrue(symptoms.length > 0);
	}
	
}
