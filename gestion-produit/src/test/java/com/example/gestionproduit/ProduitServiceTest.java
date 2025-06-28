package com.example.gestionproduit;

import com.example.gestionproduit.model.Produit;
import com.example.gestionproduit.repository.ProduitRepository;
import com.example.gestionproduit.service.ProduitService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProduitServiceTest {

    private ProduitRepository produitRepository;
    private ProduitService produitService;

    @BeforeEach
    void setUp() {
        produitRepository = mock(ProduitRepository.class);
        produitService = new ProduitService(produitRepository);
    }

    @Test
    void testAjouterProduit() {
        Produit produit = new Produit();
        produit.setNom("Test produit");
        when(produitRepository.save(produit)).thenReturn(produit);

        Produit result = produitService.ajouterProduit(produit);
        assertEquals("Test produit", result.getNom());
        verify(produitRepository, times(1)).save(produit);
    }

    @Test
    void testObtenirProduitParId() {
        Produit produit = new Produit();
        produit.setId("123");
        when(produitRepository.findById("123")).thenReturn(Optional.of(produit));

        Optional<Produit> result = produitService.obtenirProduitParId("123");
        assertTrue(result.isPresent());
        assertEquals("123", result.get().getId());
    }
}
