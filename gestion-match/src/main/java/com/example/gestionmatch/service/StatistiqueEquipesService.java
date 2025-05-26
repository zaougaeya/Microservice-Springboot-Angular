package com.example.gestionmatch.service;

import com.example.gestionmatch.model.Equipe;
import com.example.gestionmatch.model.StatistiqueEquipes;
import com.example.gestionmatch.repository.EquipeRepository;
import com.example.gestionmatch.repository.StatistiqueEquipesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StatistiqueEquipesService {

    @Autowired
    private StatistiqueEquipesRepository statistiquesEquipeRepository;
    @Autowired
    private EquipeRepository equipeRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public StatistiqueEquipes saveStatistiquesEquipe(StatistiqueEquipes statistiquesEquipe) {
        return statistiquesEquipeRepository.save(statistiquesEquipe);
    }

    public List<StatistiqueEquipes> getAllStatistiquesEquipes() {
        return statistiquesEquipeRepository.findAll();
    }

    public Optional<StatistiqueEquipes> getStatistiquesEquipeById(String id) {
        return statistiquesEquipeRepository.findById(id);
    }

    public void deleteStatistiquesEquipe(String id) {
        statistiquesEquipeRepository.deleteById(id);
    }

    public StatistiqueEquipes ajouterStatistiques(String equipeId, StatistiqueEquipes stats) {
        // Rechercher l'équipe avec l'ID donné
        Equipe equipe = equipeRepository.findById(equipeId)
                .orElseThrow(() -> new RuntimeException("Équipe non trouvée !"));

        // Associer l'équipe à l'objet StatistiquesEquipe
        stats.setEquipe(equipe);  // 🔹 Associer l'équipe avant de sauvegarder
        stats.setEquipeId(equipeId);
        // Sauvegarder les statistiques dans la base de données
        return statistiquesEquipeRepository.save(stats);
    }

    // Méthode pour calculer dynamiquement les statistiques d'une équipe
    public StatistiqueEquipes calculerStatistiquesDynamique(String equipeId, LocalDate startDate, LocalDate endDate) {
        // Utiliser une agrégation MongoDB pour filtrer par équipe et dates
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("equipeId").is(equipeId)
                        .and("matchDate").gte(startDate).lte(endDate)),  // Assure-toi que "matchDate" est bien une date
                Aggregation.group("equipeId")
                        .sum("cartonsJaunes").as("totalCartonsJaunes")
                        .sum("cartonsRouges").as("totalCartonsRouges")
                        .sum("but").as("totalButs")
                        .sum("fautes").as("totalFautes")
        );

        // Exécution de l'agrégation
        AggregationResults<StatistiqueEquipes> result = mongoTemplate.aggregate(aggregation, "statistiques_equipe", StatistiqueEquipes.class);

        // Retourner les résultats sous forme d'objet StatistiqueEquipe
        return result.getUniqueMappedResult();
    }

}
