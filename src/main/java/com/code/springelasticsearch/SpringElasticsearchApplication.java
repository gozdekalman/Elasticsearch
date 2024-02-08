package com.code.springelasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
@EnableElasticsearchRepositories
@SpringBootApplication
public class SpringElasticsearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringElasticsearchApplication.class, args);
    }

}
