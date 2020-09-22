package com.rakuten.buildcharacterbackend.infrastructure.configuration;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Value("${info.name}")
	private String appName;

	@Value("${info.description}")
	private  String appDesc;

	@Value("${info.version}")
	private String appVersion;

	final ApiInfo apiInfo() {
		return new ApiInfoBuilder()
					.title(appName)
					.description(appDesc)
					.license("")
					.licenseUrl("")
					.termsOfServiceUrl("")
					.version(appVersion)
					.contact(
					new Contact(
						"Renan Xavier Calmon",
						"https://github.com/renanxc",
						"renanxcalmon@gmail.com")
					)
					.build();
	}

	@Bean
	public Docket customImplementation() {
		return new Docket(DocumentationType.SWAGGER_2)
					.select()
					.apis(RequestHandlerSelectors.basePackage("com.rakuten"))
					.build()
					.directModelSubstitute(LocalDate.class, java.sql.Date.class)
					.directModelSubstitute(Date.class, java.util.Date.class)
					.apiInfo(apiInfo());
	}

}