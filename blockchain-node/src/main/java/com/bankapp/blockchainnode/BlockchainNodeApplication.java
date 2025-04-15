package com.bankapp.blockchainnode;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import java.security.KeyPair;

@SpringBootApplication
@EnableDiscoveryClient
public class BlockchainNodeApplication {
    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(BlockchainNodeApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
