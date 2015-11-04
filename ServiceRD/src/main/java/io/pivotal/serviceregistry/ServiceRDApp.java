package io.pivotal.serviceregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServiceRDApp {

    public static void main(String[] args) {
        SpringApplication.run(ServiceRDApp.class, args);
    }
}
