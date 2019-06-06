package com.miracle.feature.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan({ "com.miracle.database.connection", "com.miracle.controller", "com.miracle.config",
		"com.miracle.utility", "com.miracle.feature.*" })
@EnableSwagger2
//@EnableMongoRepositories({ "com.miracle.database.connection" })
public class APIFeatureService extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(APIFeatureService.class, args);
	}

	@Override
	public SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(APIFeatureService.class);
	}

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.miracle"))
				// .paths(PathSelectors.ant("/.*"))
				.build();
	}

}
