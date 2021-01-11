package com.advinow.mica;

import java.io.IOException;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.advinow.mica.MICAApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MICAApplication.class)
@WebAppConfiguration
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
public abstract class AbstractTest {
   
	protected MockMvc mockMvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    /*@Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();*/

    protected void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    /*public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .apply(documentationConfiguration(this.restDocumentation))
            .build();
    }*/

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
        throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
}
