package com.coffee.pos.dto.products;
import lombok.Data;

@Data
public class CreateProductsDTO {
    private String name;
    private double price;
    private String description;
}

