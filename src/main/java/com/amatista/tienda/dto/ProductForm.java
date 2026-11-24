package com.amatista.tienda.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @NoArgsConstructor
public class ProductForm {

    @NotBlank(message = "Escribe la categoría")
    @Size(max = 60, message = "Máximo 60 caracteres")
    private String category;

    @NotBlank(message = "Escribe el nombre")
    @Size(max = 120, message = "Máximo 120 caracteres")
    private String name;

    @Size(max = 300, message = "Máximo 300 caracteres")
    private String description;

    @NotNull(message = "Escribe el precio")
    @Positive(message = "El precio debe ser mayor a cero")
    private Long priceUnit;

    @Positive(message = "El precio por mayor debe ser mayor a cero")
    private Long priceWholesale;

    @Size(max = 300, message = "Máximo 300 caracteres")
    private String colors;

    @Size(max = 300, message = "Máximo 300 caracteres")
    private String shapes;

    @Size(max = 100, message = "Máximo 100 caracteres")
    private String sizes;

    private boolean active = true;

    private MultipartFile image;
}