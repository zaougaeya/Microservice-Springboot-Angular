package com.example.gestionproduit.service;

import com.example.gestionproduit.model.CategorieAjoutPanier;
import com.example.gestionproduit.repository.CategorieAjoutPanierRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategorieStatsService {

    private final CategorieAjoutPanierRepository repository;

    public CategorieStatsService(CategorieAjoutPanierRepository repository) {
        this.repository = repository;
    }

    public void enregistrerAjout(String userId, String nomCategorie) {
        Optional<CategorieAjoutPanier> existant =
                repository.findByUserIdAndNomCategorie(userId, nomCategorie);

        if (existant.isPresent()) {
            CategorieAjoutPanier stat = existant.get();
            stat.setNombreAjouts(stat.getNombreAjouts() + 1);
            repository.save(stat);
        } else {
            CategorieAjoutPanier nouveau = new CategorieAjoutPanier();
            nouveau.setUserId(userId);
            nouveau.setNomCategorie(nomCategorie);
            nouveau.setNombreAjouts(1);
            repository.save(nouveau);
        }
    }

    public List<CategorieAjoutPanier> getTopCategoriesForUser(String userId) {
    return repository.findByUserIdOrderByNombreAjoutsDesc(userId);
}

}
