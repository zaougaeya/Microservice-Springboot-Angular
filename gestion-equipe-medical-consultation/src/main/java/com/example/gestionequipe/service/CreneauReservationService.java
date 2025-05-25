package com.example.userservice.service;


import com.example.userservice.model.CreneauReservation;
import com.example.userservice.repository.CreneauReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreneauReservationService {

    @Autowired
    private CreneauReservationRepository repository;

    public CreneauReservation createCreneau(CreneauReservation creneau) {
        return repository.save(creneau);
    }

    public List<CreneauReservation> getAll() {
        return repository.findAll();
    }

    public List<CreneauReservation> getAvailableCreneaux() {
        return repository.findByIsDisponibleTrue();
    }

    public List<CreneauReservation> getByMedecinAndDate(String medecinId, java.time.LocalDate date) {
        return repository.findByMedecinIdAndDate(medecinId, date);
    }

    public void markAsUnavailable(String id) {
        repository.findById(id).ifPresent(creneau -> {
            creneau.setDisponible(false);
            repository.save(creneau);
        });
    }
}


