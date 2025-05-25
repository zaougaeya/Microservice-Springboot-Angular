package com.example.userservice.repository;

import com.example.userservice.model.AffectationEquipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AffectationEquipeRepository extends MongoRepository<AffectationEquipe, String> {
}
