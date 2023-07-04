package com.tuespotsolutions.config;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import springfox.documentation.spi.service.contexts.SecurityContext;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {
	
	
	Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);
	
	public static final String title              = "Job Solutions";
	public static final String description        = "Job Sicker App";
	public static final String version            = "0.1";
	public static final String termsOfServiceUrl  = "https://tuespotsolution.com";
	public static final String contactName        = "078144-78557";
	public static final String license            = "tuespot123456789";
	public static final String licenseUrl         = "https://tuespotsolution.com";
	
	
	private ApiKey apiKey() { 
		logger.info("line no 43 : ApiKey Method");
	    return new ApiKey("JWT", "Authorization", "header"); 
	}
	
	private SecurityContext securityContext() { 
		logger.info("line no 48 : SecurityContext Method");
	    return SecurityContext.builder().securityReferences(defaultAuth()).build(); 
	} 
	
	private List<SecurityReference> defaultAuth() { 
		logger.info("line no 53 : defaultAuth Method");
	    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything"); 
	    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1]; 
	    authorizationScopes[0] = authorizationScope; 
	    return Arrays.asList(new SecurityReference("JWT", authorizationScopes)); 
	}

	
	private  static final String[] swaggerUrl = {
			"/swagger-ui/**",
			"/v3/api-docs",
			"/webjars/**"
	};
	
	 @Bean
	    public Docket api() { 
		 logger.info("line no 53 : api Method return Docket");
		 return new Docket(DocumentationType.SWAGGER_2)
			      .apiInfo(getInfo())
			      .securityContexts(Arrays.asList(securityContext()))
			      .securitySchemes(Arrays.asList(apiKey()))
			      .select()
			      .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
			      .paths(PathSelectors.any())
			      .build();
	    }
	
	@SuppressWarnings("deprecation")
	private ApiInfo getInfo() {
		logger.info("line no 53 : getInfo() Method");
		// TODO Auto-generated method stub
		return new ApiInfo(title, description, version, termsOfServiceUrl, contactName, license, licenseUrl);
	}
	

	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {	
		logger.info("line no 53 : addResourceHandlers() Method");
        registry.addResourceHandler(swaggerUrl)
                .addResourceLocations("classpath:/META-INF/resources/**");
   }

}
