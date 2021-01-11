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
import com.advinow.mica.domain.PolicyRef;
import com.advinow.mica.domain.queryresult.SymptomGroupResult;
import com.advinow.mica.model.LogicalSymptomGroupsModel;
import com.advinow.mica.model.PolicyModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TreatmentPolicyControllerTest  extends AbstractTest {
	

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private  String POLICY_NAME = "Policy-GovindTest";

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Test
	public void verifyCreatePolicy() throws Exception {	
		PolicyRef policyDTO = new PolicyRef();
		policyDTO.setName(POLICY_NAME);
	
	MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/policies/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(super.mapToJson(policyDTO)))
	        .andReturn();

	String content = mvcResult.getResponse().getContentAsString();
    int status = mvcResult.getResponse().getStatus();
     assertEquals(201, status);
     PolicyRef created = super.mapFromJson(content, PolicyRef.class);
     assertEquals(POLICY_NAME, created.getName());
 	}

	
	@Test
	public void verifyPolicyMappings() throws Exception {	
		
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/policies/all").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();		
		assertEquals(200, status);
		
		
		
		String content = mvcResult.getResponse().getContentAsString();
		PolicyRef[] policiesRef = super.mapFromJson(content, PolicyRef[].class);				
		PolicyRef plicyRef = Stream.of(policiesRef).filter(s->s.getName().equalsIgnoreCase(POLICY_NAME)).findAny().orElse(null);		
	
		
		PolicyModel policyDTO = new PolicyModel();
		policyDTO.setPolicyID(plicyRef.getPolicyID());
		
		 List<String> symptoms = new ArrayList<String>();
		symptoms.add("SYMPT0000006");
		symptoms.add("SYMPT0000005");
		symptoms.add("SYMPT0000004");
		symptoms.add("SYMPT0000003");
		symptoms.add("SYMPT0000002");
	//	symptoms.add("SYMPT0000001");
		
		policyDTO.setSymptoms(symptoms);
	
	MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.put("/template/policies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(super.mapToJson(policyDTO)))
	        .andReturn();

	
	String content1 = mvcResult1.getResponse().getContentAsString();
    int status1 = mvcResult1.getResponse().getStatus();
    assertEquals(201, status1);
    
    System.out.println(content1);
  //  LogicalSymptomGroupsModel created1 = super.mapFromJson(content1, LogicalSymptomGroupsModel.class);
    assertEquals("Symptoms policies added successfully.", content1);
    
    
    ObjectMapper mapper = new ObjectMapper();
    try {
        String json = mapper.writeValueAsString(policyDTO);
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
	public void verifyPoliciesUnmap() throws Exception {
		
		
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/policies/all").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();		
		assertEquals(200, status);
		

		
		String content = mvcResult.getResponse().getContentAsString();
		PolicyRef[] policiesRef = super.mapFromJson(content, PolicyRef[].class);				
		PolicyRef plicyRef = Stream.of(policiesRef).filter(s->s.getName().equalsIgnoreCase(POLICY_NAME)).findAny().orElse(null);			
	
		
		assertNotNull(plicyRef);	
		
		MvcResult mvcResult1 = mockMvc
					.perform(MockMvcRequestBuilders.get("/template/policies/" + plicyRef.getPolicyID()).accept(MediaType.APPLICATION_JSON)).andReturn();
		int	 status1 = mvcResult.getResponse().getStatus();		
		
		
		assertEquals(200, status1);
			String content1 = mvcResult1.getResponse().getContentAsString();
			PolicyModel policy = super.mapFromJson(content1, PolicyModel.class);
			assertNotNull(policy);	
			 assertEquals(policy.getPolicyID(), plicyRef.getPolicyID());
	
		
	}
	
	
	
	@Test
	public void verifyGetAllPolicies() throws Exception {
		try{
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/policies/all").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();		
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		PolicyRef[] policiesRef = super.mapFromJson(content, PolicyRef[].class);	
		assertNotNull(policiesRef);	
		assertFalse(policiesRef.length<0);
		}catch (Exception e) {
		e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	@Test
	public void verifyUpdateDeletePolicies() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/policies/all").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();		
		assertEquals(200, status);
		
		String content = mvcResult.getResponse().getContentAsString();
		PolicyRef[] policiesRef = super.mapFromJson(content, PolicyRef[].class);				
		PolicyRef plicyRef = Stream.of(policiesRef).filter(s->s.getName().equalsIgnoreCase(POLICY_NAME)).findAny().orElse(null);		
		
		assertNotNull(plicyRef);			
		MvcResult mvcResult1 = mockMvc
					.perform(MockMvcRequestBuilders.delete("/policies/" + plicyRef.getPolicyID()).accept(MediaType.APPLICATION_JSON)).andReturn();
		int	 status1 = mvcResult.getResponse().getStatus();		
			assertEquals(200, status1);
			String content1 = mvcResult1.getResponse().getContentAsString();			
			  assertEquals("Policy deleted Successfully", content1);
			
		
	}
	
	
}
