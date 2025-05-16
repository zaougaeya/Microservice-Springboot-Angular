package com.example.gestionmateriel.config;



import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dlnjmka2b",
                "api_key", "645672722756598",
                "api_secret", "FNIQDZdxnntyWAvS0R9T6NULOR4"
        ));
    }
}
