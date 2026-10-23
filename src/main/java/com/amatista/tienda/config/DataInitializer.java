package com.amatista.tienda.config;

import com.amatista.tienda.model.Product;
import com.amatista.tienda.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedProducts(ProductRepository repo) {
        return args -> {
            if (repo.count() > 0) return;

            repo.saveAll(List.of(
                    Product.builder().category("Mugs").name("Mug blanco 11 onzas")
                            .priceUnit(16000).priceWholesale(13000L).build(),
                    Product.builder().category("Mugs").name("Mug negro 11 onzas")
                            .priceUnit(18000).priceWholesale(15000L).build(),
                    Product.builder().category("Mini-mugs").name("Mug blanco 6 onzas")
                            .priceUnit(10000).priceWholesale(9000L).build(),
                    Product.builder().category("Llaveros").name("Llavero acrílico blanco")
                            .priceUnit(4000)
                            .shapes(List.of("Rombo", "Círculo", "Cuadrado", "Corazón")).build(),
                    Product.builder().category("Camisas").name("Camisa algodón 180")
                            .priceUnit(19000).priceWholesale(16000L)
                            .colors(List.of("Blanco", "Negro", "Verde militar")).build()
            ));
        };
    }
}