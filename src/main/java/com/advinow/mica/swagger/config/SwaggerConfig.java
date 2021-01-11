package com.advinow.mica.swagger.config;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
/**
 * 
 * @author Govinda Reddy
 *
 */
@Configuration
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig extends WebMvcConfigurationSupport {

    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES =
        new HashSet<>(Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));

    private static final Contact DEFAULT_CONTACT = new Contact(
        "Advinow", "http://www.advinow.com", "advinow@support.com");

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
            .build()
            .produces(DEFAULT_PRODUCES_AND_CONSUMES)
            .consumes(DEFAULT_PRODUCES_AND_CONSUMES)
             .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
            .title("Neo4j Java services")
            .description("Neo4j java services")
            .version("1.0.0")
            .termsOfServiceUrl("urn:tos")
            .contact(DEFAULT_CONTACT)
            .license("Apache 2.0")
            .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
            .build();
    }
    

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
