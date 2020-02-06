package com.neuai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.neuai.controller" })
public class NiotCloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(NiotCloudApplication.class, args);
	}

}