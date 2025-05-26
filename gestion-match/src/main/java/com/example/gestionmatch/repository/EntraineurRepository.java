package com.example.gestionmatch.repository;

import com.example.gestionmatch.model.Entraineur;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntraineurRepository extends MongoRepository<Entraineur, String> {
}
