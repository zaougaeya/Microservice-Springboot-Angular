package com.example.gestionmateriel.service;

import com.example.gestionmateriel.dto.ReservationCreateDTO;
import com.example.gestionmateriel.dto.ReservationResponseDTO;
import com.example.gestionmateriel.model.Materiel;
import com.example.gestionmateriel.model.Reservation;
import com.example.gestionmateriel.model.ReservationStatus;
import com.example.gestionmateriel.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MaterielService materielService;

    /**
     * Crée une nouvelle réservation à partir du DTO, après vérification de disponibilité.
     */
    public Reservation createReservation(ReservationCreateDTO dto) {
        Materiel materiel = materielService.getMaterielById(dto.getMaterielId())
                .orElseThrow(() -> new RuntimeException("Matériel non trouvé avec l'ID : " + dto.getMaterielId()));

        if (!isMaterielAvailable(materiel.getId(), dto.getStartDate(), dto.getEndDate())) {
            throw new RuntimeException("Matériel indisponible pour cette période.");
        }

        Reservation reservation = new Reservation();
        reservation.setReservedBy(dto.getReservedBy());
        reservation.setMateriel(materiel);
        reservation.setStartDate(dto.getStartDate());
        reservation.setEndDate(dto.getEndDate());
        reservation.setStatus(ReservationStatus.CONFIRMED);

        return reservationRepository.save(reservation);
    }

    /**
     * Vérifie si un matériel est disponible entre deux dates.
     */
    public boolean isMaterielAvailable(String materielId, LocalDateTime start, LocalDateTime end) {
        return reservationRepository
                .findByMaterielIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(materielId, end, start)
                .stream()
                .noneMatch(r -> r.getStatus() != ReservationStatus.CANCELLED);
    }

    public List<Reservation> getReservationsByReservedBy(String reservedBy) {
        return reservationRepository.findByReservedBy(reservedBy);
    }

    public void cancelReservation(String reservationId) {
        reservationRepository.findById(reservationId).ifPresent(reservation -> {
            reservation.setStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(reservation);
        });
    }

    public List<Reservation> getReservationsByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public ReservationResponseDTO mapToDTO(Reservation reservation) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(reservation.getId());
        dto.setReservedBy(reservation.getReservedBy());
        dto.setMaterielId(reservation.getMateriel().getId());
        dto.setMaterielName(reservation.getMateriel().getName());
        dto.setStartDate(reservation.getStartDate());
        dto.setEndDate(reservation.getEndDate());
        dto.setStatus(reservation.getStatus().name());
        return dto;
    }
}
