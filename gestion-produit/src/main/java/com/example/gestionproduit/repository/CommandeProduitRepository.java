package com.example.gestionproduit.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.gestionproduit.model.Commande;
import com.example.gestionproduit.model.CommandeProduit;

import java.util.List;

@Repository
public interface CommandeProduitRepository extends MongoRepository<CommandeProduit, String> {
    
   
    List<CommandeProduit> findByCommande(Commande commande);
}
