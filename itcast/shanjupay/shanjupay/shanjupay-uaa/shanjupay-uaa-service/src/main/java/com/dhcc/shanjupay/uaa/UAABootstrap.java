package com.dhcc.shanjupay.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class UAABootstrap {

	
	public static void main(String[] args) {
		SpringApplication.run(UAABootstrap.class, args);

	}
	

}
