package com.example.DepartmentProj.securityConfig;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MyConfiglass {

	@Bean
	@LoadBalanced
    @Primary

    public RestTemplate loadBalancedRestTemplate() {
        return new RestTemplate();
    }
	 public RestTemplate restTemplate(){
        return new RestTemplate();
    }
	 
	 
	
}
