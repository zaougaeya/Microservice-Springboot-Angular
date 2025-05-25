package com.example.gestionmatch.Controller;

import com.example.userservice.model.SessionDeJeu;
import com.example.userservice.repository.SessionDeJeuRepository;
import com.example.userservice.service.SessionDeJeuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "http://localhost:4200")
public class SessionDeJeuController {
    @Autowired
    private SessionDeJeuService sessionService;
    @Autowired
    private SessionDeJeuRepository sessionDeJeuRepository;

    @GetMapping
    public List<SessionDeJeu> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @GetMapping("/{id}")
    public Optional<SessionDeJeu> getSession(@PathVariable String id) {
        return sessionService.getSessionById(id);
    }

    @PostMapping
    public SessionDeJeu createSession(@RequestBody SessionDeJeu session) {
        return sessionService.createSession(session);
    }

    @PutMapping("/{id}")
    public SessionDeJeu updateSession(@PathVariable String id, @RequestBody SessionDeJeu session) {
        return sessionService.updateSession(id, session);
    }

    @DeleteMapping("/{id}")
    public void deleteSession(@PathVariable String id) {
        sessionService.deleteSession(id);
    }

    @GetMapping("/type/{type}")
    public List<SessionDeJeu> getByTypeMatch(@PathVariable String type) {
        return sessionService.findByTypeMatch(type);
    }

    @GetMapping("/statut/{statut}")
    public List<SessionDeJeu> getByStatut(@PathVariable String statut) {
        return sessionService.findByStatut(statut);
    }

    @GetMapping("/terrain/{terrainId}")
    public List<SessionDeJeu> getByTerrainId(@PathVariable String terrainId) {
        return sessionService.findByTerrainId(terrainId);
    }
    @GetMapping("/search")
    public List<SessionDeJeu> searchSessions(
            @RequestParam("dateDebut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam("dateFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin,
            @RequestParam("typeMatch") String typeMatch) {

        return sessionService.searchSessions(dateDebut, dateFin, typeMatch);
    }


    @PostMapping("/rejoindreSession/{equipeId}/{id}")
    public void rejoindreSession( @PathVariable  String equipeId, @PathVariable String id) {
        System.err.println("idSession" + id);
        System.err.println("equipeId" + equipeId);

        sessionService.rejoindreSession(id, equipeId);
    }

}
