package com.advinow.mica.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.stream.Stream;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.advinow.mica.AbstractTest;
import com.advinow.mica.domain.TreatmentSourceInfo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TreatmentSourceControllerTest  extends AbstractTest {
	
	private  String SOURCE_NAME = "Test_Teatment_Source";
	

	

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Test
	public void verifyCreateSource() throws Exception {	
		TreatmentSourceInfo sourceInfo = new TreatmentSourceInfo();
		sourceInfo.setSource(SOURCE_NAME);
	
	MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/treatmentsources/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(super.mapToJson(sourceInfo)))
	        .andReturn();

	String content = mvcResult.getResponse().getContentAsString();
    int status = mvcResult.getResponse().getStatus();
     assertEquals(201, status);
    TreatmentSourceInfo created = super.mapFromJson(content, TreatmentSourceInfo.class);
    assertEquals(SOURCE_NAME, created.getSource());
 	}

	
	@Test
	public void verifySearchSources() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/treatmentsources/search?source=" + SOURCE_NAME).accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();		
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		TreatmentSourceInfo[] sources = super.mapFromJson(content, TreatmentSourceInfo[].class);
		assertNotNull(sources);	
		assertFalse(sources.length<0);		
		TreatmentSourceInfo source = Stream.of(sources).filter(s->s.getSource().equalsIgnoreCase(SOURCE_NAME)).findAny().orElse(null);		
		assertNotNull(source);			
	}
	
	
	@Test
	public void verifyTreatmentSources() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/treatmentsources/all").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();		
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		TreatmentSourceInfo[] sources = super.mapFromJson(content, TreatmentSourceInfo[].class);
		assertNotNull(sources);	
		assertFalse(sources.length<0);		
	}
	
		
	
	@Test
	public void verifyUpdateSources() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/treatmentsources/search?source=" + SOURCE_NAME).accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();		
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		TreatmentSourceInfo[] sources = super.mapFromJson(content, TreatmentSourceInfo[].class);
		assertNotNull(sources);	
		assertFalse(sources.length<0);		
		TreatmentSourceInfo source = Stream.of(sources).filter(s->s.getSource().equalsIgnoreCase(SOURCE_NAME)).findAny().orElse(null);		
		assertNotNull(source);	
		MvcResult mvcResult1 = mockMvc
					.perform(MockMvcRequestBuilders.delete("/treatmentsources/" + source.getSourceID()).accept(MediaType.APPLICATION_JSON)).andReturn();
		int	 status1 = mvcResult.getResponse().getStatus();		
			assertEquals(200, status1);
			String content1 = mvcResult1.getResponse().getContentAsString();			
			  assertEquals("Source deleted Successfully", content1);
			
		
	}
}
