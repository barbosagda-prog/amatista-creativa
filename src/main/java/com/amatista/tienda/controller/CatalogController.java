package com.amatista.tienda.controller;

import com.amatista.tienda.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CatalogController {

    private final ProductService productService;

    @GetMapping("/")
    public String home(@RequestParam(required = false) String category, Model model) {
        model.addAttribute("products", productService.listActive(category));
        model.addAttribute("selectedCategory", category);
        return "catalog";
    }
}