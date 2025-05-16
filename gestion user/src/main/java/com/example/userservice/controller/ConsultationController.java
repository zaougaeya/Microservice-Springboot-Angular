package com.example.userservice.controller;

import com.example.userservice.model.Consultation;
import com.example.userservice.model.FeedbackConsultation;
import com.example.userservice.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/consultations")
public class ConsultationController {
    @Autowired
    private ConsultationService consultationService;

    // CREATE
    @PostMapping
    public Consultation createConsultation(@RequestBody Consultation consultation,
                                           @RequestParam String userId, // Accept userId as a parameter
                                           @RequestParam String equipeMedicaleId, // Accept equipeMedicaleId as a parameter
                                           @RequestParam(required = false) List<FeedbackConsultation> feedbacks) { // Accept feedbacks as a parameter
        return consultationService.saveConsultation(consultation, userId, equipeMedicaleId, feedbacks);
    }

    // READ ALL
    @GetMapping
    public List<Consultation> getAllConsultations() {
        return consultationService.getAllConsultations();
    }

    // READ ONE
    @GetMapping("/{id}")
    public Optional<Consultation> getConsultationById(@PathVariable String id) {
        return consultationService.getConsultationById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Consultation updateConsultation(@PathVariable String id, @RequestBody Consultation updatedConsultation,
                                           @RequestParam String userId, @RequestParam String equipeMedicaleId,
                                           @RequestParam(required = false) List<FeedbackConsultation> feedbacks) {
        return consultationService.updateConsultation(id, updatedConsultation, userId, equipeMedicaleId, feedbacks);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteConsultation(@PathVariable String id) {
        consultationService.deleteConsultation(id);
        return "Consultation supprimée avec succès.";
    }
}
