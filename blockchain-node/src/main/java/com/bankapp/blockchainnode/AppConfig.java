package com.bankapp.blockchainnode;


import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class AppConfig {
    @Bean
    @Lazy
    public Blockchain blockchain(DiscoveryClient discoveryClient,
                                 NodeClient nodeClient,
                                 UserService userService) {
        return new Blockchain(discoveryClient, nodeClient, userService);
    }
}
