package com.amatista.tienda.service;

import com.amatista.tienda.dto.ProductForm;
import com.amatista.tienda.model.Product;
import com.amatista.tienda.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository repository;
    private final ImageStorageService images;

    public List<Product> listAll() {
        return repository.findAll(Sort.by("position"));
    }

    public List<String> categories() {
        return listAll().stream().map(Product::getCategory).distinct().toList();
    }

    public Product get(String id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    public ProductForm toForm(Product p) {
        ProductForm f = new ProductForm();
        f.setCategory(p.getCategory());
        f.setName(p.getName());
        f.setDescription(p.getDescription());
        f.setPriceUnit(p.getPriceUnit());
        f.setPriceWholesale(p.getPriceWholesale());
        f.setColors(join(p.getColors()));
        f.setShapes(join(p.getShapes()));
        f.setSizes(join(p.getSizes()));
        f.setActive(p.isActive());
        return f;
    }

    public Product create(ProductForm f) throws IOException {
        Product p = new Product();
        p.setPosition(repository.findFirstByOrderByPositionDesc()
                .map(last -> last.getPosition() + 1)
                .orElse(1));
        apply(p, f);
        return repository.save(p);
    }

    public Product update(String id, ProductForm f) throws IOException {
        Product p = get(id);
        apply(p, f);
        return repository.save(p);
    }

    public void toggleActive(String id) {
        Product p = get(id);
        p.setActive(!p.isActive());
        repository.save(p);
    }

    public void delete(String id) {
        Product p = get(id);
        images.delete(p.getImageUrl());
        repository.deleteById(id);
    }

    private void apply(Product p, ProductForm f) throws IOException {
        p.setCategory(f.getCategory().trim());
        p.setName(f.getName().trim());
        p.setDescription(blankToNull(f.getDescription()));
        p.setPriceUnit(f.getPriceUnit());
        p.setPriceWholesale(f.getPriceWholesale());
        p.setColors(split(f.getColors()));
        p.setShapes(split(f.getShapes()));
        p.setSizes(split(f.getSizes()));
        p.setActive(f.isActive());

        String nueva = images.save(f.getImage());
        if (nueva != null) {
            images.delete(p.getImageUrl());
            p.setImageUrl(nueva);
        }
    }

    private static String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    private static String join(List<String> list) {
        return list == null ? "" : String.join(", ", list);
    }

    private static List<String> split(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        Set<String> set = Arrays.stream(text.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return set.isEmpty() ? null : new ArrayList<>(set);
    }
}