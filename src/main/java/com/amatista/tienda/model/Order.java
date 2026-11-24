package com.amatista.tienda.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder

public class Order {

    @Id
    private String id;

    private Instant createdAt;
    private String status;

    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String city;
    private String shippingAddress;
    private String notes;

    private List<OrderItem> items;
    private long total;
}
