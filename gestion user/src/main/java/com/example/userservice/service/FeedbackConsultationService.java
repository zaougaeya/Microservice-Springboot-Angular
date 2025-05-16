package com.example.userservice.service;

import com.example.userservice.model.Consultation;
import com.example.userservice.model.FeedbackConsultation;
import com.example.userservice.repository.ConsultationRepository;
import com.example.userservice.repository.FeedbackConsultationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackConsultationService {
    @Autowired
    private FeedbackConsultationRepository feedbackConsultationRepository;

    @Autowired
    private ConsultationRepository consultationRepository;

    // CREATE
    public FeedbackConsultation saveFeedbackConsultation(String consultationId, FeedbackConsultation feedbackConsultation) {
        Optional<Consultation> consultationOpt = consultationRepository.findById(consultationId);

        if (consultationOpt.isPresent()) {
            Consultation consultation = consultationOpt.get();
            feedbackConsultation.setConsultation(consultation); // Link the feedback to the consultation

            return feedbackConsultationRepository.save(feedbackConsultation); // Save the feedback
        } else {
            throw new RuntimeException("Consultation not found with id " + consultationId);
        }
    }

    // READ ALL for a Consultation
    public List<FeedbackConsultation> getAllFeedbacksForConsultation(String consultationId) {
        return feedbackConsultationRepository.findByConsultationId(consultationId); // Assuming you have a custom query for this
    }

    // READ ONE
    public Optional<FeedbackConsultation> getFeedbackById(String feedbackId) {
        return feedbackConsultationRepository.findById(feedbackId);
    }

    // UPDATE
    public FeedbackConsultation updateFeedbackConsultation(String feedbackId, FeedbackConsultation updatedFeedback) {
        Optional<FeedbackConsultation> existingFeedbackOpt = feedbackConsultationRepository.findById(feedbackId);

        if (existingFeedbackOpt.isPresent()) {
            FeedbackConsultation existingFeedback = existingFeedbackOpt.get();
            existingFeedback.setNote(updatedFeedback.getNote());
            existingFeedback.setCommentaire(updatedFeedback.getCommentaire());

            return feedbackConsultationRepository.save(existingFeedback);
        } else {
            throw new RuntimeException("Feedback not found with id " + feedbackId);
        }
    }

    // DELETE
    public void deleteFeedbackConsultation(String feedbackId) {
        feedbackConsultationRepository.deleteById(feedbackId);
    }
}
