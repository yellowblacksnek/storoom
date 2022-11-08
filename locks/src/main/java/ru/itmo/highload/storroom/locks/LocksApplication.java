package ru.itmo.highload.storroom.locks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class LocksApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocksApplication.class, args);
	}

}
