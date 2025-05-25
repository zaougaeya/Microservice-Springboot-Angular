package com.example.userservice.controller;

import com.example.userservice.model.Consultation;
import com.example.userservice.model.FeedbackConsultation;
import com.example.userservice.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/consultations")
public class ConsultationController {
    @Autowired
    private ConsultationService consultationService;

    @PostMapping
    public Consultation createConsultation(@RequestBody Consultation consultation) {
        return consultationService.saveConsultation(consultation);
    }

    @GetMapping
    public List<Consultation> getAllConsultations() {
        return consultationService.getAllConsultations();
    }

    @GetMapping("/{id}")
    public Optional<Consultation> getConsultationById(@PathVariable String id) {
        return consultationService.getConsultationById(id);
    }

    @PutMapping("/{id}")
    public Consultation updateConsultation(@PathVariable String id, @RequestBody Consultation updatedConsultation) {
        return consultationService.updateConsultation(id, updatedConsultation);
    }

    @DeleteMapping("/{id}")
    public String deleteConsultation(@PathVariable String id) {
        consultationService.deleteConsultation(id);
        return "Consultation supprimée avec succès.";
    }
}