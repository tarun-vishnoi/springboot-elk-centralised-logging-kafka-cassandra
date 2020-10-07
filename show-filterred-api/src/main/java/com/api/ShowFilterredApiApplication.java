package com.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableCassandraRepositories(basePackages = "com.api.pojo")
@EnableElasticsearchRepositories(basePackages = "com.api.doc")
public class ShowFilterredApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShowFilterredApiApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}
