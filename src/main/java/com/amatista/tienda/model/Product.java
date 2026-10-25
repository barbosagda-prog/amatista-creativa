package com.amatista.tienda.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "products")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {

    @Id
    private String id;

    @Indexed
    private String category;

    private String name;
    private String description;

    private long priceUnit;
    private Long priceWholesale;

    private List<String> colors;
    private List<String> shapes;

    private String imageUrl;

    @Builder.Default
    private boolean active = true;
    private int position;
}