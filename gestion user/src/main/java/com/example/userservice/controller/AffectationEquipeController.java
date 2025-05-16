package com.example.userservice.controller;

import com.example.userservice.service.AffectationEquipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AffectationEquipeController {
     @Autowired
    private AffectationEquipeService affectationEquipeService;
     @GetMapping("saveAffectation")
     public String saveAffectation() {
         affectationEquipeService.saveAffectationEquipe();
         return "Affectation enregistrée avec succès";
     }
}
