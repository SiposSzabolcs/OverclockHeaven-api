package com.example.OverclockHeaven.Products;

import com.example.OverclockHeaven.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductResource {
    private final ProductService productService;

    @GetMapping("/get")
    public ResponseEntity<List> getProducts(){
        return ResponseEntity.ok().body(productService.getAllProducts());
    }

    @PostMapping("/add")
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        try{
            return ResponseEntity.created(URI.create("/products/" + product.getId())).body(productService.createProduct(product));
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("An error occurred: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "id") Integer id) {
        try {
            ProductDTO deletedProductDTO = productService.deleteProduct(id);
            return ResponseEntity.ok(deletedProductDTO);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("An error occurred: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
