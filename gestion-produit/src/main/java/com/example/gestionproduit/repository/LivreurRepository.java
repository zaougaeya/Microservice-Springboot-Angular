package com.example.gestionproduit.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.gestionproduit.model.Livreur;
import com.example.gestionproduit.model.StatutLivreur;

import java.util.List;
import java.util.Optional;

public interface LivreurRepository extends MongoRepository<Livreur, String> {
    List<Livreur> findByStatutLivreur(StatutLivreur statutLivreur);
    Optional<Livreur> findById(String id);


}
