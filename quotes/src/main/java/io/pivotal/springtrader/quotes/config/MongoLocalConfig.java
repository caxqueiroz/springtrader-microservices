package io.pivotal.springtrader.quotes.config;

import io.pivotal.springtrader.quotes.repositories.StockRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by cax on 28/11/2015.
 */
@Configuration
@Profile("local")
@EnableMongoRepositories(basePackageClasses = {StockRepository.class})
public class MongoLocalConfig {

    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) {
        return new MongoTemplate(mongoDbFactory);
    }
}
