package com.advinow.mica.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.advinow.mica.AbstractTest;
import com.advinow.mica.domain.DataKeys;
import com.advinow.mica.domain.DataStore;
import com.advinow.mica.model.BodyParts;

public class MICAGenericRestControllerTest extends AbstractTest {

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Test
	public void verifyDataStore() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/generic/datakeys/Gender").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		DataKeys datakeys = super.mapFromJson(content, DataKeys.class);
		
		assertEquals(datakeys.getCode(), "DK003");
		assertEquals(datakeys.getName(), "Gender");
		assertEquals(datakeys.getTitle(), "Gender");
	    Set<DataStore> dataStoreList = datakeys.getDataStoreList();
		assertNotNull(dataStoreList);		
		
		DataStore dataStore = dataStoreList.stream().filter(s->s.getCode().equalsIgnoreCase("DS008")).findAny().orElse(null);
		assertNotNull(dataStore);
		assertEquals(dataStore.getName(), "Male");
		assertEquals(dataStore.getDisplayListValue(), true);	
		

		dataStore = dataStoreList.stream().filter(s->s.getCode().equalsIgnoreCase("DS009")).findAny().orElse(null);
		assertNotNull(dataStore);
		assertEquals(dataStore.getName(), "Female");
		assertEquals(dataStore.getDisplayListValue(), true);	
		

		dataStore = dataStoreList.stream().filter(s->s.getCode().equalsIgnoreCase("DS010")).findAny().orElse(null);
		assertNotNull(dataStore);
		assertEquals(dataStore.getName(), "Transgender");
		assertEquals(dataStore.getDisplayListValue(), true);	
}
	
	
	@Test
	public void verifyBodyParts() throws Exception {
		
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/generic/bodyparts").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		BodyParts bodyParts = super.mapFromJson(content, BodyParts.class);
		assertNotNull(bodyParts);
		
		assertEquals(bodyParts.getName(), "Whole Body");
		assertEquals(bodyParts.getBodypartID(),"BODYPART01");	

		assertEquals(bodyParts.getPosition().get(0),"Whole Body" );	
		assertEquals(bodyParts.getSubParts().size(),9);
	}

	
	

	@Test
	public void verifyBodyPartsWithParentCategories() throws Exception {
		
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/generic/bodyparts/all").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		BodyParts bodyParts = super.mapFromJson(content, BodyParts.class);
		assertNotNull(bodyParts);
		
		assertEquals(bodyParts.getName(), "Whole Body");
	
		assertEquals(bodyParts.getPosition().get(0),"Whole Body" );	
		assertEquals(bodyParts.getSubParts().size(),9);
		assertEquals(bodyParts.getParentID().intValue(),1);
		List<String> rootBodyParts = new ArrayList<String>() ;
		rootBodyParts.add("SW_BODYPART01");
		rootBodyParts.add("PAIN_BODYPART01");
		rootBodyParts.add("BODYPART01");
		rootBodyParts.sort(Comparator.naturalOrder());		
	    List<String> dbBodyParts = bodyParts.getBodyPartsCodes();
	    dbBodyParts.sort(Comparator.naturalOrder());
		assertEquals(dbBodyParts,rootBodyParts );
	}

	@Test
	public void verifyPainSwellingBodyParts() throws Exception {
		
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/generic/painswelling/bodyparts").accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		 BodyParts[] bodyParts= super.mapFromJson(content, BodyParts[].class);
		assertNotNull(bodyParts);
		assertEquals(bodyParts.length,2 );
		
		assertEquals(bodyParts[0].getName(), "Whole Body");
		assertEquals(bodyParts[0].getBodypartID(), "PAIN_BODYPART01");
		assertEquals(bodyParts[0].getGroupName(), "pain");	
		assertEquals(bodyParts[0].getSubParts().size(), 11);	
	
		assertEquals(bodyParts[1].getName(), "Whole Body");
		assertEquals(bodyParts[1].getBodypartID(), "SW_BODYPART01");
		assertEquals(bodyParts[1].getGroupName(), "swelling");	
		assertEquals(bodyParts[1].getSubParts().size(), 10);	
	
	
	}
	
}