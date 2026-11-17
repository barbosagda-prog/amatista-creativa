package com.amatista.tienda.service;

import com.amatista.tienda.dto.OrderRequest;
import com.amatista.tienda.model.Order;
import com.amatista.tienda.model.OrderItem;
import com.amatista.tienda.model.Product;
import com.amatista.tienda.repository.OrderRepository;
import com.amatista.tienda.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public Order create(OrderRequest req) {
        List<String> ids = req.items().stream()
                .map(OrderRequest.Item::productId)
                .distinct()
                .toList();

        Map<String, Product> products = productRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        List<OrderItem> items = new ArrayList<>();
        long total = 0;

        for (OrderRequest.Item it : req.items()) {
            Product p = products.get(it.productId());
            if (p == null || !p.isActive()) {
                throw bad("Producto no disponible");
            }
            String color = elegido(it.color(), p.getColors(), "color");
            String size = elegido(it.size(), p.getSizes(), "talla");

            items.add(OrderItem.builder()
                    .productoId(p.getId())
                    .name(p.getName())
                    .unitPrice(p.getPriceUnit())
                    .color(color)
                    .size(size)
                    .quantity(it.quantity())
                    .build());

            total += p.getPriceUnit() * it.quantity();
        }

        return orderRepository.save(Order.builder()
                .createdAt(Instant.now())
                .status("PENDIENTE")
                .customerName(req.name().trim())
                .customerPhone(req.phone().trim())
                .customerEmail(req.email().trim())
                .city(req.city().trim())
                .shippingAddress(req.address().trim())
                .notes(req.notes())
                .items(items)
                .total(total)
                .build());
    }

    private String elegido(String valor, List<String> permitidos, String campo) {
        boolean exige = permitidos != null && !permitidos.isEmpty();
        if (!exige) {
            return null;
        }
        if (valor == null || !permitidos.contains(valor)) {
            throw bad("Falta " + campo + " válido");
        }
        return valor;
    }

    private ResponseStatusException bad(String mensaje) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, mensaje);
    }
}