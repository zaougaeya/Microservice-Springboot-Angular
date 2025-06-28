package com.example.gestionproduit.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.gestionproduit.model.Panier;

import java.util.Optional;

@Repository
public interface PanierRepository extends MongoRepository<Panier, String> {
    Optional<Panier> findByUserId(String userId);
}
