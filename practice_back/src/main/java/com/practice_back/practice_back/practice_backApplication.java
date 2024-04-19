package com.practice_back.practice_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication(
		// aws sdk 를 사용하기 위해 ec2에 대한 메타데이터가 필요한데, 로컬 환경은 ec2메타 데이터를 못가져오기 때문에 해당 메타 데이터를 필요로 하는 것을 exclude 함
		exclude = {
		org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration.class,
		org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration.class,
		org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration.class
})
@EntityScan(basePackages = { "com.practice_back.entity" })
@EnableJpaRepositories(basePackages = { "com.practice_back.repository" })
@ComponentScan(basePackages = { "com.practice_back.api", "com.practice_back.service", "com.practice_back.config","com.practice_back.jwt","com.practice_back.handler" })
public class practice_backApplication {

	public static void main(String[] args) {
		SpringApplication.run(practice_backApplication.class, args);
	}
	@PostConstruct
	public void setDefaultTimeZone(){
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}
}
