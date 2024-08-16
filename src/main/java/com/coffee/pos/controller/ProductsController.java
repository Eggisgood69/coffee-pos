package com.coffee.pos.controller;

import com.coffee.pos.dto.CommonListResponse;
import com.coffee.pos.dto.CommonObjectResponse;
import com.coffee.pos.dto.products.CreateProductsDTO;
import com.coffee.pos.enums.CommonStatus;
import com.coffee.pos.model.Products;
import com.coffee.pos.service.ProductsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

    @Autowired
    private ProductsService productsService;

    @GetMapping
    public ResponseEntity<CommonListResponse<Products>> getProducts(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort.Direction direction =
                sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction, sortBy));
        Page<Products> productsList;
        if (name != null && !name.isEmpty()) {
            productsList = productsService.findByName(name, pageable);
        } else {
            productsList = productsService.getAll(pageable);
        }
        CommonListResponse<Products> response = new CommonListResponse<>("Success", CommonStatus.SUCCESS, productsList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonObjectResponse> getProductById(@PathVariable String id) {
        Products products = productsService.findById(id);
        CommonObjectResponse response = new CommonObjectResponse("Success", CommonStatus.SUCCESS, products);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CommonObjectResponse> saveProducts(@RequestBody CreateProductsDTO products) {
        Products newProducts = productsService.save(products);
        CommonObjectResponse response = new CommonObjectResponse("Success", CommonStatus.SUCCESS, newProducts);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CommonObjectResponse> updateProduct(@PathVariable String id, @RequestBody CreateProductsDTO createProductsDTO) {
        Products existProducts = productsService.findById(id);
        if (existProducts == null) {
            return ResponseEntity.notFound().build();
        }
        if (createProductsDTO.getName() != null) {
            existProducts.setName(createProductsDTO.getName());
        }
        if (createProductsDTO.getDescription() != null) {
            existProducts.setDescription(createProductsDTO.getDescription());
        }
        existProducts.setUpdateAt(LocalDateTime.now());
        existProducts = productsService.save(existProducts);
        CommonObjectResponse response = new CommonObjectResponse("Success", CommonStatus.SUCCESS, existProducts);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}