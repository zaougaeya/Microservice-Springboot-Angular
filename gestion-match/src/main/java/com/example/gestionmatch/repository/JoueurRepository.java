package com.example.gestionmatch.repository;

import com.example.gestionmatch.model.Joueur;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface JoueurRepository extends MongoRepository<Joueur, String> {
    List<Joueur> findByEquipeId(String equipeId);
}
