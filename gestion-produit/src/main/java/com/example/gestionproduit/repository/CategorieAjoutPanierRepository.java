package com.example.gestionproduit.repository;

import com.example.gestionproduit.model.CategorieAjoutPanier;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
public interface CategorieAjoutPanierRepository extends MongoRepository<CategorieAjoutPanier, String> {
    Optional<CategorieAjoutPanier> findByUserIdAndNomCategorie(String userId, String nomCategorie);
    List<CategorieAjoutPanier> findByUserIdOrderByNombreAjoutsDesc(String userId);
}