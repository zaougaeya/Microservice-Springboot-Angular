package com.example.gestionproduit.service;

import org.springframework.stereotype.Service;

import com.example.gestionproduit.model.Produit;
import com.example.gestionproduit.repository.ProduitRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitService {

    private final ProduitRepository produitRepository;

    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

  
    public Produit ajouterProduit(Produit produit) {
        return produitRepository.save(produit);
    }


    public List<Produit> obtenirTousLesProduits() {
        return produitRepository.findAll();
    }


    public Optional<Produit> obtenirProduitParId(String id) {
        return produitRepository.findById(id);
    }


    public Produit mettreAJourProduit(String id, Produit produitDetails) {
        return produitRepository.findById(id).map(produit -> {
            produit.setNom(produitDetails.getNom());
            produit.setDescription(produitDetails.getDescription());
            produit.setPrix(produitDetails.getPrix());
            produit.setQuantiteEnStock(produitDetails.getQuantiteEnStock());
            produit.setDisponible(produitDetails.isDisponible());
            produit.setImageUrl(produitDetails.getImageUrl());
            produit.setDateAjout(produitDetails.getDateAjout());
            produit.setNote(produitDetails.getNote());
            produit.setPourcentagePromotion(produitDetails.getPourcentagePromotion());
            produit.setCategorie(produitDetails.getCategorie());
            produit.setGenreProduit(produitDetails.getGenreProduit());
            produit.setStatutProduit(produitDetails.getStatutProduit());
            return produitRepository.save(produit);
        }).orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID : " + id));
    }

 
    public void supprimerProduit(String id) {
        produitRepository.deleteById(id);
    }
    public List<Produit> obtenirProduitsParCategorie(String idCategorie) {
        return produitRepository.findByCategorieId(idCategorie);
    }
    
    public List<Produit> obtenirProduitsDisponibles() {
        return produitRepository.findByDisponibleTrue();
    }
    
    public List<Produit> obtenirProduitsAvecNoteSuperieure(double note) {
        return produitRepository.findByNoteGreaterThan(note);
    }
}
