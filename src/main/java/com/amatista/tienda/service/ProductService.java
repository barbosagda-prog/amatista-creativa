package com.amatista.tienda.service;

import com.amatista.tienda.model.Product;
import com.amatista.tienda.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Map<String, List<Product>> groupedByCategory() {
        return repository.findByActiveTrueOrderByPositionAsc().stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        LinkedHashMap::new,
                        Collectors.toList()));
    }
}