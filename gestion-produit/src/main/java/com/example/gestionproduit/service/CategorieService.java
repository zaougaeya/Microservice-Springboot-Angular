package com.example.gestionproduit.service;


import org.springframework.stereotype.Service;

import com.example.gestionproduit.model.Categorie;
import com.example.gestionproduit.repository.CategorieRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategorieService {
    private final CategorieRepository categorieRepository;

    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

  
    public Categorie ajouterCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

 
    public List<Categorie> obtenirToutesLesCategories() {
        return categorieRepository.findAll();
    }

  
    public Optional<Categorie> obtenirCategorieParId(String id) {
        return categorieRepository.findById(id);
    }

 
    public Categorie modifierCategorie(String id, Categorie categorieModifiee) {
        return categorieRepository.findById(id).map(categorie -> {
            categorie.setNom(categorieModifiee.getNom());
            categorie.setDescription(categorieModifiee.getDescription());
            return categorieRepository.save(categorie);
        }).orElse(null);
    }

  
    public void supprimerCategorie(String id) {
        categorieRepository.deleteById(id);
    }
}
