package com.example.gestionmatch.service;

import com.example.gestionmatch.model.Joueur;
import com.example.gestionmatch.repository.JoueurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JoueurService {

    private final JoueurRepository joueurRepository;
    public JoueurService(JoueurRepository joueurRepository) {
        this.joueurRepository = joueurRepository;
    }

    // Get all joueurs
    public List<Joueur> getAllJoueurs() {
        return joueurRepository.findAll();
    }

    // Get a joueur by ID
    public Optional<Joueur> getJoueurById(String id) {
        return joueurRepository.findById(id);
    }

    // Create a new joueur
    public Joueur createJoueur(Joueur joueur) {
        return joueurRepository.save(joueur);
    }

    public Joueur updateJoueur(String id, Joueur joueurDetails) {
        // Vérifier si le joueur existe
        Joueur joueur = joueurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Joueur non trouvé"));

        // Mettre à jour les informations du joueur
        joueur.setNom(joueurDetails.getNom());
        joueur.setPrenom(joueurDetails.getPrenom());
        joueur.setEquipe(joueurDetails.getEquipe());
        joueur.setAge(joueurDetails.getAge());
        joueur.setPosition(joueurDetails.getPosition());
        joueur.setNumero(joueurDetails.getNumero());
        // Ajouter d'autres champs à mettre à jour ici

        // Sauvegarder et retourner le joueur mis à jour
        return joueurRepository.save(joueur);

    }

    public void deleteJoueur(String id) {
        // Vérifier si le joueur existe avant de le supprimer
        Joueur joueur = joueurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Joueur non trouvé"));

        joueurRepository.delete(joueur);
    }








}



