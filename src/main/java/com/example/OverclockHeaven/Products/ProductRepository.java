package com.example.OverclockHeaven.Products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository <Product, Integer>{
    Optional<Product> findById(Integer id);
    List<Product> findByTag(String tag);
}
