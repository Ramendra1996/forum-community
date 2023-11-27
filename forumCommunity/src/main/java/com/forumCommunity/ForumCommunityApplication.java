package com.forumCommunity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
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

	@Autowired
	public ObjectMapper objectMapper(){
		return new ObjectMapper();
	}
}
