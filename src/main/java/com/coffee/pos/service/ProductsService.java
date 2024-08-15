package com.coffee.pos.service;

import com.coffee.pos.dto.products.CreateProductsDTO;
import com.coffee.pos.model.Products;
import com.coffee.pos.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProductsService {

    @Autowired
    private ProductsRepository productsRepository;

    public Page<Products> getAll(Pageable pageable) {
        return productsRepository.findAll(pageable);
    }

    public Products save(CreateProductsDTO products) {
        Products newProducts = mapToEntity(products);
        return productsRepository.save(newProducts);
    }

    public Products save(Products products){
        return productsRepository.save(products);
    }

    public Products findById(String id) {
        return productsRepository.findById(id).orElse(null);
    }

    public Page<Products> findByName(String name, Pageable pageable) {
        return productsRepository.findByNameContaining(name, pageable);
    }

    private Products mapToEntity(CreateProductsDTO products) {
        Products newProducts = new Products();
        newProducts.setName(products.getName());
        newProducts.setDescription(products.getDescription());
        newProducts.setCreate_at(LocalDateTime.now());
        newProducts.setUpdate_at(LocalDateTime.now());
        return newProducts;
    }
}

