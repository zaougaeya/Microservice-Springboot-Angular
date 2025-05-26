package com.example.gestionmatch.controller;

import com.example.gestionmatch.model.Joueur;
import com.example.gestionmatch.service.JoueurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/joueurs")
@CrossOrigin(origins = "http://localhost:4200")
public class JoueurController {

   private final JoueurService joueurService;
   public JoueurController(JoueurService joueurService) {
    this.joueurService = joueurService;
}
    // Get all users
    @GetMapping
    public List<Joueur> getAllJoueurs() {
        return joueurService.getAllJoueurs();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Joueur> getJoueurById(@PathVariable String id) {
        return joueurService.getJoueurById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // Create a new user
    @PostMapping
    public Joueur createJoueur(@RequestBody Joueur joueur) {
        return joueurService.createJoueur(joueur);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Joueur> updateJoueur(@PathVariable String id, @RequestBody Joueur joueurDetails) {
        try {
            Joueur updatedJoueur = joueurService.updateJoueur(id, joueurDetails);
            return ResponseEntity.ok(updatedJoueur);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJoueur(@PathVariable String id) {
        try {
            joueurService.deleteJoueur(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
