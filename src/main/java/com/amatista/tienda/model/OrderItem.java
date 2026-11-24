package com.amatista.tienda.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class OrderItem {

    private String productoId;
    private String name;
    private long unitPrice;
    private String color;
    private String size;
    private int quantity;
    }
