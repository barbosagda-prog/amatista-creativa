package com.amatista.tienda.service;

import com.amatista.tienda.model.Product;
import com.amatista.tienda.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public List<Product> listActive(String category) {
        if (category == null || category.isBlank()) {
            return repository.findByActiveTrueOrderByCategoryAscNameAsc();
        }
        return repository.findByCategoryAndActiveTrueOrderByNameAsc(category);
    }
}