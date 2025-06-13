package com.example.gestionmatch.service;

import com.example.gestionmatch.model.Equipe;
import com.example.gestionmatch.model.Match;
import com.example.gestionmatch.model.User;
import com.example.gestionmatch.repository.EquipeRepository;
import com.example.gestionmatch.repository.JoueurRepository;
import com.example.gestionmatch.repository.MatchRepository;
import com.example.gestionmatch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class EquipeService {


    private final EquipeRepository equipeRepository;
    private final JoueurRepository joueurRepository;
    private final MatchRepository matchRepository;
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;

    // Injection des dépendances par le constructeur
    public EquipeService(EquipeRepository equipeRepository,
                         JoueurRepository joueurRepository,
                         MatchRepository matchRepository,
                         CloudinaryService cloudinaryService,
                         UserRepository userRepository) {
        this.equipeRepository = equipeRepository;
        this.joueurRepository = joueurRepository;
        this.matchRepository = matchRepository;
        this.cloudinaryService = cloudinaryService;
        this.userRepository = userRepository;
    }


    public List<Equipe> getAllEquipes() {
        return equipeRepository.findAll();
    }


    // Récupérer une équipe par ID
    public Equipe getEquipeById(String id) {
        return equipeRepository.findById(id)
                .orElse(null); // retourne null si non trouvée (tu peux aussi lancer une exception si tu préfères)
    }


    public Equipe createEquipe(String nameEquipe, MultipartFile logoFile) throws IOException {
        // 1) upload sur Cloudinary
        String logoUrl = cloudinaryService.uploadImage(logoFile);


        Equipe equipe = new Equipe();
        equipe.setNameEquipe(nameEquipe);
        equipe.setLogo(logoUrl);

        // 3) persistance en base
        return equipeRepository.save(equipe);
    }

    public Optional<Equipe> updateEquipe(String id, Equipe equipeDetails) {
        return equipeRepository.findById(id)
                .map(equipe -> {
                    equipe.setNameEquipe(equipeDetails.getNameEquipe());
                    equipe.setLogo(equipeDetails.getLogo());
                    return equipeRepository.save(equipe); // Retourne l'objet sauvegardé
                });
    }


    public boolean deleteEquipe(String id) {
        if (!equipeRepository.existsById(id)) {
            return false;
        }

        // 1) Supprimer tous les matchs où l'équipe est présente en tant qu'équipe1
        List<Match> matches1 = matchRepository.findByIdEquipe1(id);
        for (Match m : matches1) {
            matchRepository.delete(m);
        }

        // 2) Supprimer tous les matchs où l'équipe est présente en tant qu'équipe2
        List<Match> matches2 = matchRepository.findByIdEquipe2(id);
        for (Match m : matches2) {
            // Évite de supprimer deux fois le même match s'il est déjà supprimé
            if (!matches1.contains(m)) {
                matchRepository.delete(m);
            }
        }

        // 3) Supprimer l'équipe
        equipeRepository.deleteById(id);
        return true;
    }


    public List<User> getUserByEquipe(String equipeId) {
        return userRepository.findByEquipeId(equipeId);
    }

    public Equipe getUsersAndEquipe(String equipeId) {
        return equipeRepository.findById(equipeId)
                .map(equipe -> {
                    List<User> users = userRepository.findByEquipeId(equipeId);
                    equipe.setUsers(users);
                    return equipe;
                })
                .orElse(null);
    }


}
