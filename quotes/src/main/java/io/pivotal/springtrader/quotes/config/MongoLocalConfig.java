package io.pivotal.springtrader.quotes.config;


import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import io.pivotal.springtrader.quotes.domain.Stock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.concurrent.TimeUnit;

/**
 * Created by cax on 28/11/2015.
 */
@Configuration
@Profile("local")
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = {"io.pivotal.springtrader.quotes"})
public class MongoLocalConfig extends AbstractMongoConfiguration {

    @Value("${expiration.time}")
    int expirationTime;

    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
        if(expirationTime >=0)
            mongoTemplate.
                    indexOps(Stock.class).
                    ensureIndex((new Index().on("lastModifiedDate", Sort.Direction.ASC).
                            expire(expirationTime, TimeUnit.HOURS)));
        return mongoTemplate;
    }

    @Override
    protected String getDatabaseName() {
        return "Quotes";
    }

    @Override
    public Mongo mongo() throws Exception {
        Mongo mongo = new MongoClient("localhost", 27017);
        return mongo;
    }

    @Override
    protected String getMappingBasePackage() {
        return "io.pivotal.springtrader.quotes.domain";
    }
}
