package com.example.userservice.service;

import com.example.userservice.model.Consultation;
import com.example.userservice.model.EquipeMedicale;
import com.example.userservice.model.FeedbackConsultation;
import com.example.userservice.model.User;
import com.example.userservice.repository.ConsultationRepository;
import com.example.userservice.repository.EquipeMedicaleRepository;
import com.example.userservice.repository.FeedbackConsultationRepository;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultationService {
    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EquipeMedicaleRepository equipeMedicaleRepository;

    @Autowired
    private FeedbackConsultationRepository feedbackConsultationRepository; // Added to manage feedback

    // CREATE
    public Consultation saveConsultation(Consultation consultation, String userId, String equipeMedicaleId, List<FeedbackConsultation> feedbacks) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<EquipeMedicale> equipeMedicaleOpt = equipeMedicaleRepository.findById(equipeMedicaleId);

        if (userOpt.isPresent() && equipeMedicaleOpt.isPresent()) {
            consultation.setUser(userOpt.get());
            consultation.setEquipeMedicale(equipeMedicaleOpt.get());

            // Save feedbacks if present
            if (feedbacks != null) {
                for (FeedbackConsultation feedback : feedbacks) {
                    feedback.setConsultation(consultation); // Set the consultation for each feedback
                    feedbackConsultationRepository.save(feedback); // Save the feedback to the database
                }
            }

            return consultationRepository.save(consultation); // Save the consultation
        } else {
            throw new RuntimeException("User or EquipeMedicale not found");
        }
    }

    // READ ALL
    public List<Consultation> getAllConsultations() {
        return consultationRepository.findAll();
    }

    // READ ONE
    public Optional<Consultation> getConsultationById(String id) {
        return consultationRepository.findById(id);
    }

    // UPDATE
    public Consultation updateConsultation(String id, Consultation updatedConsultation, String userId, String equipeMedicaleId, List<FeedbackConsultation> feedbacks) {
        Optional<Consultation> existingConsultationOpt = consultationRepository.findById(id);
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<EquipeMedicale> equipeMedicaleOpt = equipeMedicaleRepository.findById(equipeMedicaleId);

        if (existingConsultationOpt.isPresent()) {
            Consultation existingConsultation = existingConsultationOpt.get();

            if (userOpt.isPresent()) {
                existingConsultation.setUser(userOpt.get());
            }
            if (equipeMedicaleOpt.isPresent()) {
                existingConsultation.setEquipeMedicale(equipeMedicaleOpt.get());
            }

            existingConsultation.setDateConsultation(updatedConsultation.getDateConsultation());
            existingConsultation.setRapport(updatedConsultation.getRapport());

            // Save or update feedbacks if present
            if (feedbacks != null) {
                existingConsultation.setFeedbacks(feedbacks);
                for (FeedbackConsultation feedback : feedbacks) {
                    feedback.setConsultation(existingConsultation); // Set the consultation for each feedback
                    feedbackConsultationRepository.save(feedback); // Save the feedback to the database
                }
            }

            return consultationRepository.save(existingConsultation);
        } else {
            throw new RuntimeException("Consultation not found with id " + id);
        }
    }

    // DELETE
    public void deleteConsultation(String id) {
        consultationRepository.deleteById(id);
    }
}
