package com.example.OverclockHeaven.Security.user;

import com.example.OverclockHeaven.Products.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;
    private List<ProductDTO> cart;
    private List<ProductDTO> purchaseHistory;
}