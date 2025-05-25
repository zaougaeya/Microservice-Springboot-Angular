package com.example.gestionequipe.service;
import com.example.userapi.client.UserClient;
import com.example.userapi.dto.UserResponseDTO;


import com.example.gestionequipe.model.Consultation;
import com.example.gestionequipe.repository.ConsultationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultationService {
    @Autowired
    private ConsultationRepository consultationRepository;

    public Consultation saveConsultation(Consultation consultation) {
        if (consultation.getUserId() == null || consultation.getEquipeMedicaleId() == null) {
            throw new RuntimeException("userId and equipeMedicaleId are required.");
        }

        consultation.setDateConsultation(consultation.getDateConsultation() != null
                ? consultation.getDateConsultation()
                : new Date());

        return consultationRepository.save(consultation);
    }

    public List<Consultation> getAllConsultations() {
        return consultationRepository.findAll();
    }

    public Optional<Consultation> getConsultationById(String id) {
        return consultationRepository.findById(id);
    }

    public Consultation updateConsultation(String id, Consultation updatedConsultation) {
        Optional<Consultation> existing = consultationRepository.findById(id);
        if (existing.isPresent()) {
            Consultation consult = existing.get();

            consult.setUserId(updatedConsultation.getUserId());
            consult.setEquipeMedicaleId(updatedConsultation.getEquipeMedicaleId());
            consult.setDateConsultation(updatedConsultation.getDateConsultation());
            consult.setRapport(updatedConsultation.getRapport());
            consult.setFeedbacks(updatedConsultation.getFeedbacks());

            return consultationRepository.save(consult);
        } else {
            throw new RuntimeException("Consultation not found.");
        }
    }

    public void deleteConsultation(String id) {
        consultationRepository.deleteById(id);
    }
}