package com.advinow.mica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.advinow.mica.swagger.config.SwaggerConfig;

import springfox.documentation.swagger2.annotations.EnableSwagger2;



/**
 * Spring boot class loads all the dependency classes and required spring configurations.
 * 
 * @author Govinda Reddy
 *
 */
@EnableTransactionManagement
@EnableNeo4jRepositories
@SpringBootApplication
@EnableWebMvc
@ComponentScan
@EnableSwagger2
@Import(SwaggerConfig.class)
public class MICAApplication extends SpringBootServletInitializer  {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MICAApplication.class, args);
    }
 
}
