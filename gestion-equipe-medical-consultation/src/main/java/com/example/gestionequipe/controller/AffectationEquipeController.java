package com.example.gestionequipe.controller;
import com.example.userapi.client.UserClient;
import com.example.userapi.dto.UserResponseDTO;


import com.example.gestionequipe.service.AffectationEquipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AffectationEquipeController {
     
    private AffectationEquipeService affectationEquipeService;
     @GetMapping("saveAffectation")
     public String saveAffectation() {
         affectationEquipeService.saveAffectationEquipe();
         return "Affectation enregistrée avec succès";
     }
}


// TODO: Inject UserClient in AffectationEquipeController.java via constructor
