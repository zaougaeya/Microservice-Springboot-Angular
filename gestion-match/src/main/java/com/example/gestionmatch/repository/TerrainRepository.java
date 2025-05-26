package com.example.gestionmatch.repository;

import com.example.gestionmatch.model.Terrain;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TerrainRepository extends MongoRepository<Terrain, String> {
}
