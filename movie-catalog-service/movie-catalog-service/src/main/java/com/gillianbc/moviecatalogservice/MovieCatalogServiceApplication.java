package com.gillianbc.moviecatalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
public class MovieCatalogServiceApplication {

	// The @LoadBalanced annotation allows us to consume a discovered service using our RestTemplate
	// Services will automatically be discovered by Spring on the default Eureka Discovery Server port
	// of 8761
	@Bean
	@LoadBalanced
	public RestTemplate makeRestTemplateBean(){
		return  new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(MovieCatalogServiceApplication.class, args);
	}

}
