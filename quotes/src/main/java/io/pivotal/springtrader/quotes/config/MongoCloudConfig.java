package io.pivotal.springtrader.quotes.config;

import com.mongodb.Mongo;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.service.common.MongoServiceInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.sql.DataSource;

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
