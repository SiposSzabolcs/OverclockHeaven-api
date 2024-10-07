package com.example.OverclockHeaven.Products;

import com.example.OverclockHeaven.global.CustomExceptions;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public List<Product> getProductByTag(String tag){
        return productRepository.findByTag(tag);
    }

    public Product createProduct(Product product){
        return productRepository.save(product);
    }

    public Product rateProduct(Integer rating, Integer id){
        if (rating > 5 || rating < 0){
            throw new CustomExceptions.RatingException();
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.ProductNotFoundException(id));

        product.getRatings().add(rating);
        productRepository.save(product);
        return product;
    }

    public ProductDTO deleteProduct(Integer id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.ProductNotFoundException(id));
        productRepository.delete(product);
        return new ProductDTO(
                product.getName(),
                product.getTag(),
                product.getPrice()
        );
    }
}
