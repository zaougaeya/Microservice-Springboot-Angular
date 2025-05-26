package com.example.gestionmatch.controller;

import com.example.gestionmatch.model.Entraineur;
import com.example.gestionmatch.service.EntraineurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/entraineurs")

public class EntraineurController {

    private final EntraineurService entraineurService;
    public EntraineurController(EntraineurService entraineurService) {
        this.entraineurService = entraineurService;
    }

    @PostMapping
    public Entraineur createEntraineur(@RequestBody Entraineur entraineur) {
        return entraineurService.createEntraineur(entraineur);
    }

    @GetMapping
    public List<Entraineur> getAllEntraineurs() {
        return entraineurService.getAllEntraineurs();
    }
    @GetMapping("/{id}")
    public Optional<Entraineur> getEntraineurById(@PathVariable String id) {
        return entraineurService.getEntraineurById(id);
    }
    @PutMapping("/{id}")
    public Entraineur updateEntraineur(@PathVariable String id, @RequestBody Entraineur entraineurDetails) {
        return entraineurService.updateEntraineur(id, entraineurDetails);
    }
    @DeleteMapping("/{id}")
    public void deleteEntraineur(@PathVariable String id) {
        entraineurService.deleteEntraineur(id);
    }


    @GetMapping("/{id}/equipe")
    public String getEquipeOfEntraineur(@PathVariable String id) {
        Optional<Entraineur> entraineur = entraineurService.getEntraineurById(id);
        return entraineur.map(en -> "L'entraîneur " + en.getNom() + " " + en.getPrenom() + " appartient à l'équipe " + en.getEquipe().getNameEquipe())
                .orElse("Entraîneur non trouvé");
    }
}
