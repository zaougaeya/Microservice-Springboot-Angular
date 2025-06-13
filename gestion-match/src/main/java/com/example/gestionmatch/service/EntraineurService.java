package com.example.gestionmatch.service;

import com.example.gestionmatch.model.Entraineur;
import com.example.gestionmatch.repository.EntraineurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntraineurService {

    @Autowired
    private EntraineurRepository entraineurRepository;

    public Entraineur createEntraineur(Entraineur entraineur) {
        return entraineurRepository.save(entraineur);
    }

    // Récupérer tous les entraîneurs
    public List<Entraineur> getAllEntraineurs() {
        return entraineurRepository.findAll();
    }

    public Optional<Entraineur> getEntraineurById(String id) {
        Optional<Entraineur> entraineur = entraineurRepository.findById(id);
        entraineur.ifPresent(e -> {
            System.out.println("Equipe: " + e.getEquipe());
            if (e.getEquipe() != null) {
                System.out.println("Joueurs récupérés: " + e.getEquipe().getUsers());
            }
        });
        return entraineur;
    }


    public Entraineur updateEntraineur(String id, Entraineur entraineurDetails) {
        return entraineurRepository.findById(id)
                .map(entraineur -> {
                    entraineur.setNom(entraineurDetails.getNom());
                    entraineur.setPrenom(entraineurDetails.getPrenom());
                    entraineur.setAge(entraineurDetails.getAge());
                    entraineur.setEquipe(entraineurDetails.getEquipe());
                    return entraineurRepository.save(entraineur);
                })
                .orElseThrow(() -> new RuntimeException("Entraîneur non trouvé"));
    }

    public void deleteEntraineur(String id) {
        entraineurRepository.deleteById(id);
    }



}
