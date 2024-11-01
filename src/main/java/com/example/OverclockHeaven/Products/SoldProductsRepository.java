package com.example.OverclockHeaven.Products;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SoldProductsRepository extends JpaRepository<SoldProduct, Integer> {
    Optional<SoldProduct> findByProductId(Integer productId);
}
