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
import java.util.Optional;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CommentRepository commentRepository;
    private final RatingRepository ratingRepository;
    private final ImageService imageService;
    private final SoldProductsRepository soldProductsRepository;

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

    public Product rateProduct(Rating rating, Integer id){
        if (rating.getRating() > 5 || rating.getRating() < 0){
            throw new CustomExceptions.RatingException();
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.ProductNotFoundException(id));

        boolean hasRated = product.getRatings().stream()
                .anyMatch(existingRating -> existingRating.getUserId().equals(rating.getUserId()));

        if (hasRated) {
            throw new CustomExceptions.RatingAlreadyExistsException();
        }

        ratingRepository.save(rating);

        product.getRatings().add(rating);
        productRepository.save(product);
        return product;
    }

    public Product addComment(Comment comment, Integer productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomExceptions.ProductNotFoundException(productId));

        if (comment.getRating() > 5 || comment.getRating() < 0){
            throw new CustomExceptions.RatingException();
        }

        boolean hasRated = product.getComments().stream()
                .anyMatch(existingComment -> existingComment.getUserId().equals(comment.getUserId()));

        if (hasRated) {
            throw new CustomExceptions.RatingAlreadyExistsException();
        }

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

    public List<SoldProduct> getAllSoldProducts(){
        return soldProductsRepository.findAll();
    }

    public SoldProduct addSoldProduct(Product product) {
        Optional<SoldProduct> optionalSoldProduct = soldProductsRepository.findByProductId(product.getId());

        SoldProduct soldProduct;

        if (optionalSoldProduct.isPresent()) {
            soldProduct = optionalSoldProduct.get();
            soldProduct.setAmount(soldProduct.getAmount() + 1);
        } else {
            soldProduct = new SoldProduct();
            soldProduct.setProduct(product);
            soldProduct.setAmount(1);
        }

        return soldProductsRepository.save(soldProduct);
    }

    public Product updateProduct(Product updatedProduct, MultipartFile imageFile) throws Exception {
        Product existingProduct = productRepository.findById(updatedProduct.getId())
                .orElseThrow(() -> new CustomExceptions.ProductNotFoundException(updatedProduct.getId()));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setTag(updatedProduct.getTag());

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = imageService.uploadImage(imageFile);
            existingProduct.setImgUrl(imageUrl);
        }

        return productRepository.save(existingProduct);
    }
}
