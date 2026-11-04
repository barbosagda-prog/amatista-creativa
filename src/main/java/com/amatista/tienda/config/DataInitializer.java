package com.amatista.tienda.config;

import com.amatista.tienda.model.Product;
import com.amatista.tienda.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.Normalizer;
import java.util.List;

@Configuration
public class DataInitializer {

    private static final List<String> VIBRANTES = List.of(
            "Rojo", "Azul", "Amarillo", "Verde oliva", "Negro", "Naranja", "Azul oscuro", "Verde oscuro");
    private static final List<String> VIBRANTES_MINI = List.of(
            "Rojo", "Azul", "Amarillo", "Verde oliva", "Naranja", "Azul oscuro");
    private static final List<String> PASTEL = List.of(
            "Rosado", "Celeste", "Verde claro", "Amarillo lima", "Lila");
    private static final List<String> MAGICO_INTERNO = List.of(
            "Rojo", "Azul", "Verde", "Naranja", "Celeste", "Rosado");
    private static final List<String> TEXTIL_11 = List.of(
            "Rojo", "Azul", "Amarillo", "Verde claro", "Negro", "Naranja",
            "Rosado", "Verde militar", "Lila", "Celeste", "Blanco");
    private static final List<String> TEXTIL_10 = List.of(
            "Rojo", "Azul", "Amarillo", "Verde claro", "Negro", "Naranja",
            "Rosado", "Verde militar", "Lila", "Celeste");

    private static final String SUB = "Listo para personalizar con tu diseño por sublimación.";
    private static final String MAGICO = "Cambia de color con el calor y revela tu diseño.";

    @Bean
    CommandLineRunner seedProducts(ProductRepository repo) {
        return args -> {
            if (repo.count() > 0) return;

            List<Product> all = List.of(
                    // MUGS
                    p("Mugs", "Mug blanco 11 onzas", 16000, 13000L, SUB, null, null),
                    p("Mugs", "Mug negro 11 onzas", 18000, 15000L, SUB, null, null),
                    p("Mugs", "Mug color vibrante interno 11 onzas", 19000, 16000L, SUB, VIBRANTES, null),
                    p("Mugs", "Mug color pastel interno 11 onzas", 19000, 16000L, SUB, PASTEL, null),
                    p("Mugs", "Mug mágico blanco 11 onzas", 22000, 20000L, MAGICO, null, null),
                    p("Mugs", "Mug mágico negro 11 onzas", 24000, 22000L, MAGICO, null, null),
                    p("Mugs", "Mug mágico escarchado 11 onzas", 26000, 24000L, MAGICO, null, null),
                    p("Mugs", "Mug mágico color interno 11 onzas", 25000, 23000L, MAGICO, MAGICO_INTERNO, null),
                    // MINI-MUGS
                    p("Mini-mugs", "Mug blanco 6 onzas", 10000, 9000L, SUB, null, null),
                    p("Mini-mugs", "Mug color vibrante interno 6 onzas", 14000, 12000L, SUB, VIBRANTES_MINI, null),
                    p("Mini-mugs", "Mug mágico blanco 6 onzas", 18000, 16000L, MAGICO, null, null),
                    p("Mini-mugs", "Mug mágico escarchado 6 onzas", 22000, 19000L, MAGICO, null, null),
                    // RECIPIENTES
                    p("Recipientes", "Botilito de lata 300 ml", 35000, 30000L, SUB, null, null),
                    p("Recipientes", "Lata 350 ml", 18000, 15000L, SUB, null, null),
                    p("Recipientes", "Botella deportiva", 19000, 16000L, SUB, VIBRANTES, null),
                    p("Recipientes", "Caramañola", 20000, 17000L, SUB, PASTEL, null),
                    // LLAVEROS (un solo precio)
                    p("Llaveros", "Llavero acrílico blanco", 4000, null, SUB, null,
                            List.of("Rombo", "Círculo", "Rectángulo", "Corazón")),
                    p("Llaveros", "Llavero acrílico transparente", 4300, null, SUB, null,
                            List.of("Rombo", "Círculo", "Rectángulo", "Corazón", "Hexágono")),
                    p("Llaveros", "Llavero acrílico negro", 4600, null, SUB, null,
                            List.of("Cuadrado", "Círculo", "Rectángulo", "Corazón", "Hexágono")),
                    p("Llaveros", "Llavero madera DMF", 5000, null, SUB, null,
                            List.of("Rombo", "Círculo", "Rectángulo", "Corazón")),
                    // LÁMINAS
                    p("Láminas", "Lámina de 30x40", 40000, 37000L, SUB, null, null),
                    p("Láminas", "Lámina de 23x29", 33000, 30000L, SUB, null, null),
                    p("Láminas", "Lámina de 20x30", 30000, 27000L, SUB, null, null),
                    p("Láminas", "Lámina de 15x20", 25000, 22000L, SUB, null, null),
                    // ACRÍLICOS
                    p("Acrílicos", "Portarretrato rectangular", 18000, 16000L, SUB, null, null),
                    p("Acrílicos", "Portarretrato corazón", 18000, 16000L, SUB, null, null),
                    p("Acrílicos", "Portarretrato hexagonal", 18000, 16000L, SUB, null, null),
                    // CAMISAS
                    p("Camisas", "Camisa piel de durazno", 22000, 19000L, SUB, List.of("Blanco", "Negro", "Rosado"), null),
                    p("Camisas", "Camisa algodón licrado", 27000, 25000L, SUB, TEXTIL_11, null),
                    p("Camisas", "Camisa seda fría", 30000, 28000L, SUB, TEXTIL_10, null),
                    p("Camisas", "Camisa algodón 180", 19000, 16000L, SUB, List.of("Blanco", "Negro", "Verde militar"), null),
                    // BUZOS
                    p("Buzos", "Buzo cerrado con capota", 70000, 64000L, SUB, TEXTIL_11, null),
                    p("Buzos", "Buzo cerrado cuello redondo", 65000, 58000L, SUB, TEXTIL_11, null),
                    p("Buzos", "Buzo abierto con capota", 72000, 65000L, SUB, TEXTIL_11, null),
                    // GORRAS
                    p("Gorras", "Gorra básica tracker", 12000, 9500L, SUB, TEXTIL_11, null),
                    p("Gorras", "Gorra flex poliéster", 16000, 13000L, SUB, TEXTIL_11, null),
                    p("Gorras", "Gorra flex microfibra", 23000, 18000L, SUB, TEXTIL_11, null),
                    p("Gorras", "Gorra premium", 40000, 35000L, SUB, TEXTIL_11, null)
            );

            for (int i = 0; i < all.size(); i++) {
                Product x = all.get(i);
                x.setPosition(i + 1);
                switch (x.getCategory()) {
                    case "Camisas", "Buzos" -> x.setSizes(TALLAS_ROPA);
                    case "Gorras" -> x.setSizes(TALLAS_GORRA);
                    default -> { }
                }
            }
            repo.saveAll(all);
        };
    }

    private static Product p(String category, String name, long unit, Long wholesale,
                             String description, List<String> colors, List<String> shapes) {
        return Product.builder()
                .category(category)
                .name(name)
                .description(description)
                .priceUnit(unit)
                .priceWholesale(wholesale)
                .colors(colors)
                .shapes(shapes)
                .imageUrl("/img/products/" + slug(name) + ".jpg")
                .build();
    }

    private static String slug(String text) {
        String n = Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return n.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
    }
    private static final List<String> TALLAS_ROPA = List.of("S", "M", "L", "XL");
    private static final List<String> TALLAS_GORRA = List.of("S/M", "L/XL");
}