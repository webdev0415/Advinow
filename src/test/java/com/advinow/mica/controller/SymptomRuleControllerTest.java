package com.advinow.mica.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.advinow.mica.AbstractTest;
import com.advinow.mica.domain.queryresult.SymptomRuleModel;
import com.advinow.mica.model.MICAResponse;
import com.fasterxml.jackson.databind.ObjectMapper;



/**
 * 
 * https://advinow.atlassian.net/browse/MICA-2589
 * 
 * @author Developer
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SymptomRuleControllerTest  extends AbstractTest {
	
	private  Integer ruleID = 2;
	
	 ObjectMapper objectMapper;
	
	@Override
	@Before
	public void setUp() {
		super.setUp();
		objectMapper = new ObjectMapper();
	}
	
	@Test
	public void createRule() throws Exception {	
		SymptomRuleModel rules = objectMapper.readValue(new File(getClass().getClassLoader().getResource("rules_add.json").getFile()),SymptomRuleModel.class);
		
/*		SymptomRuleModel rules = new SymptomRuleModel();
		rules.setDirection("1 Way");
		rules.setRuleID(roleID);
		rules.setRelation_s_to_t("Positive");
		rules.getS_list_items().add("DS2249");
		rules.setS_symptom_id("SYMPT0004242");
		rules.setS_rule("Any");
		rules.setS_trigger(SimpleTrigger.NO);
		rules.setT_symptom_id("SYMPT0000228");
		rules.getT_list_items().add("DS204");
		*/
	System.out.println("Rules " +rules );
		
	MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/rules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(super.mapToJson(rules)))
	        .andReturn();

	String content = mvcResult.getResponse().getContentAsString();
    int status = mvcResult.getResponse().getStatus();
    assertEquals(201, status);
    MICAResponse created = super.mapFromJson(content, MICAResponse.class);
    assertEquals("Rule created Successfully", created.getStatus());
 	}

	

	
	@Test
	public void verifyAllSources() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/rules").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();		
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		SymptomRuleModel[] sources = super.mapFromJson(content, SymptomRuleModel[].class);
		
		for (SymptomRuleModel symptomRuleModel : sources) {
			System.out.println(symptomRuleModel);
		}
		
		assertNotNull(sources);	
		
	}
	
		
	
	@Test
	public void verifyUpdateSources() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/rules/" + ruleID).accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();		
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		  SymptomRuleModel created = super.mapFromJson(content, SymptomRuleModel.class);
		assertNotNull(created);	
		
	//	SymptomSourceInfo source = Stream.of(sources).filter(s->s.getRuleID().equalsIgnoreCase(roleID)).findAny().orElse(null);		
		assertNotNull(created);	
		MvcResult mvcResult1 = mockMvc
					.perform(MockMvcRequestBuilders.delete("/rules/" + created.getRuleID()).accept(MediaType.APPLICATION_JSON)).andReturn();
		int	 status1 = mvcResult.getResponse().getStatus();		
			assertEquals(200, status1);
			String content1 = mvcResult1.getResponse().getContentAsString();			
			  assertEquals("Rule deleted Successfully", content1);
			
		
	}


}
