package com.example.gestionproduit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gestionproduit.model.Livreur;
import com.example.gestionproduit.model.StatutVoiture;
import com.example.gestionproduit.model.Voiture;
import com.example.gestionproduit.repository.LivreurRepository;
import com.example.gestionproduit.repository.VoitureRepository;

import java.util.List;

@Service
public class VoitureService {

    @Autowired
    private VoitureRepository voitureRepository;
    @Autowired
    private LivreurRepository livreurRepository;
   /*   @Autowired
    private GeocodingService geocodingService;*/

    // ➡️ Ajouter une voiture
    public Voiture ajouterVoiture(Voiture voiture) {
        voiture.setStatutVoiture(StatutVoiture.DISPONIBLE);  
        return voitureRepository.save(voiture);
    }
    // VoitureService.java
public void affecterVoitureALivreur(String idVoiture, String idLivreur) throws Exception {
    Voiture voiture = voitureRepository.findById(idVoiture)
            .orElseThrow(() -> new Exception("Voiture introuvable."));
    
    if (!voiture.getStatutVoiture().equals(StatutVoiture.DISPONIBLE)) {
        throw new Exception("Cette voiture n'est pas disponible.");
    }

    Livreur livreur = livreurRepository.findById(idLivreur)
            .orElseThrow(() -> new Exception("Livreur introuvable."));

    if (livreur.getVoiture() != null) {
        throw new Exception("Ce livreur a déjà une voiture affectée.");
    }

    voiture.setStatutVoiture(StatutVoiture.NON_DISPONIBLE);
    voiture.setLivreur(livreur);
    livreur.setVoiture(voiture);

    voitureRepository.save(voiture);
    livreurRepository.save(livreur);
}


    // ➡️ Récupérer toutes les voitures
    public List<Voiture> getAllVoitures() {
        return voitureRepository.findAll();
    }

    // ➡️ Récupérer les voitures disponibles
    public List<Voiture> getVoituresDisponibles() {
        return voitureRepository.findByStatutVoiture("DISPONIBLE");
    }
    
   /*  public double calculerConsommationCO2(String idVoiture, String adresse) throws Exception {
        Voiture voiture = voitureRepository.findById(idVoiture)
                .orElseThrow(() -> new Exception("Voiture introuvable."));
    
        double[] coordinates = geocodingService.getCoordinatesFromAddress(adresse);
    
        double latitudeDest = coordinates[0];
        double longitudeDest = coordinates[1];
    
        double distance = calculerDistance(CHARGUIA2_LATITUDE, CHARGUIA2_LONGITUDE, latitudeDest, longitudeDest);
        double consommationCO2 = distance * voiture.getCo2ParKm();
    
        System.out.println("Distance parcourue : " + distance + " km");
        System.out.println("Consommation CO2 estimée : " + consommationCO2 + " kg");
    
        return consommationCO2;
    }*/
    










}
