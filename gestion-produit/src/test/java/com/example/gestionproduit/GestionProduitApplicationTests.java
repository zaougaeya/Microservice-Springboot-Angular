package com.example.gestionproduit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ImportAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
class GestionProduitApplicationTests {

    @Test
    void contextLoads() {
    }

}
