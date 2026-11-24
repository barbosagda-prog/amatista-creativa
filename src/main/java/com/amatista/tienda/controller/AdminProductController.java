package com.amatista.tienda.controller;

import com.amatista.tienda.dto.ProductForm;
import com.amatista.tienda.service.AdminProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", service.listAll());
        return "admin/products";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("form", new ProductForm());
        return formView(model, "Nuevo producto", "/admin/products", null);
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") ProductForm form,
                         BindingResult errors, Model model, RedirectAttributes redirect) {
        validarPrecios(form, errors);
        if (!errors.hasErrors()) {
            try {
                service.create(form);
                redirect.addFlashAttribute("ok", "Producto creado.");
                return "redirect:/admin/products";
            } catch (IllegalArgumentException e) {
                errors.rejectValue("image", "invalid", e.getMessage());
            } catch (IOException e) {
                errors.rejectValue("image", "io", "No se pudo guardar la imagen.");
            }
        }
        return formView(model, "Nuevo producto", "/admin/products", null);
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, Model model) {
        var product = service.get(id);
        model.addAttribute("form", service.toForm(product));
        return formView(model, "Editar producto", "/admin/products/" + id, product.getImageUrl());
    }

    @PostMapping("/{id}")
    public String update(@PathVariable String id,
                         @Valid @ModelAttribute("form") ProductForm form,
                         BindingResult errors, Model model, RedirectAttributes redirect) {
        validarPrecios(form, errors);
        if (!errors.hasErrors()) {
            try {
                service.update(id, form);
                redirect.addFlashAttribute("ok", "Producto actualizado.");
                return "redirect:/admin/products";
            } catch (IllegalArgumentException e) {
                errors.rejectValue("image", "invalid", e.getMessage());
            } catch (IOException e) {
                errors.rejectValue("image", "io", "No se pudo guardar la imagen.");
            }
        }
        return formView(model, "Editar producto", "/admin/products/" + id,
                service.get(id).getImageUrl());
    }

    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable String id, RedirectAttributes redirect) {
        service.toggleActive(id);
        redirect.addFlashAttribute("ok", "Visibilidad actualizada.");
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id, RedirectAttributes redirect) {
        service.delete(id);
        redirect.addFlashAttribute("ok", "Producto eliminado.");
        return "redirect:/admin/products";
    }

    private String formView(Model model, String title, String action, String currentImage) {
        model.addAttribute("title", title);
        model.addAttribute("action", action);
        model.addAttribute("currentImage", currentImage);
        model.addAttribute("categories", service.categories());
        return "admin/product-form";
    }

    private void validarPrecios(ProductForm form, BindingResult errors) {
        if (form.getPriceUnit() != null && form.getPriceWholesale() != null
                && form.getPriceWholesale() >= form.getPriceUnit()) {
            errors.rejectValue("priceWholesale", "invalid",
                    "El precio por mayor debe ser menor al unitario");
        }
    }
}