package com.example.userservice.service;

import com.example.userservice.model.AffectationEquipe;
import com.example.userservice.repository.AffectationEquipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AffectationEquipeService {
@Autowired
    private AffectationEquipeRepository affectationEquipeRepository;
    public void saveAffectationEquipe() {
        AffectationEquipe affectation = new AffectationEquipe();
        affectation.setIdEquipeMedicale("1"); // Example, replace with actual logic
        affectation.setIdMatch("Match123");
        affectation.setIdEvenement("Event123");
        affectation.setDateAffectation(new Date());
        affectationEquipeRepository.save(affectation); // Save the affectation object
    }

}
