package com.example.gestionmatch.service;

import com.example.gestionmatch.model.SessionDeJeu;
import com.example.gestionmatch.model.User;
import com.example.gestionmatch.repository.SessionDeJeuRepository;
import com.example.gestionmatch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SessionDeJeuService {

    private final SessionDeJeuRepository sessionRepo;
    private final UserRepository userRepository;

    public SessionDeJeuService(SessionDeJeuRepository sessionRepo, UserRepository userRepository) {
        this.sessionRepo = sessionRepo;
        this.userRepository = userRepository;
    }
    // ✅ Constructeur recommandé pour l'injection de dépendance


    public List<SessionDeJeu> getAllSessions() {
        return sessionRepo.findAll();
    }

    public Optional<SessionDeJeu> getSessionById(String id) {
        return sessionRepo.findById(id);
    }

    public SessionDeJeu createSession(SessionDeJeu session) {
        return sessionRepo.save(session);
    }

    public SessionDeJeu updateSession(String id, SessionDeJeu session) {
        session.setId(id);
        return sessionRepo.save(session);
    }

    public void deleteSession(String id) {
        sessionRepo.deleteById(id);
    }

    public List<SessionDeJeu> findByTypeMatch(String type) {
        return sessionRepo.findByTypeMatch(type);
    }

    public List<SessionDeJeu> findByStatut(String statut) {
        return sessionRepo.findByStatut(statut);
    }

    public List<SessionDeJeu> findByTerrainId(String terrainId) {
        return sessionRepo.findByTerrainId(terrainId);
    }
    public List<SessionDeJeu> searchSessions(LocalDateTime dateDebut, LocalDateTime dateFin, String typeMatch) {
        return sessionRepo.findByDateAndType(dateDebut, dateFin, typeMatch);
    }

public void rejoindreSession(String idSession, String equipeId) {
    System.err.println("id" + idSession);

SessionDeJeu session = sessionRepo.findById(idSession).get();
 int n=   session.getJoueursInscrits();
 n++;
 session.setJoueursInscrits(n);
 session.getJoueurs().add("6819fd7706093610282c9fbe");
    System.err.println("session" + session);
 sessionRepo.save(session);

    Optional<User>  user= userRepository.findById("6819fd7706093610282c9fbe");
    user.get().setEquipeId(equipeId);
    userRepository.save(user.get());
    }

}



