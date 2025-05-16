package com.example.gestionproduit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.gestionproduit.model.Voiture;

import java.util.List;

public interface VoitureRepository extends MongoRepository<Voiture, String> {
    List<Voiture> findByStatutVoiture(String statutVoiture);
}
