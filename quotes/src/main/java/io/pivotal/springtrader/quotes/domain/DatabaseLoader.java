package io.pivotal.springtrader.quotes.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pivotal.springtrader.quotes.repositories.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cq on 1/12/15.
 */
@Service
public class DatabaseLoader {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);

    private final StockRepository stockRepository;

    @Autowired
    public DatabaseLoader(StockRepository repository) {
        this.stockRepository = repository;
    }

    @PostConstruct
    void init() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            logger.info("DatabaseLoader:init - Loading data into mongodb");
            Stock[] arrayOfStocks = mapper.readValue(DatabaseLoader.class.getResource("/data.json"),Stock[].class);
            List<Stock> stocks = Arrays.asList(arrayOfStocks);
            stockRepository.save(stocks);
            logger.info(stockRepository.count() + " stocks loaded into the Quotes collection");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}