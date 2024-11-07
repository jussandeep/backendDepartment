package com.example.DepartmentProj.restTempletConfigure;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
public class configure {
	
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(){
        return new RestTemplate();
    }
	
	 @Bean
		MongoTransactionManager mongoTransactionManager(MongoTemplate mongoTemplate) {
	        return new MongoTransactionManager(mongoTemplate.getMongoDatabaseFactory());
	    }

}
