package com.example.gestionmatch.Repository;

import com.example.userservice.model.Equipe;
import com.example.userservice.model.Joueur;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipeRepository extends MongoRepository<Equipe, String> {
}
