package com.example.gestionmatch.service;

import com.example.gestionmatch.model.Match;
import com.example.gestionmatch.model.Terrain;
import com.example.gestionmatch.repository.MatchRepository;
import com.example.gestionmatch.repository.TerrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TerrainService {
    private final TerrainRepository terrainRepository;
    private final MatchRepository matchRepository;

    public TerrainService(TerrainRepository terrainRepository, MatchRepository matchRepository) {
        this.terrainRepository = terrainRepository;
        this.matchRepository = matchRepository;
    }


    // Créer un terrain
    public Terrain createTerrain(Terrain terrain) {
        return terrainRepository.save(terrain);
    }

    public List<Terrain> getAllTerrains() {
        return terrainRepository.findAll();
    }


    public Terrain getTerrainById(String id) {
        return terrainRepository.findById(id)
                .orElse(null); // retourne null si non trouvée (tu peux aussi lancer une exception si tu préfères)
    }


    public Optional<Terrain> updateTerrain(String id, Terrain terrainDetails) {
        return terrainRepository.findById(id)
                .map(terrain -> {
                    terrain.setName(terrainDetails.getName());
                    terrain.setAdresse(terrainDetails.getAdresse());
                    terrain.setType(terrainDetails.getType());
                    return terrainRepository.save(terrain);
                });
    }


    public boolean deleteTerrain(String id) {
        if (!terrainRepository.existsById(id)) {
            return false;
        }

        // 1) Supprimer tous les matchs où l'équipe est présente en tant qu'équipe1
        List<Match> matches1 = matchRepository.findByIdTerrain(id);
        for (Match m : matches1) {
            matchRepository.delete(m);
        }

        // 3) Supprimer l'équipe
        terrainRepository.deleteById(id);
        return true;
    }


}

