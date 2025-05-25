package com.example.gestionequipe.service;
import com.example.userapi.client.UserClient;
import com.example.userapi.dto.UserResponseDTO;



import com.example.gestionequipe.model.Consultation;
import com.example.gestionequipe.model.CreneauReservation;
import com.example.gestionequipe.model.ReservationConsultation;
import com.example.gestionequipe.repository.ConsultationRepository;
import com.example.gestionequipe.repository.CreneauReservationRepository;
import com.example.gestionequipe.repository.ReservationConsultationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Service
public class ReservationConsultationService {
    private final ReservationConsultationRepository reservationConsultationRepository;
    private final ConsultationRepository consultationRepository;
    private final CreneauReservationRepository creneauRepository;
    private final UserClient userClient;

    // Constructor injection
   /* public ReservationConsultationService(ReservationConsultationRepository reservationRepo,
                                          ConsultationRepository consultationRepo,
                                          CreneauReservationRepository creneauRepo,
                                          UserClient userClient) {
        this.reservationConsultationRepository = reservationRepo;
        this.consultationRepository = consultationRepo;
        this.creneauRepository = creneauRepo;
        this.userClient = userClient;
    }*/



    // CREATE
  /*  public ReservationConsultation saveReservation(ReservationConsultation reservation) {
        return reservationConsultationRepository.save(reservation);
    }*/
   /* public boolean isMedecinDisponible(String medecinId, LocalDate date, LocalTime heureDebut, LocalTime heureFin) {
        // Récupère tous les créneaux pour ce médecin à cette date
        List<CreneauReservation> creneaux = creneauReservationRepository.findByMedecinIdAndDate(medecinId, date);

        for (CreneauReservation creneau : creneaux) {
            if (creneau.isDisponible()) {
                // Si le créneau disponible couvre toute la plage demandée
                boolean debutOk = !creneau.getHeureDebut().isAfter(heureDebut);
                boolean finOk = !creneau.getHeureFin().isBefore(heureFin);
                if (debutOk && finOk) {
                    return true;
                }
            }
        }

        return false; // Aucun créneau ne couvre la période
    }*/
 /*public ReservationConsultation saveReservation(ReservationConsultation reservation) {
     return reservationConsultationRepository.save(reservation);
 }*/

    public ReservationConsultation saveReservation(ReservationConsultation reservation, String token) {
        UserResponseDTO user = userClient.getCurrentUser(token);
        System.out.println(user);

        reservation.setPatientId(user.id());
        return reservationConsultationRepository.save(reservation);
    }
   /* public ReservationConsultation saveReservation(ReservationConsultation reservation) {
        // 1. Check if the creneau exists and is available
        String creneauId = reservation.getCreneauId();
        CreneauReservation creneau = creneauRepository.findById(creneauId)
                .orElseThrow(() -> new RuntimeException("Creneau not found"));

        if (!creneau.isDisponible()) {
            throw new RuntimeException("Ce créneau est déjà réservé.");
        }

        // 2. Mark the creneau as unavailable
        creneau.setDisponible(false);
        creneauRepository.save(creneau);

        // 3. Save the reservation
        return reservationConsultationRepository.save(reservation);
    }
*/


    // READ
    public List<ReservationConsultation> getAllReservations() {
        return reservationConsultationRepository.findAll();
    }

    public Optional<ReservationConsultation> getReservationById(String id) {
        return reservationConsultationRepository.findById(id);
    }

    // UPDATE
    public ReservationConsultation updateReservation(String id, ReservationConsultation updatedReservation) {
        return reservationConsultationRepository.findById(id).map(reservation -> {
            reservation.setDateConsultation(updatedReservation.getDateConsultation());
            reservation.setEquipeMedicaleId(updatedReservation.getEquipeMedicaleId());
            reservation.setMedecinId(updatedReservation.getMedecinId());
            reservation.setPatientId(updatedReservation.getPatientId());
            reservation.setSpecialite(updatedReservation.getSpecialite());
            reservation.setStatutConsultation(updatedReservation.getStatutConsultation());
            reservation.setMotifConsultation(updatedReservation.getMotifConsultation());
            reservation.setMoyenCommunication(updatedReservation.getMoyenCommunication());
            reservation.setIsUrgent(updatedReservation.getIsUrgent());
            reservation.setDureeConsultation(updatedReservation.getDureeConsultation());

            // ✅ Create Consultation if statut is "A_VENIR" and no consultationId yet
            if ("VALIDEE".equalsIgnoreCase(updatedReservation.getStatutConsultation())
                    && (reservation.getConsultationId() == null || reservation.getConsultationId().isEmpty())) {

                Consultation consultation = new Consultation();
                consultation.setDateConsultation(updatedReservation.getDateConsultation());
                consultation.setUserId(updatedReservation.getMedecinId()); // or patientId depending on logic
                consultation.setEquipeMedicaleId(updatedReservation.getEquipeMedicaleId());
                consultation.setReservationId(reservation.getId());

                Consultation savedConsultation = consultationRepository.save(consultation);
                reservation.setConsultationId(savedConsultation.getId());
            }

            return reservationConsultationRepository.save(reservation);
        }).orElseThrow(() -> new RuntimeException("Reservation not found with id " + id));
    }

    // DELETE
    public void deleteReservation(String id) {
        reservationConsultationRepository.deleteById(id);
    }

    public List<CreneauReservation> getAvailableTimeSlotsForMedecin(String medecinId, LocalDate date) {
        // Define working hours (example: 9:00 to 17:00)
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(17, 0);

        // Generate all possible 30-min slots for the day
        List<LocalTime> allStartTimes = new ArrayList<>();
        LocalTime time = startTime;
        while (time.isBefore(endTime)) {
            allStartTimes.add(time);
            time = time.plusMinutes(30);
        }

        // Fetch all reserved slots (unavailable) for this medecin & date
        List<CreneauReservation> reservedSlots = creneauRepository.findByMedecinIdAndDate(medecinId, date)
                .stream()
                .filter(creneau -> !creneau.isDisponible())  // only unavailable
                .collect(Collectors.toList());

        // Filter out reserved slots from allStartTimes
        List<CreneauReservation> availableSlots = new ArrayList<>();
        for (LocalTime start : allStartTimes) {
            boolean isReserved = reservedSlots.stream().anyMatch(reserved ->
                    reserved.getHeureDebut().equals(start)
            );
            if (!isReserved) {
                // Create a CreneauReservation object representing available slot
                CreneauReservation slot = new CreneauReservation();
                slot.setMedecinId(medecinId);
                slot.setDate(date);
                slot.setHeureDebut(start);
                slot.setHeureFin(start.plusMinutes(30));
                slot.setDisponible(true);
                availableSlots.add(slot);
            }
        }

        return availableSlots;
    }
}