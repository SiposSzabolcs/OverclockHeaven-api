package com.example.OverclockHeaven.Security.user;

import com.example.OverclockHeaven.Products.Product;
import com.example.OverclockHeaven.Products.ProductDTO;
import com.example.OverclockHeaven.Products.ProductRepository;
import com.example.OverclockHeaven.global.CustomExceptions;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Page<UserDTO> getAllUsers(int page, int size) {
        Page<User> usersPage = userRepository.findAll(PageRequest.of(page, size, Sort.by("firstname")));
        List<UserDTO> userDTOs = usersPage.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(userDTOs, PageRequest.of(page, size), usersPage.getTotalElements());
    }

    public UserDTO getUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.UserNotFoundException(id));
        return mapToDTO(user);
    }

    public UserDTO addProductToCart (Integer productId, Integer id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.UserNotFoundException(id));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomExceptions.ProductNotFoundException(productId));

        if (user.getCart().contains(product)){
            throw new CustomExceptions.DuplicateInCartException();
        } else {
            user.getCart().add(product);
        }
        userRepository.save(user);
        return mapToDTO(user);
    }

    public UserDTO completePurchase (Integer id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.UserNotFoundException(id));

        if (user.getCart().isEmpty()){
            throw new CustomExceptions.CartIsEmptyException();
        }

        user.getPurchaseHistory().addAll(user.getCart());

        user.getCart().clear();

        userRepository.save(user);
        return mapToDTO(user);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomExceptions.UserNotFoundExceptionString(email));

        return mapToDTO(user);
    }

    public UserDTO mapToDTO(User user) {
        return new UserDTO(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getRole(), mapToProductDTO(user.getCart()), mapToProductDTO(user.getPurchaseHistory()));
    }


    public List<ProductDTO> mapToProductDTO(List<Product> products) {
        return products.stream()
                .map(product -> new ProductDTO(product.getId(), product.getName(), product.getTag(), product.getPrice()))
                .collect(Collectors.toList());
    }

}

