package com.amatista.tienda.controller;

import com.amatista.tienda.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CatalogController {

    private static final Map<String, String> COLOR_HEX = Map.ofEntries(
            Map.entry("Rojo", "#FF3B3B"),
            Map.entry("Azul", "#0A2FD0"),
            Map.entry("Amarillo", "#FFE156"),
            Map.entry("Verde oliva", "#5C7410"),
            Map.entry("Negro", "#000000"),
            Map.entry("Naranja", "#FF9800"),
            Map.entry("Azul oscuro", "#0B2A5B"),
            Map.entry("Verde oscuro", "#163A0F"),
            Map.entry("Verde", "#00803E"),
            Map.entry("Rosado", "#FFC0C7"),
            Map.entry("Celeste", "#8FD8FF"),
            Map.entry("Verde claro", "#BFEFB0"),
            Map.entry("Amarillo lima", "#E6FF6B"),
            Map.entry("Lila", "#DDB6F7"),
            Map.entry("Verde militar", "#6B7565"),
            Map.entry("Blanco", "#FFFFFF"));

    private final ProductService productService;

    @Value("${app.store.whatsapp}")
    private String whatsapp;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("grouped", productService.groupedByCategory());
        model.addAttribute("colorHex", COLOR_HEX);
        model.addAttribute("whatsapp", whatsapp);
        return "catalog";
    }
}