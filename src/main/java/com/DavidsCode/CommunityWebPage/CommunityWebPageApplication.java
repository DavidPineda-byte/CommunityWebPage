package com.DavidsCode.CommunityWebPage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CommunityWebPageApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommunityWebPageApplication.class, args);
	}

}
