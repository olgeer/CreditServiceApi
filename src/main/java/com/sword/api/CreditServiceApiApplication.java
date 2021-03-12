package com.sword.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
//@EnableDiscoveryClient
/**
 * Credit service spring boot main class
 * @author max
 */
public class CreditServiceApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CreditServiceApiApplication.class, args);
    }
}
