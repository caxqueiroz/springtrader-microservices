package io.pivotal.springtrader.quotes.repositories;

import io.pivotal.springtrader.quotes.domain.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by cax on 28/11/2015.
 */
@Repository
public interface StockRepository extends MongoRepository<Stock, String>{

}
