package com.amatista.tienda.controller;

import com.amatista.tienda.model.Order;
import com.amatista.tienda.service.OrderService;
import com.amatista.tienda.dto.OrderRequest;
import com.amatista.tienda.dto.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@Valid @RequestBody OrderRequest request) {
        Order order = orderService.create(request);
        String id = order.getId();
        String code = id.substring(id.length() - 6).toUpperCase();
        return new OrderResponse(id, code, order.getTotal());
    }
}