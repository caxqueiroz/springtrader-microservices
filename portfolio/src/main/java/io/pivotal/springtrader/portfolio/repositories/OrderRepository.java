package io.pivotal.springtrader.portfolio.repositories;


import io.pivotal.springtrader.portfolio.domain.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 
 * @author David Ferreira Pinto
 *
 */
public interface OrderRepository extends CrudRepository<Order,Integer> {

	List<Order> findByAccountId(String accountId);
}
