package com.example.OverclockHeaven.Products;

import com.example.OverclockHeaven.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/sold/get")
    public ResponseEntity<List> getSoldProducts(){
        return ResponseEntity.ok().body(productService.getAllSoldProducts());
    }

    @GetMapping("/get/{tag}")
    public ResponseEntity<List> getProducts(@PathVariable(value = "tag") String tag){
        return ResponseEntity.ok().body(productService.getProductByTag(tag));
    }

    @PostMapping("/rate/{id}")
    public ResponseEntity<?> rateProduct(@PathVariable(value="id") Integer id, @RequestBody Rating rating){
        try{
            return ResponseEntity.ok().body(productService.rateProduct(rating, id));
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("An error occurred: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

    @PostMapping("/add")
    public ResponseEntity<?> createProduct(
            @RequestParam("file") MultipartFile imageFile,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("tag") String tag) {
        try {
            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setTag(tag);
            product.setDescription(description);

            Product createdProduct = productService.createProduct(product, imageFile);
            return ResponseEntity.created(URI.create("/products/" + createdProduct.getId())).body(createdProduct);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("An error occurred: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/comment/{id}")
    public ResponseEntity<?> commentOnProduct(@RequestBody Comment comment, @PathVariable(value="id") Integer id){
        try{
            return ResponseEntity.ok().body(productService.addComment(comment, id));
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("An error occurred: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "id") Integer id) {
        try {
            ProductDTO deletedProductDTO = productService.deleteProduct(id);
            return ResponseEntity.ok(deletedProductDTO);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("An error occurred: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable(value = "id") Integer id,
            @RequestParam(value = "file", required = false) MultipartFile imageFile,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("tag") String tag) {
        try {
            Product updatedProduct = new Product();
            updatedProduct.setId(id);
            updatedProduct.setName(name);
            updatedProduct.setDescription(description);
            updatedProduct.setPrice(price);
            updatedProduct.setTag(tag);

            Product product = productService.updateProduct(updatedProduct, imageFile);
            return ResponseEntity.ok().body(product);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("An error occurred: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
