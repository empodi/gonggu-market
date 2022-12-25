package com.gonggu.deal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.util.TimeZone;
@EnableAsync
@SpringBootApplication
public class GongguApplication {

	public static void main(String[] args) {
		SpringApplication.run(GongguApplication.class, args);
	}

}
