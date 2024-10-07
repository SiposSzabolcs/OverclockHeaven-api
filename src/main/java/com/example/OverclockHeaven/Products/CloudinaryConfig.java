package com.example.OverclockHeaven.Products;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "deea2iows",
                "api_key", "353872364595939",
                "api_secret", "whxYV-lKWN7Li9I8I13gCdfmbag"
        ));
    }
}

