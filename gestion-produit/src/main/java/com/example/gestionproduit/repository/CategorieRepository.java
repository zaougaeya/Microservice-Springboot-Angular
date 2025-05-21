package com.example.gestionproduit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.gestionproduit.model.Categorie;

@Repository
public interface CategorieRepository extends MongoRepository<Categorie, String> {
    Categorie findByNom(String nom); 
}
