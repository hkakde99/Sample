package com.spring.boot.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.spring.boot.config.FileStorageProperties;


@SpringBootApplication
@EnableJpaRepositories(basePackages = ("com.spring.boot"))
@EntityScan(basePackages = ("com.spring.boot.entities"))
@ComponentScan(basePackages = ("com.spring.boot"))
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
