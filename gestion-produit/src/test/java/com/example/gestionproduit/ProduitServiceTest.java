package com.example.gestionproduit;

import com.example.gestionproduit.model.Produit;
import com.example.gestionproduit.model.Categorie; // Importe la classe Categorie depuis votre modèle
import com.example.gestionproduit.model.StatutProduit; // Importe l'énumération StatutProduit depuis votre modèle
import com.example.gestionproduit.model.GenreProduit; // Importe l'énumération GenreProduit depuis votre modèle
import com.example.gestionproduit.repository.ProduitRepository;
import com.example.gestionproduit.service.ProduitService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith; // Pour l'intégration Mockito JUnit 5
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension; // Pour initialiser les mocks automatiquement

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Date; // Pour la dateAjout

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


// Permet à Mockito d'initialiser les mocks annotés avec @Mock et d'injecter dans @InjectMocks
@ExtendWith(MockitoExtension.class)
class ProduitServiceTest {

    // Crée un mock de ProduitRepository
    @Mock
    private ProduitRepository produitRepository;

    // Injecte les mocks créés ci-dessus (produitRepository) dans une instance de ProduitService
    @InjectMocks
    private ProduitService produitService;

    // @BeforeEach n'est plus strictement nécessaire pour l'initialisation des mocks
    // grâce à @ExtendWith(MockitoExtension.class) et @Mock/@InjectMocks.
    // Vous pouvez le garder si vous avez d'autres initialisations communes.
    // @BeforeEach
    // void setUp() {
    //    // Pas besoin de 'produitRepository = mock(ProduitRepository.class);'
    //    // 'produitService = new ProduitService(produitRepository);'
    // }

    @Test
    void testAjouterProduit() {
        Produit produit = new Produit();
        produit.setNom("Smartphone");
        produit.setDescription("Un nouveau smartphone performant");
        produit.setPrix(799.99);
        produit.setQuantiteEnStock(50);
        produit.setDisponible(true);
        produit.setDateAjout(new Date());
        produit.setNote(4.8);
        produit.setPourcentagePromotion(0.0);
        
        // La catégorie doit être une instance de la classe Categorie.
        // Si Categorie a un constructeur par défaut ou peut être instanciée facilement:
        produit.setCategorie(new Categorie()); // Adapter si Categorie a un constructeur spécifique
        
        produit.setGenreProduit(GenreProduit.FEMME);
        produit.setStatutProduit(StatutProduit.EN_STOCK);

        // Quand save est appelé avec n'importe quel objet Produit, retourner le produit en entrée
        when(produitRepository.save(any(Produit.class))).thenReturn(produit);

        Produit result = produitService.ajouterProduit(produit);

        assertNotNull(result);
        assertEquals("Smartphone", result.getNom());
        assertEquals(799.99, result.getPrix());
        assertEquals(50, result.getQuantiteEnStock());
        // Vérifie que la méthode save du repository a été appelée exactement une fois avec le produit
        verify(produitRepository, times(1)).save(produit);
    }

    @Test
    void testObtenirTousLesProduits() {
        // Crée des produits de mock pour la liste
        Produit p1 = new Produit(); p1.setId("1"); p1.setNom("Produit A");
        Produit p2 = new Produit(); p2.setId("2"); p2.setNom("Produit B");
        List<Produit> produits = Arrays.asList(p1, p2);

        // Quand findAll est appelé, retourner la liste de produits mockés
        when(produitRepository.findAll()).thenReturn(produits);

        List<Produit> result = produitService.obtenirTousLesProduits();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals("Produit A", result.get(0).getNom());
        assertEquals("Produit B", result.get(1).getNom());
        // Vérifie que findAll a été appelé une fois
        verify(produitRepository, times(1)).findAll();
    }

    @Test
    void testObtenirProduitParId_ProduitExiste() {
        Produit produit = new Produit();
        produit.setId("prod123");
        produit.setNom("Test Produit ID");

        // Quand findById est appelé avec "prod123", retourner un Optional contenant le produit
        when(produitRepository.findById("prod123")).thenReturn(Optional.of(produit));

        Optional<Produit> result = produitService.obtenirProduitParId("prod123");

        assertTrue(result.isPresent());
        assertEquals("prod123", result.get().getId());
        assertEquals("Test Produit ID", result.get().getNom());
        verify(produitRepository, times(1)).findById("prod123");
    }

    @Test
    void testObtenirProduitParId_ProduitNonTrouve() {
        // Quand findById est appelé avec un ID inexistant, retourner un Optional vide
        when(produitRepository.findById("nonExistentId")).thenReturn(Optional.empty());

        Optional<Produit> result = produitService.obtenirProduitParId("nonExistentId");

        assertFalse(result.isPresent());
        verify(produitRepository, times(1)).findById("nonExistentId");
    }

