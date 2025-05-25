package com.example.gestionequipe.repository;

import com.example.gestionequipe.model.AffectationEquipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AffectationEquipeRepository extends MongoRepository<AffectationEquipe, String> {
}
