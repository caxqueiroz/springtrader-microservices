package io.pivotal.springtrader.quotes.config;

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
 * Created by cax on 28/11/2015.
 */
@Configuration
@Profile("local")
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = {"io.pivotal.springtrader.quotes"})
public class MongoLocalConfig extends AbstractMongoConfiguration{

    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) {
        return new MongoTemplate(mongoDbFactory);
    }

    @Override
    protected String getDatabaseName() {
        return "STOCKS";
    }

    @Override
    public Mongo mongo() throws Exception {
        Fongo fongo = new Fongo("mongo server 1");
        return fongo.getMongo();
//        return new MongoClient("localhost", 27017);
    }

    @Override
    protected String getMappingBasePackage() {
        return "io.pivotal.springtrader.quotes.domain";
    }
}
