package com.example.gestionequipe.controller;

import com.example.gestionequipe.model.FeedbackConsultation;
import com.example.gestionequipe.service.FeedbackConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("feedbacks")
public class FeedbackConsultationController {
    @Autowired
    private FeedbackConsultationService feedbackConsultationService;

    // CREATE
    @PostMapping("/ajouterFeedback")
    public FeedbackConsultation createFeedback(@RequestBody FeedbackConsultation feedbackConsultation) {
        return feedbackConsultationService.saveFeedbackConsultation(feedbackConsultation);
    }

    // READ ALL for a specific consultation
    @GetMapping("/afficherFeedbacks/{consultationId}")
    public List<FeedbackConsultation> getAllFeedbacks(@PathVariable String consultationId) {
        return feedbackConsultationService.getAllFeedbacksForConsultation(consultationId);
    }

    // READ ONE
    @GetMapping("/{id}")
    public Optional<FeedbackConsultation> getFeedbackById(@PathVariable String id) {
        return feedbackConsultationService.getFeedbackById(id);
    }

    // UPDATE
    @PutMapping("/modifierFeedback/{id}")
    public FeedbackConsultation updateFeedback(@PathVariable String id, @RequestBody FeedbackConsultation updatedFeedback) {
        return feedbackConsultationService.updateFeedbackConsultation(id, updatedFeedback);
    }

    // DELETE
    @DeleteMapping("/supprimerFeedback/{id}")
    public String deleteFeedback(@PathVariable String id) {
        feedbackConsultationService.deleteFeedbackConsultation(id);
        return "Feedback supprimé avec succès.";
    }
}
