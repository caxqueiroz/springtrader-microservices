package io.pivotal.springtrader.quotes;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
//@EnableDiscoveryClient
public class QuotesApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuotesApplication.class,args);
    }
}