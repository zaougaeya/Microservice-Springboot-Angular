package com.example.gestionmatch.controller;

import com.example.gestionmatch.model.Match;
import com.example.gestionmatch.repository.MatchRepository;
import com.example.gestionmatch.service.MatchService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;
    private final MatchRepository matchRepository;

    // Injection des dépendances via le constructeur
    public MatchController(MatchService matchService,
                           MatchRepository matchRepository) {
        this.matchService = matchService;
        this.matchRepository = matchRepository;
    }

    @PostMapping
    public ResponseEntity<Match> createMatch(@RequestBody Match match) {
        try {
            // Appel à la méthode du service
            Match savedMatch = matchService.createMatch(match);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMatch);
        } catch (RuntimeException e) {
            // Gestion des erreurs en cas d'exception lancée par le service
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateMatch(@PathVariable String id, @RequestBody Match matchDetails) {
        try {
            Match updatedMatch = matchService.updateMatch(id, matchDetails);
            if (updatedMatch != null) {
                return ResponseEntity.ok(updatedMatch);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Match non trouvé.");
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }


    @GetMapping
    public List<Match> getAllMatches() {
        return matchService.getAllMatches();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable String id) {
        boolean deleted = matchService.deleteMatch(id);

        if (deleted) {
            return ResponseEntity.noContent().build(); // ✅ 204 No Content
        } else {
            return ResponseEntity.notFound().build();   // ✅ 404 Not Found
        }
    }


    @PostMapping("/matchsDetails")
    public ResponseEntity<Match> matchsDetails(
            @RequestBody Match matchDetails) {

        Match match = matchService.matchDetails(matchDetails);
        return ResponseEntity.ok(match);
    }


    @PostMapping("/affecter/{matchId}/{equipeId1}/{equipeId2}")
    public ResponseEntity<String> affecterEquipesAMatch(@PathVariable String matchId,
                                                        @PathVariable String equipeId1,
                                                        @PathVariable String equipeId2) {
        try {
            matchService.affecterEquipesAMatch(matchId, equipeId1, equipeId2);
            return ResponseEntity.ok("Les équipes ont été affectées au match avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @DeleteMapping("/desaffecter/{matchId}/{equipeId}")
    public ResponseEntity<String> desaffecterUneEquipeDuMatch(
            @PathVariable String matchId,
            @PathVariable String equipeId) {

        try {
            // Appel du service pour désaffecter l'équipe du match
            matchService.desaffecterUneEquipeDuMatch(matchId, equipeId);
            // Retourner un message de succès si la désaffectation a bien été effectuée
            return ResponseEntity.ok("L'équipe a été désaffectée du match avec succès.");
        } catch (RuntimeException e) {
            // Gestion des erreurs (match ou équipe non trouvés)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }




    @PutMapping("/{id}/scores")
    public ResponseEntity<String> affecterScores(
            @PathVariable String id,
            @RequestParam int scoreEquipe1,
            @RequestParam int scoreEquipe2) {

        try {
            matchService.affecterScores(id, scoreEquipe1, scoreEquipe2);
            return ResponseEntity.ok("Scores mis à jour avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Match non trouvé");
        }
    }


    @PutMapping("/{id}/terrain")
    public ResponseEntity<String> affecterTerrain(
            @PathVariable("id") String matchId,
            @RequestParam String terrainId) {

        try {
            matchService.affecterTerrain(matchId, terrainId);
            return ResponseEntity.ok("Terrain affecté avec succès au match");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/affecter-date/{matchId}")
    public ResponseEntity<Match> affecterDateAuMatch(@PathVariable String matchId,
                                                     @RequestBody Map<String, String> body) {
        try {
            String dateString = body.get("date");

            // Convertir manuellement la date string en objet Date
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date nouvelleDate = formatter.parse(dateString);

            Match match = matchRepository.findById(matchId)
                    .orElseThrow(() -> new RuntimeException("Match non trouvé"));

            //  match.setDate(nouvelleDate);
            matchRepository.save(match);

            return ResponseEntity.ok(match);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PutMapping("/desaffecter-date/{matchId}")
    public ResponseEntity<Match> desaffecterDateDuMatch(@PathVariable String matchId) {
        try {
            Match match = matchRepository.findById(matchId)
                    .orElseThrow(() -> new RuntimeException("Match non trouvé"));

            match.setDate(null);  // Désaffecter la date
            matchRepository.save(match);

            return ResponseEntity.ok(match);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @GetMapping("/statistiques")
    public ResponseEntity<Map<String, Map<String, Integer>>> getStatistiquesParEquipe() {
        Map<String, Map<String, Integer>> stats = matchService.calculerStatistiquesParEquipe();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/statistiques/{dateD}/{dateF}")
    public ResponseEntity<Map<String, Map<String, Integer>>> getStatistiquesParEquipeEtDates(
            @PathVariable String dateD,
            @PathVariable String dateF) {

        Map<String, Map<String, Integer>> stats = matchService.calculerStatistiquesParEquipeEtDates(dateD, dateF);
        System.out.println(dateD + "............." + dateF);
        return ResponseEntity.ok(stats);
    }
    @GetMapping("/search")
    public List<Match> searchMatchs(
            @RequestParam("dateDebut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam("dateFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam("type") String type) {

        return matchService.searchMatchs(dateDebut, dateFin, type);
    }


    @PostMapping("/rejoindreMatch/{equipeId}/{id}")
    public void rejoindreMatch(@RequestHeader("Authorization") String authHeader,
                               @PathVariable String equipeId,
                               @PathVariable String id) {
        matchService.rejoindreMatch(id, equipeId, authHeader);
    }

    @GetMapping("/joues")
    public List<Match> getMatchsJoues() {
        return matchService.getMatchsJoues();
    }

}





 