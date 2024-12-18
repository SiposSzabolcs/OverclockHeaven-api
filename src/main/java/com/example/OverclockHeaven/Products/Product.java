package com.example.OverclockHeaven.Products;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private String description;
    private String tag;
    private String imgUrl;
    private Number price;
    @ElementCollection
    private List<Rating> ratings = new ArrayList<>();

    @ElementCollection
    private List<Comment> comments = new ArrayList<>();
}
