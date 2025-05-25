package com.example.gestionmatch.Repository;

import com.example.userservice.model.Terrain;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TerrainRepository extends MongoRepository<Terrain, String> {
}
