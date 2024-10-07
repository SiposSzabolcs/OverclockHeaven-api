package com.example.OverclockHeaven.Products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDTO {
    private Integer Id;
    private String name;
    private String tag;
    private Number price;
    private String description;
    private String imgUrl;
}
