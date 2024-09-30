package com.example.SpringMVN;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableMongoRepositories
@EnableTransactionManagement

public class SpringMvnApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMvnApplication.class, args);
	}
    @Bean
	public PlatformTransactionManager manager(MongoDatabaseFactory dbFactory){
        return new MongoTransactionManager(dbFactory);
	}
}
