package com.advinow.mica.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
import com.advinow.mica.domain.queryresult.SymptomGroupResult;
import com.advinow.mica.model.LogicalSymptomGroupsModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SymptomGroupsControllerTest  extends AbstractTest {
	

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private  String GROUP_NAME = "ROS-GovindTest";

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Test
	public void verifyCreateLogicalGroup() throws Exception {	
		LogicalSymptomGroupsModel groupDTO = new LogicalSymptomGroupsModel();
		groupDTO.setName(GROUP_NAME);
	
	MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/groups/saveOrUpdate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(super.mapToJson(groupDTO)))
	        .andReturn();

	String content = mvcResult.getResponse().getContentAsString();
    int status = mvcResult.getResponse().getStatus();
     assertEquals(201, status);
     LogicalSymptomGroupsModel created = super.mapFromJson(content, LogicalSymptomGroupsModel.class);
     assertEquals(GROUP_NAME, created.getName());
 	}

	
	@Test
	public void verifyCreateSmptomsToGroup() throws Exception {	
		
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/groups/all").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();		
		assertEquals(200, status);
		
		
		
		String content = mvcResult.getResponse().getContentAsString();
		LogicalSymptomGroupsModel[] groups = super.mapFromJson(content, LogicalSymptomGroupsModel[].class);				
		LogicalSymptomGroupsModel group = Stream.of(groups).filter(s->s.getName().equalsIgnoreCase(GROUP_NAME)).findAny().orElse(null);		
	

		
		
		LogicalSymptomGroupsModel groupDTO = new LogicalSymptomGroupsModel();
		groupDTO.setGroupID(group.getGroupID());
		
		 List<SymptomGroupResult> symptoms = new ArrayList<SymptomGroupResult>();
			
		
		SymptomGroupResult symptom6 = new SymptomGroupResult();
		symptom6.setSymptomID("SYMPT0000006");
		symptom6.setGroupFlag("C");

		symptoms.add(symptom6);
		
		SymptomGroupResult symptom5 = new SymptomGroupResult();
		symptom5.setSymptomID("SYMPT0000005");
		symptom5.setGroupFlag("C");


		symptoms.add(symptom5);

		
		SymptomGroupResult symptom4 = new SymptomGroupResult();
		symptom4.setSymptomID("SYMPT0000004");
		symptom4.setGroupFlag("C");


		symptoms.add(symptom4);

		
		SymptomGroupResult symptom3 = new SymptomGroupResult();
		symptom3.setSymptomID("SYMPT0000003");
		symptom3.setGroupFlag("C");


		symptoms.add(symptom3);

		
		SymptomGroupResult symptom2 = new SymptomGroupResult();
		symptom2.setSymptomID("SYMPT0000002");
		symptom2.setGroupFlag("C");


		symptoms.add(symptom2);

		
		SymptomGroupResult symptom1 = new SymptomGroupResult();
		symptom1.setSymptomID("SYMPT0000001");
		symptom1.setGroupFlag("C");
		

		symptoms.add(symptom1);

		
		
		groupDTO.setSymptoms(symptoms);
	
	MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.put("/template/groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(super.mapToJson(groupDTO)))
	        .andReturn();

	
	String content1 = mvcResult1.getResponse().getContentAsString();
    int status1 = mvcResult1.getResponse().getStatus();
    assertEquals(201, status1);
    
    System.out.println(content1);
  //  LogicalSymptomGroupsModel created1 = super.mapFromJson(content1, LogicalSymptomGroupsModel.class);
    assertEquals("Symptoms group mapping done successfully ", content1);
    
    
    ObjectMapper mapper = new ObjectMapper();
    try {
        String json = mapper.writeValueAsString(groupDTO);
        System.out.println("JSON = " + json);
    } catch (JsonProcessingException e) {
        e.printStackTrace();
    }
    
    
    
   /* assertEquals(created1.getGroupID(), group.getGroupID());
	 
	 symptoms.sort(Comparator.naturalOrder());
	 List<String>	 dbSymptoms = created1.getSymptoms();
	 System.out.println(dbSymptoms);
	 dbSymptoms .sort(Comparator.naturalOrder());
	// assertEquals(dbSymptoms,symptoms);
*/    
 	}
	
	
	

	@Test
	public void verifyDeSymptomsMapping() throws Exception {
		
/*
		 List<SymptomGroupResult> symptoms = new ArrayList<SymptomGroupResult>();
			
		
		SymptomGroupResult symptom6 = new SymptomGroupResult();
		symptom6.setSymptomID("SYMPT0000006");
		symptom6.setGroupFlag("C");

		SymptomGroupResult symptom5 = new SymptomGroupResult();
		symptom5.setSymptomID("SYMPT0000005");
		symptom5.setGroupFlag("C");
		
		SymptomGroupResult symptom2 = new SymptomGroupResult();
		symptom2.setSymptomID("SYMPT0000002");
		symptom2.setGroupFlag("C");
		
		SymptomGroupResult symptom1 = new SymptomGroupResult();
		symptom1.setSymptomID("SYMPT0000001");
		symptom1.setGroupFlag("C");*/
	
		
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/groups/all").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();		
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		LogicalSymptomGroupsModel[] groups = super.mapFromJson(content, LogicalSymptomGroupsModel[].class);				
		LogicalSymptomGroupsModel group = Stream.of(groups).filter(s->s.getName().equalsIgnoreCase(GROUP_NAME)).findAny().orElse(null);		
	
		
		assertNotNull(group);	
		
		MvcResult mvcResult1 = mockMvc
					.perform(MockMvcRequestBuilders.get("/template/groups/" + group.getGroupID()).accept(MediaType.APPLICATION_JSON)).andReturn();
		int	 status1 = mvcResult.getResponse().getStatus();		
		
		
		assertEquals(200, status1);
			String content1 = mvcResult1.getResponse().getContentAsString();
			LogicalSymptomGroupsModel symptomGroups = super.mapFromJson(content1, LogicalSymptomGroupsModel.class);
			assertNotNull(symptomGroups);	
			 assertEquals(symptomGroups.getGroupID(), group.getGroupID());
		//	 symptoms.sort(Comparator.naturalOrder());
			// List<String>	 dbSymptoms = symptomGroups.getSymptoms();
		// dbSymptoms .sort(Comparator.naturalOrder());
		//	 assertEquals(dbSymptoms,symptoms);
		
	}
	
	
	
	@Test
	public void verifyGetAllGroups() throws Exception {
		try{
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/groups/all").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();		
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		LogicalSymptomGroupsModel[] groups = super.mapFromJson(content, LogicalSymptomGroupsModel[].class);
		assertNotNull(groups);	
		assertFalse(groups.length<0);
		}catch (Exception e) {
		e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	@Test
	public void verifyUpdateDeleteGroups() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/groups/all").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();		
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		LogicalSymptomGroupsModel[] groups = super.mapFromJson(content, LogicalSymptomGroupsModel[].class);		
		LogicalSymptomGroupsModel group = Stream.of(groups).filter(s->s.getName().equalsIgnoreCase(GROUP_NAME)).findAny().orElse(null);		
		assertNotNull(group);			
		MvcResult mvcResult1 = mockMvc
					.perform(MockMvcRequestBuilders.delete("/groups/" + group.getGroupID()).accept(MediaType.APPLICATION_JSON)).andReturn();
		int	 status1 = mvcResult.getResponse().getStatus();		
			assertEquals(200, status1);
			String content1 = mvcResult1.getResponse().getContentAsString();			
			  assertEquals("Group deleted Successfully", content1);
			
		
	}
	
	
}
