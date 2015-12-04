package io.pivotal.springtrader.quotes;

import com.github.fakemongo.Fongo;
import com.mongodb.Mongo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by cq on 4/12/15.
 */
@Configuration
@Profile("test")
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = {"io.pivotal.springtrader.quotes"})
public class MongoTestConfig extends AbstractMongoConfiguration {

    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) {
        return new MongoTemplate(mongoDbFactory);
    }

    @Override
    protected String getDatabaseName() {
        return "Stocks";
    }

    @Override
    public Mongo mongo() throws Exception {
        Fongo fongo = new Fongo("mongo server local");
        return fongo.getMongo();

    }

    @Override
    protected String getMappingBasePackage() {
        return "io.pivotal.springtrader.quotes.domain";
    }
}