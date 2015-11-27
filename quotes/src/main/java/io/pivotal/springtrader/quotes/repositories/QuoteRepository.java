package io.pivotal.springtrader.quotes.repositories;

import io.pivotal.springtrader.quotes.domain.Quote;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by cax on 27/11/2015.
 */
public interface QuoteRepository extends CrudRepository<Quote,String>{

}
