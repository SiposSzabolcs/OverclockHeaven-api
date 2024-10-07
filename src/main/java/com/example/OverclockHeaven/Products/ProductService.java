package com.example.OverclockHeaven.Products;

import com.example.OverclockHeaven.global.CustomExceptions;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CommentRepository commentRepository;
    private final ImageService imageService;

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public List<Product> getProductByTag(String tag){
        return productRepository.findByTag(tag);
    }

    public Product createProduct(Product product, MultipartFile imageFile) throws Exception {
        String imageUrl = imageService.uploadImage(imageFile);
        product.setImgUrl(imageUrl);

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

    public Product addComment(Comment comment, Integer productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomExceptions.ProductNotFoundException(productId));

        comment.setProductId(productId);

        commentRepository.save(comment);

        product.getComments().add(comment);
        return productRepository.save(product);
    }

    public ProductDTO deleteProduct(Integer id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.ProductNotFoundException(id));
        productRepository.delete(product);
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getTag(),
                product.getPrice(),
                product.getDescription(),
                product.getImgUrl()
        );
    }
}
