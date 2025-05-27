package com.example.gestionproduit.service;

import com.example.gestionproduit.model.Commande;
import com.example.gestionproduit.model.CommandeProduit;
import com.example.gestionproduit.model.Panier;
import com.example.gestionproduit.model.Produit;
import com.example.gestionproduit.model.StatutCommande;
import com.example.userapi.client.UserClient;
import com.example.userapi.dto.UserResponseDTO;
import com.example.gestionproduit.repository.CommandeProduitRepository;
import com.example.gestionproduit.repository.CommandeRepository;
import com.example.gestionproduit.repository.PanierRepository;
import com.example.gestionproduit.repository.ProduitRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final PanierRepository panierRepository;
    private final CommandeRepository commandeRepository;
    private final CommandeProduitRepository commandeProduitRepository;
    private final ProduitRepository produitRepository;
    private final UserClient userClient;  // Client Feign pour accéder au microservice gestion-user

    private final EmailService emailService;

    public Commande creerCommandeDepuisPanier(String userId, String token) {
        // 1️⃣ Récupération du panier
        Panier panier = panierRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé pour l'utilisateur : " + userId));

        if (panier.getProduits().isEmpty()) {
            throw new RuntimeException("Le panier est vide.");
        }

        // 2️⃣ Récupération des infos utilisateur via gestion-user
        UserResponseDTO user = userClient.getUserById(userId, token);
        if (user == null) {
            throw new RuntimeException("Utilisateur non trouvé avec l'ID : " + userId);
        }

        // 3️⃣ Création de la commande
        Commande commande = new Commande();
        commande.setDateCommande(LocalDate.now().toString());
        commande.setMontantTotal(panier.getTotalTTC());
        commande.setStatutCommande(StatutCommande.EN_PREPARATION);
        commande.setClientId(user.getId());
        commande.setAdresseLivraison(user.getAdresse());
        commande.setNomClient(user.getNom());
        commande.setPrenomClient(user.getPrenom());
        commande.setEmailClient(user.getEmail());
        commande.setTelephoneClient(user.getTel());

        LocalDate dateLivraisonPrevue = calculerDateLivraisonPrevue(LocalDate.now());
        commande.setDateLivraisonPrevue(dateLivraisonPrevue.toString());

        // 4️⃣ Application éventuelle d'une promotion
        String messagePromotionnel = appliquerPromotion(commande);

        // 5️⃣ Sauvegarde initiale de la commande
        Commande savedCommande = commandeRepository.save(commande);

        // 6️⃣ Transfert des produits du panier vers la commande
        List<CommandeProduit> produitsCommande = new ArrayList<>();

        for (CommandeProduit cp : panier.getProduits()) {
            // Met à jour la commande associée
            cp.setCommande(savedCommande);
            cp.setPrixUnitaire(cp.getProduit().getPrixAprèsPromotion());
            cp.setPrixTotal(cp.calculerPrixTotal());

            // Vérification stock
            Produit produit = cp.getProduit();
            int nouvelleQuantite = produit.getQuantiteEnStock() - cp.getQuantite();
            if (nouvelleQuantite < 0) {
                throw new RuntimeException("Stock insuffisant pour le produit : " + produit.getNom());
            }
            produit.setQuantiteEnStock(nouvelleQuantite);

            produitRepository.save(produit);
            commandeProduitRepository.save(cp);

            produitsCommande.add(cp);
        }

        savedCommande.setProduits(produitsCommande);
        commandeRepository.save(savedCommande);

        // 7️⃣ Vider le panier et mettre à jour
        panier.getProduits().clear();
        panier.setMontantTotal(0);
        panier.setTotalTTC(0);
        panierRepository.save(panier);

        // 8️⃣ Envoi de l'email de confirmation
        envoyerEmailConfirmationCommande(user, savedCommande, messagePromotionnel);

        return savedCommande;
    }

    private String appliquerPromotion(Commande commande) {
        if (commande.getMontantTotal() > 400) {
            Random random = new Random();
            int choix = random.nextInt(3);
            switch (choix) {
                case 0:
                    commande.setMontantTotal(commande.getMontantTotal() * 0.8);
                    return "🎉 Vous bénéficiez d'une réduction de 20% sur votre commande !";
                case 1:
                    commande.setMontantTotal(Math.max(0, commande.getMontantTotal() - 8));
                    return "🚚 Félicitations ! La livraison est gratuite (8 DT offerts) !";
                case 2:
                    return "🎁 Félicitations ! Vous bénéficierez de 50% de réduction sur votre prochaine commande supérieure à 200 DT !";
            }
        }
        return null;
    }

    private LocalDate calculerDateLivraisonPrevue(LocalDate dateCommande) {
        int joursAjoutes = 0;
        LocalDate dateLivraison = dateCommande;
        while (joursAjoutes < 3) {
            dateLivraison = dateLivraison.plusDays(1);
            if (!(dateLivraison.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    dateLivraison.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                joursAjoutes++;
            }
        }
        return dateLivraison;
    }

    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    public void supprimerCommandeParId(String idCommande) {
        Commande commande = commandeRepository.findById(idCommande)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        if (commande.getProduits() != null && !commande.getProduits().isEmpty()) {
            commandeProduitRepository.deleteAll(commande.getProduits());
        }
        commandeRepository.delete(commande);
    }

    private void envoyerEmailConfirmationCommande(UserResponseDTO user, Commande commande, String messagePromotionnel) {
        String subject = "Confirmation de votre commande";
        StringBuilder text = new StringBuilder();

        text.append("Bonjour ").append(user.getNom()).append(" ").append(user.getPrenom())
                .append(",\n\nNous avons bien reçu votre commande. Voici les détails :\n");

        if (messagePromotionnel != null) {
            text.append(messagePromotionnel).append("\n");
        }

        text.append("Montant total (TTC) : ").append(String.format("%.2f", commande.getMontantTotal())).append(" DT\n")
                .append("Statut : ").append(commande.getStatutCommande()).append("\n")
                .append("Livraison prévue : ").append(commande.getDateLivraisonPrevue()).append("\n\n")
                .append("🛒 Produits commandés :\n");

        for (CommandeProduit cp : commandeProduitRepository.findByCommande(commande)) {
            text.append("- ").append(cp.getProduit().getNom())
                    .append(" | Qté : ").append(cp.getQuantite())
                    .append(" | PU : ").append(cp.getPrixUnitaire()).append(" DT")
                    .append(" | Total : ").append(cp.getPrixTotal()).append(" DT\n");
        }

        text.append("\nMerci pour votre confiance.\nL'équipe commande");

        try {
            emailService.sendEmail(user.getEmail(), subject, text.toString());
        } catch (Exception e) {
            System.err.println("Erreur envoi mail : " + e.getMessage());
        }
    }
}
