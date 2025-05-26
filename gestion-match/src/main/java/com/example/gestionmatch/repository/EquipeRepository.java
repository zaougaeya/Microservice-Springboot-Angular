package com.example.gestionmatch.repository;

import com.example.gestionmatch.model.Equipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipeRepository extends MongoRepository<Equipe, String> {
}
