package com.coffee.pos.repository;

import com.coffee.pos.model.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Products, String> {
    Page<Products> findByNameContaining(String name, Pageable pageable);
}
