package com.amatista.tienda.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OrderRequest(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Pattern(regexp = "^[0-9+ ]{7,15}$") String phone,
        @NotBlank @Email @Size(max = 120) String email,
        @NotBlank @Size(max = 80) String city,
        @NotBlank @Size(max = 160) String address,
        @Size(max = 300) String notes,
        @NotEmpty @Size(max = 50) @Valid List<Item> items) {

    public record Item(
            @NotBlank String productId,
            String color,
            String size,
            @Min(1) @Max(99) int quantity) {
    }
}