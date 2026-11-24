package com.amatista.tienda.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheckoutController {

    @Value("${app.store.whatsapp}")
    private String whatsapp;

    @GetMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("whatsapp", whatsapp);
        return "checkout";
    }
}