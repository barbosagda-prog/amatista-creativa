package com.amatista.tienda.repository;

import com.amatista.tienda.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByActiveTrueOrderByPositionAsc();
}