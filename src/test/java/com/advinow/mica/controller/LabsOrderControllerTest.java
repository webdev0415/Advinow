package com.advinow.mica.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

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
import com.advinow.mica.domain.LabOrdersRef;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LabsOrderControllerTest  extends AbstractTest {
	
	protected Logger logger = LoggerFactory.getLogger(LabsOrderControllerTest.class);
	/*
	private 	List<String> ordersNames = new ArrayList<String>();
	*/
//	private String symptomID ="SYMPT0004293";
	
	@Override
	@Before
	public void setUp() {
		super.setUp();
		/*ordersNames.add("Test_order_1");
		ordersNames.add("Test_order_2");
		ordersNames.add("Test_order_3");*/
	}
	
/*	@Test
	public void verify_1_CreateLabOrder() throws Exception {	
		SymptomsLabOrders symptomsLabOrders = new SymptomsLabOrders();
		
		symptomsLabOrders.setSymptomID(symptomID);
		symptomsLabOrders.setLabsOrdered(ordersNames);
	
	MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/laborder/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(super.mapToJson(symptomsLabOrders)))
	        .andReturn();

	String content = mvcResult.getResponse().getContentAsString();
    int status = mvcResult.getResponse().getStatus();
    assertEquals(201, status);
    SymptomsLabOrders created = super.mapFromJson(content, SymptomsLabOrders.class);
    assertEquals(symptomID, created.getSymptomID());
    assertEquals(ordersNames,created.getLabsOrdered());
 	}
*/
	
	
	/*
	
	@Test
	public void verify_2_SearchLabs() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/laborder/SYMPT0004293").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();		
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		logger.info("Content "+ content);
		SymptomsLabOrders orders = super.mapFromJson(content, SymptomsLabOrders.class);
		assertNotNull(orders);	
	}
	*/
	
	@Test
	public void verify_3_All_labs() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/laborders").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();		
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println(content);
		LabOrdersRef[] allOrders = super.mapFromJson(content, LabOrdersRef[].class);
		assertNotNull(allOrders);	
		assertFalse(allOrders.length<0);		
	}
	
	/*	
	
	@Test
	public void verifyUpdateSources() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/symptomsources/search?source=" + SOURCE_NAME).accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();		
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		SymptomSourceInfo[] sources = super.mapFromJson(content, SymptomSourceInfo[].class);
		assertNotNull(sources);	
		assertFalse(sources.length<0);		
		SymptomSourceInfo source = Stream.of(sources).filter(s->s.getSource().equalsIgnoreCase(SOURCE_NAME)).findAny().orElse(null);		
		assertNotNull(source);	
		MvcResult mvcResult1 = mockMvc
					.perform(MockMvcRequestBuilders.delete("/symptomsources/" + source.getSourceID()).accept(MediaType.APPLICATION_JSON)).andReturn();
		int	 status1 = mvcResult.getResponse().getStatus();		
			assertEquals(200, status1);
			String content1 = mvcResult1.getResponse().getContentAsString();			
			  assertEquals("Source deleted Successfully", content1);
			
		
	}
*/

}
