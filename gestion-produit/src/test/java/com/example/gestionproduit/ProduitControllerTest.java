package com.example.gestionproduit;

import com.example.gestionproduit.controller.ProduitController;
import com.example.gestionproduit.model.Produit;
import com.example.gestionproduit.service.ProduitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProduitController.class)
@WithMockUser
class ProduitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProduitService produitService;

    @Test
    void testObtenirTousLesProduits() throws Exception {
        Produit produit = new Produit();
        produit.setNom("Produit 1");

        when(produitService.obtenirTousLesProduits()).thenReturn(Collections.singletonList(produit));

        mockMvc.perform(get("/api/produits")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Produit 1"));
    }
}
