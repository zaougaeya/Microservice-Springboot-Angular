package com.example.gestionproduit.controller;



import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.gestionproduit.model.Livreur;
import com.example.gestionproduit.service.LivreurService;

import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/api/livreurs")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class LivreurController {

    private final LivreurService livreurService;

    @PostMapping
    public Livreur createLivreur(@RequestBody Livreur livreur) {
        return livreurService.createLivreur(livreur);
    }

    @GetMapping
    public List<Livreur> getAllLivreurs() {
        return livreurService.getAllLivreurs();
    }

    @GetMapping("/{id}")
    public Livreur getLivreurById(@PathVariable String id) {
        return livreurService.getLivreurById(id);
    }

    @PutMapping("/{id}")
    public Livreur updateLivreur(@PathVariable String id, @RequestBody Livreur livreur) {
        return livreurService.updateLivreur(id, livreur);
    }
    @DeleteMapping("/{id}")
    public void deleteLivreur(@PathVariable String id) {
        livreurService.deleteLivreur(id);
    }

    @GetMapping("/disponibles")
    public List<Livreur> getLivreursDisponibles() {
        return livreurService.getLivreursDisponibles();
    }

    @PostMapping("/{idLivreur}/assigner-commandes")
    public Livreur assignerCommandes(
            @PathVariable String idLivreur,
            @RequestParam List<String> idCommandes) {
        return livreurService.assignerCommandesAuLivreur(idLivreur, idCommandes);
    }
}


