package com.amatista.tienda.repository;

import com.amatista.tienda.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
