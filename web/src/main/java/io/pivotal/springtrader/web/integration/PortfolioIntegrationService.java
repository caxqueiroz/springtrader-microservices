package io.pivotal.springtrader.web.integration;

import io.pivotal.springtrader.web.domain.Order;
import io.pivotal.springtrader.web.domain.Portfolio;
import io.pivotal.springtrader.web.exceptions.OrderNotSavedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by cq on 3/11/15.
 */
@Service
public class PortfolioIntegrationService {

    private static final Logger logger = LoggerFactory.getLogger(PortfolioIntegrationService.class);


    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;


    public Order sendOrder(Order order ) throws OrderNotSavedException {
        logger.debug("send order: " + order);

        //check result of http request to ensure its ok.

        ResponseEntity<Order> result = restTemplate.postForEntity("http://portfolio/portfolio/{accountId}", order, Order.class, order.getAccountId());
        if (result.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            throw new OrderNotSavedException("Could not save the order");
        }
        logger.debug("Order saved:: " + result.getBody());
        return result.getBody();
    }

    public Portfolio getPortfolio(String accountId) {
        Portfolio folio = restTemplate.getForObject("http://portfolio/portfolio/{accountid}", Portfolio.class, accountId);
        logger.debug("Portfolio received: " + folio);
        return folio;
    }
}
