package com.example.gestionmatch.controller;

import com.example.gestionmatch.model.Equipe;
import com.example.gestionmatch.model.User;
import com.example.gestionmatch.repository.EquipeRepository;
import com.example.gestionmatch.repository.UserRepository;
import com.example.gestionmatch.service.EquipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/equipes")
@CrossOrigin(origins = "http://localhost:4200")

public class EquipeController {

    private final EquipeService equipeService;
    private final UserRepository userRepository;
    private final EquipeRepository equipeRepository;

    // Injection des dépendances via le constructeur
    public EquipeController(EquipeService equipeService, UserRepository userRepository, EquipeRepository equipeRepository) {
        this.equipeService = equipeService;
        this.userRepository = userRepository;
        this.equipeRepository = equipeRepository;
    }

    @GetMapping
    public List<Equipe> getAllEquipes() {
        return equipeService.getAllEquipes();
    }

    // Récupérer une équipe par ID
    @GetMapping("/{id}")
    public ResponseEntity<Equipe> getEquipeById(@PathVariable String id) {
        Equipe result = equipeService.getEquipeById(id);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Equipe> createEquipe(@RequestParam("nameEquipe") String nameEquipe,
                                               @RequestParam("logo") MultipartFile logoFile) {
        try {
            Equipe created = equipeService.createEquipe(nameEquipe, logoFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IOException e) {
            // on peut logger e.getMessage()
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Equipe> updateEquipe(@PathVariable String id, @RequestBody Equipe equipeDetails) {
        Optional<Equipe> updatedEquipe = equipeService.updateEquipe(id, equipeDetails);

        return updatedEquipe
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Supprimer une équipe par
    // ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipe(@PathVariable String id) {
        boolean deleted = equipeService.deleteEquipe(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {

            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{equipeId}/users")
    public List<User> getUserByEquipe(@PathVariable String equipeId) {
        return userRepository.findByEquipeId(equipeId);
    }

    @GetMapping("/{equipeId}/UsersAndEquipe")
    public Equipe getUsersAndEquipe(@PathVariable String equipeId) {
        return equipeService.getUsersAndEquipe(equipeId);
    }

    @PostMapping("/api/equipes/{id}/ajouter-joueur")
    public ResponseEntity<?> ajouterJoueurDansEquipe(
            @PathVariable String id,
            @RequestBody User user) {

        Optional<Equipe> equipeOpt = equipeRepository.findById(id);
        if (equipeOpt.isPresent()) {
            Equipe equipe = equipeOpt.get();
            equipe.getUsers().add(user);
            equipeRepository.save(equipe);
            return ResponseEntity.ok("Joueur ajouté avec succès");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Équipe non trouvée");
        }
    }

}
