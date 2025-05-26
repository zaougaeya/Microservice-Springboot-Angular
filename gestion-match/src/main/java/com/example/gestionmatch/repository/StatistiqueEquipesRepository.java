package com.example.gestionmatch.repository;

import com.example.gestionmatch.model.StatistiqueEquipes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatistiqueEquipesRepository extends MongoRepository<StatistiqueEquipes, String> {

    StatistiqueEquipes findByMatchIdAndEquipeId(String Match_id, String equipeId);
}