    @Test
    void testMettreAJourProduit_Succes() {
        String productId = "prodUpdate";
        Produit existingProduit = new Produit();
        existingProduit.setId(productId);
        existingProduit.setNom("Ancien Nom");
        existingProduit.setPrix(100.0);
        // Initialiser Categorie pour le produit existant
        existingProduit.setCategorie(new Categorie());

        Produit updatedDetails = new Produit();
        updatedDetails.setNom("Nouveau Nom");
        updatedDetails.setPrix(150.0);
        updatedDetails.setDescription("Desc updated");
        updatedDetails.setQuantiteEnStock(20);
        updatedDetails.setDisponible(true);
        updatedDetails.setImageUrl("new_image.jpg");
        updatedDetails.setDateAjout(new Date());
        updatedDetails.setNote(4.0);
        updatedDetails.setPourcentagePromotion(10.0);
        // Initialiser Categorie pour les détails mis à jour
        updatedDetails.setCategorie(new Categorie()); // Ou une instance spécifique si besoin
        updatedDetails.setGenreProduit(GenreProduit.HOMME);
        updatedDetails.setStatutProduit(StatutProduit.EN_STOCK);

        // Simule le scénario où le produit existe
        when(produitRepository.findById(productId)).thenReturn(Optional.of(existingProduit));
        // Simule le comportement de sauvegarde du repository après mise à jour
        when(produitRepository.save(any(Produit.class))).thenReturn(existingProduit); // Retourne l'objet mis à jour

        Produit result = produitService.mettreAJourProduit(productId, updatedDetails);

        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Nouveau Nom", result.getNom());
        assertEquals(150.0, result.getPrix());
        assertEquals("Desc updated", result.getDescription());
        assertEquals(20, result.getQuantiteEnStock());
        assertTrue(result.isDisponible());
        assertEquals("new_image.jpg", result.getImageUrl());
        assertNotNull(result.getDateAjout()); // Puisque nous l'avons défini
        assertEquals(4.0, result.getNote());
        assertEquals(10.0, result.getPourcentagePromotion());
        // Vérifiez l'objet Categorie lui-même si pertinent, ou juste qu'il n'est pas null
        assertNotNull(result.getCategorie());
        assertEquals(GenreProduit.HOMME, result.getGenreProduit()); // Corrige l'assertion
        assertEquals(StatutProduit.EN_STOCK, result.getStatutProduit());

        verify(produitRepository, times(1)).findById(productId);
        verify(produitRepository, times(1)).save(existingProduit);
    }

    @Test
    void testMettreAJourProduit_ProduitNonTrouve() {
        String productId = "nonExistentIdForUpdate";
        Produit updatedDetails = new Produit();
        updatedDetails.setNom("Nouveau Nom");

        // Simule le scénario où le produit n'existe pas
        when(produitRepository.findById(productId)).thenReturn(Optional.empty());

        // Vérifie qu'une RuntimeException est lancée
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                produitService.mettreAJourProduit(productId, updatedDetails)
        );
        assertEquals("Produit non trouvé avec l'ID : " + productId, exception.getMessage());
        verify(produitRepository, times(1)).findById(productId);
        verify(produitRepository, never()).save(any(Produit.class)); // save ne doit pas être appelé
    }

    @Test
    void testSupprimerProduit() {
        String productId = "prodToDelete";
        
        // Simule que la méthode deleteById ne fait rien, car elle est void.
        doNothing().when(produitRepository).deleteById(productId);

        produitService.supprimerProduit(productId);

        // Vérifie que deleteById a été appelé une fois
        verify(produitRepository, times(1)).deleteById(productId);
    }

    @Test
    void testObtenirProduitsParCategorie() {
        String categoryId = "cat1";
        Produit p1 = new Produit(); p1.setId("p1"); p1.setNom("Prod Cat 1 A");
        Produit p2 = new Produit(); p2.setId("p2"); p2.setNom("Prod Cat 1 B");
        List<Produit> produitsParCategorie = Arrays.asList(p1, p2);

        when(produitRepository.findByCategorieId(categoryId)).thenReturn(produitsParCategorie);

        List<Produit> result = produitService.obtenirProduitsParCategorie(categoryId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals("Prod Cat 1 A", result.get(0).getNom());
        verify(produitRepository, times(1)).findByCategorieId(categoryId);
    }

    @Test
    void testObtenirProduitsDisponibles() {
        Produit p1 = new Produit(); p1.setId("pA"); p1.setNom("Prod Disponible A"); p1.setDisponible(true);
        Produit p2 = new Produit(); p1.setId("pB"); p2.setNom("Prod Disponible B"); p2.setDisponible(true); // Correction ici: p2.setId("pB");
        List<Produit> produitsDisponibles = Arrays.asList(p1, p2);

        when(produitRepository.findByDisponibleTrue()).thenReturn(produitsDisponibles);

        List<Produit> result = produitService.obtenirProduitsDisponibles();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals("Prod Disponible A", result.get(0).getNom());
        verify(produitRepository, times(1)).findByDisponibleTrue();
    }

    @Test
    void testObtenirProduitsAvecNoteSuperieure() {
        double minNote = 4.0;
        Produit p1 = new Produit(); p1.setId("pN1"); p1.setNom("Prod Note 1"); p1.setNote(4.5);
        Produit p2 = new Produit(); p2.setId("pN2"); p2.setNom("Prod Note 2"); p2.setNote(4.1);
        List<Produit> produitsHauteNote = Arrays.asList(p1, p2);

        when(produitRepository.findByNoteGreaterThan(minNote)).thenReturn(produitsHauteNote);

        List<Produit> result = produitService.obtenirProduitsAvecNoteSuperieure(minNote);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals(4.5, result.get(0).getNote());
        verify(produitRepository, times(1)).findByNoteGreaterThan(minNote);
    }
}
