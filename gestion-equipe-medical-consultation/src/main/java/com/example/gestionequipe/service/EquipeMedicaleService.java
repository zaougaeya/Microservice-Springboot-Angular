package com.example.gestionequipe.service;
import com.example.userapi.client.UserClient;
import com.example.userapi.dto.UserResponseDTO;


import com.example.gestionequipe.model.Consultation;
import com.example.gestionequipe.model.EquipeMedicale;
import com.example.gestionequipe.model.User;
import com.example.gestionequipe.repository.ConsultationRepository;
import com.example.gestionequipe.repository.EquipeMedicaleRepository;
import com.example.gestionequipe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipeMedicaleService {
    @Autowired
    private EquipeMedicaleRepository equipeMedicaleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConsultationRepository consultationRepository;

    // CREATE
    public EquipeMedicale saveEquipeMedicale(EquipeMedicale equipe) {
        List<User> fullUsers = equipe.getMembres().stream().map(user -> {
            return userRepository.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("User with ID " + user.getId() + " does not exist."));
        }).toList();

        equipe.setMembres(fullUsers); // Embed full user objects
        return equipeMedicaleRepository.save(equipe);
    }

    // READ
    public List<EquipeMedicale> getAllEquipes() {
        return equipeMedicaleRepository.findAll();
    }

    public Optional<EquipeMedicale> getEquipeById(String id) {
        return equipeMedicaleRepository.findById(id);
    }

    // UPDATE
    public EquipeMedicale updateEquipeMedicale(String id, EquipeMedicale updatedEquipe) {
        return equipeMedicaleRepository.findById(id).map(equipe -> {
            equipe.setNomEquipeMedicale(updatedEquipe.getNomEquipeMedicale());
            equipe.setDescEquipeMedicale(updatedEquipe.getDescEquipeMedicale());

            // Fetch and set full User objects
            if (updatedEquipe.getMembres() != null) {
                List<User> fullUsers = updatedEquipe.getMembres().stream().map(user ->
                        userRepository.findById(user.getId())
                                .orElseThrow(() -> new RuntimeException("User not found: " + user.getId()))
                ).toList();
                equipe.setMembres(fullUsers);
            } else {
                equipe.setMembres(null);
            }

            // Fetch and set full Consultation objects
            if (updatedEquipe.getConsultations() != null) {
                List<Consultation> fullConsultations = updatedEquipe.getConsultations().stream().map(consult ->
                        consultationRepository.findById(consult.getId())
                                .orElseThrow(() -> new RuntimeException("Consultation not found: " + consult.getId()))
                ).toList();
                equipe.setConsultations(fullConsultations);
            } else {
                equipe.setConsultations(null);
            }

            return equipeMedicaleRepository.save(equipe);
        }).orElseThrow(() -> new RuntimeException("EquipeMedicale not found with id " + id));
    }

    // DELETE
    public void deleteEquipeMedicale(String id) {
        equipeMedicaleRepository.deleteById(id);
    }
}
