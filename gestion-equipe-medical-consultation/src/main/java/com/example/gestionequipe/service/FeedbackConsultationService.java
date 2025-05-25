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

    // CREATE
    public FeedbackConsultation saveFeedbackConsultation(FeedbackConsultation feedbackConsultation) {
        // Ensure that consultationId and userId are provided
        if (feedbackConsultation.getConsultationId() == null || feedbackConsultation.getUserId() == null) {
            throw new RuntimeException("consultationId and userId must be provided.");
        }
        return feedbackConsultationRepository.save(feedbackConsultation);
    }

    // READ ALL for a Consultation
    public List<FeedbackConsultation> getAllFeedbacksForConsultation(String consultationId) {
        return feedbackConsultationRepository.findByConsultationId(consultationId);
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
            // Optionally update consultationId and userId if needed
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