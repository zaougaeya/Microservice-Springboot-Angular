package com.example.gestionproduit;

import com.example.gestionproduit.controller.ProduitController;
import com.example.gestionproduit.service.ProduitService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(
    controllers = ProduitController.class,
    excludeAutoConfiguration = SecurityAutoConfiguration.class
)
public class ProduitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProduitService produitService;

    @Test
    public void testObtenirTousLesProduits() throws Exception {
        // Mock la réponse du service
        when(produitService.obtenirTousLesProduits()).thenReturn(List.of());

        mockMvc.perform(get("/api/produits"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray());
    }
}
