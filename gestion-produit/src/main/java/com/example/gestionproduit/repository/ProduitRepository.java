package com.example.gestionproduit.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.gestionproduit.model.Produit;

import java.sql.Date;
import java.util.List;

@Repository
public interface ProduitRepository extends MongoRepository<Produit, String> {
   
    List<Produit> findByCategorieId(String id);

  
    List<Produit> findByDisponibleTrue();

    List<Produit> findByNoteGreaterThan(double note);

}