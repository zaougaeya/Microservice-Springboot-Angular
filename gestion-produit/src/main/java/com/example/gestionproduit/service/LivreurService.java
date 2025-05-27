package com.example.gestionproduit.service;


import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.gestionproduit.repository.CommandeRepository;
import com.example.gestionproduit.repository.LivreurRepository;
import com.example.gestionproduit.model.Commande;
import com.example.gestionproduit.model.CommandeProduit;
import com.example.gestionproduit.model.Livreur;
import com.example.gestionproduit.model.Panier;
import com.example.gestionproduit.model.Produit;
import com.example.gestionproduit.model.StatutCommande;
import com.example.gestionproduit.model.StatutLivreur;
import com.example.gestionproduit.model.User;
import com.example.gestionproduit.repository.CommandeProduitRepository;
import com.example.gestionproduit.repository.PanierRepository;
import com.example.gestionproduit.repository.ProduitRepository;
import com.example.gestionproduit.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonManagedReference;
@Service
@RequiredArgsConstructor
public class LivreurService {

    private final LivreurRepository livreurRepository;
    private final CommandeRepository commandeRepository;
    private final EmailService emailService;

    public Livreur createLivreur(Livreur livreur) {
        return livreurRepository.save(livreur);
    }

    public Livreur getLivreurById(String id) {
        return livreurRepository.findById(id).orElse(null);
    }
    public List<Livreur> getAllLivreurs() {
        return livreurRepository.findAll();
    }

    public Livreur updateLivreur(String id, Livreur livreur) {
        Livreur existing = getLivreurById(id);
        if (existing != null) {
            existing.setNom(livreur.getNom());
            existing.setPrenom(livreur.getPrenom());
            existing.setNumeroTelephone(livreur.getNumeroTelephone());
            existing.setEmail(livreur.getEmail());
            existing.setStatutLivreur(livreur.getStatutLivreur());
            return livreurRepository.save(existing);
        }
        return null;
    }

    public void deleteLivreur(String id) {
        livreurRepository.deleteById(id);
    }

  @Transactional
public Livreur assignerCommandesAuLivreur(String idLivreur, List<String> idCommandes) {
    if (idCommandes.size() < 1 || idCommandes.size() > 5) {
        throw new RuntimeException("Le nombre de commandes doit être compris entre 1 et 5.");
    }

    Livreur livreur = livreurRepository.findById(idLivreur)
            .orElseThrow(() -> new RuntimeException("Livreur introuvable"));

    List<Commande> commandes = idCommandes.stream()
            .map(id -> commandeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Commande introuvable pour l'ID : " + id)))
            .collect(Collectors.toList());

    for (Commande commande : commandes) {
        if (commande.getLivreur() != null) {
            throw new RuntimeException("La commande " + commande.getIdCommande() + " est déjà affectée.");
        }
        if (commande.getStatutCommande() == StatutCommande.LIVREE || commande.getStatutCommande() == StatutCommande.ANNULEE) {
            throw new RuntimeException("La commande " + commande.getIdCommande() + " ne peut pas être affectée car elle est " + commande.getStatutCommande());
        }
        commande.setLivreur(livreur);
        commande.setDateAffectation(new Date());
        // Statut devient LIVREE immédiatement
        commande.setStatutCommande(StatutCommande.LIVREE);
    }

    commandeRepository.saveAll(commandes);

    if (livreur.getCommandes() == null) {
        livreur.setCommandes(new ArrayList<>());
    }
    livreur.getCommandes().addAll(commandes);

    int nombreTotalCommandes = livreur.getCommandes().size();
    if (nombreTotalCommandes >= 5) {
        livreur.setStatutLivreur(StatutLivreur.EN_COURS_DE_LIVRAISON);
    } else {
        livreur.setStatutLivreur(StatutLivreur.DISPONIBLE);
    }

    livreurRepository.save(livreur);

    try {
        envoyerEmailCircuitLivraison(livreur, commandes);
    } catch (MessagingException e) {
        System.err.println("Erreur lors de l'envoi d'email: " + e.getMessage());
    }

    return livreur;
}



    @Scheduled(cron = "0 0 * * * ?")  
    public void verifierStatutLivreurs() {
        List<Commande> commandes = commandeRepository.findAll();

        for (Commande commande : commandes) {
            if (commande.getDateAffectation() != null) {
                long diffInMillies = Math.abs(new Date().getTime() - commande.getDateAffectation().getTime());
                long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                if (diffInDays >= 3 && commande.getStatutCommande() == StatutCommande.EN_PREPARATION) {
                    Livreur livreur = commande.getLivreur();
                    if (livreur != null) {
                        livreur.setStatutLivreur(StatutLivreur.DISPONIBLE);
                        livreurRepository.save(livreur);
                    }
                }
            }
        }
    }


    /**
  
     *
     * @param livreur   Le livreur concerné.
     * @param commande La commande à livrer.
     * @throws MessagingException Si une erreur se produit lors de l'envoi de l'e-mail.
     */
   private void envoyerEmailCircuitLivraison(Livreur livreur, List<Commande> commandes) throws MessagingException {
    StringBuilder emailContent = new StringBuilder();
    emailContent.append("Bonjour ").append(livreur.getPrenom()).append(",\n\n");
    emailContent.append("Voici les détails de vos commandes à livrer :\n\n");

    List<String> adresses = new ArrayList<>();

    for (Commande commande : commandes) {
        emailContent.append("Commande :\n");
        emailContent.append("  Client: ").append(commande.getNomClient()).append("\n");
        emailContent.append("  Email: ").append(commande.getEmailClient()).append("\n");
        emailContent.append("  Adresse: ").append(commande.getAdresseLivraison()).append("\n");
        emailContent.append("  Produits:\n");

        if (commande.getProduits() != null && !commande.getProduits().isEmpty()) {
            for (CommandeProduit cp : commande.getProduits()) {
                emailContent.append("    - ").append(cp.getProduit().getNom()).append(" x ").append(cp.getQuantite()).append("\n");
            }
        } else {
            emailContent.append("    Aucun produit commandé.\n");
        }
        emailContent.append("\n");
        adresses.add(commande.getAdresseLivraison().replace(" ", "+"));
    }

    String mapsLink;
    if (adresses.size() > 1) {
        mapsLink = "https://www.google.com/maps/dir/?api=1&origin=" + adresses.get(0) +
                   "&destination=" + adresses.get(adresses.size() - 1) +
                   "&waypoints=" + String.join("|", adresses.subList(1, adresses.size() - 1));
    } else if (adresses.size() == 1) {
        mapsLink = "https://www.google.com/maps/dir/?api=1&origin=" + adresses.get(0) +
                   "&destination=" + adresses.get(0);
    } else {
        mapsLink = "https://www.google.com/maps";
    }

    emailContent.append("Itinéraire : ").append(mapsLink);

    emailService.sendEmail(livreur.getEmail(), "Votre tournée de livraison", emailContent.toString());
}

    
    public List<Livreur> getLivreursDisponibles() {
        return livreurRepository.findByStatutLivreur(StatutLivreur.DISPONIBLE);
    }
}
