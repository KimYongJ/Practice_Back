package com.practice_back.practice_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EntityScan(basePackages = { "com.practice_back.entity" })
@EnableJpaRepositories(basePackages = { "com.practice_back.repository" })
@ComponentScan(basePackages = { "com.practice_back.api", "com.practice_back.service", "com.practice_back.config","com.practice_back.jwt","com.practice_back.handler" })
@EnableJpaAuditing
public class practice_backApplication {

	public static void main(String[] args) {
		SpringApplication.run(practice_backApplication.class, args);
	}
	@PostConstruct
	public void setDefaultTimeZone(){
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}
}
