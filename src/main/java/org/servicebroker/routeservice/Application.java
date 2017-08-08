package org.servicebroker.routeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;


@SpringBootApplication
//@Profile("cloud")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}