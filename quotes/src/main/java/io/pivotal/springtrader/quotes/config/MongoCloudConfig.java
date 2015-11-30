package io.pivotal.springtrader.quotes.config;


import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;

/**
 * Created by cax on 28/11/2015.
 */
@Configuration
@Profile("cloud")
public class MongoCloudConfig extends AbstractCloudConfig {


    @Bean
    public MongoDbFactory mongoDbFactory() {
        return connectionFactory().mongoDbFactory();
    }

}
