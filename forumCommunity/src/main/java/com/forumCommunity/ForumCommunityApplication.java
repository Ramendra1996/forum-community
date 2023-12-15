package com.forumCommunity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableCaching
public class ForumCommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForumCommunityApplication.class, args);
	}

	/*@Bean
	public PollOptions pollOptions() {
		return new PollOptions();
	}*/

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	@Bean
	public WebClient webClient(){
		return  WebClient.create();

	}

	@Autowired
	public ObjectMapper objectMapper(){
		return new ObjectMapper();
	}
}
