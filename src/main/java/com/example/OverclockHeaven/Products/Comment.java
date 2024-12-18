package com.example.OverclockHeaven.Products;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Comment {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer rating;
    private String name;
    private String content;
    private Integer userId;
    private Integer productId;
}
