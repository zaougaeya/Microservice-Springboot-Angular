package com.example.gestionproduit.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gestionproduit.model.Commande;
import com.example.gestionproduit.model.CommandeProduit;
import com.example.gestionproduit.model.Panier;
import com.example.gestionproduit.model.Produit;
import com.example.gestionproduit.model.StatutCommande;
import com.example.gestionproduit.model.User;
import com.example.gestionproduit.repository.CommandeProduitRepository;
import com.example.gestionproduit.repository.CommandeRepository;
import com.example.gestionproduit.repository.PanierRepository;
import com.example.gestionproduit.repository.ProduitRepository;
import com.example.gestionproduit.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonManagedReference;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final PanierRepository panierRepository;
    private final CommandeRepository commandeRepository;
    private final CommandeProduitRepository commandeProduitRepository;
    private final ProduitRepository produitRepository; 
    private final UserRepository userRepository;

    @Autowired
    private EmailService emailService;

   public Commande creerCommandeDepuisPanier(String userId) {
    // 🔎 1️⃣ Récupération du panier de l'utilisateur
    Panier panier = panierRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Panier non trouvé pour l'utilisateur : " + userId));

    if (panier.getProduits().isEmpty()) {
        throw new RuntimeException("Le panier est vide.");
    }

    // 🔎 2️⃣ Récupération de l'utilisateur
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

    // 📦 3️⃣ Création de l'objet Commande
    Commande commande = new Commande();
    commande.setDateCommande(LocalDate.now().toString());
    commande.setMontantTotal(panier.getTotalTTC());
    commande.setStatutCommande(StatutCommande.EN_PREPARATION);
    commande.setClient(user);
    commande.setAdresseLivraison(user.getAdresse());
    commande.setNomClient(user.getNom());
    commande.setEmailClient(user.getEmail());
    commande.setTelephoneClient(user.getTel());

    // 📅 4️⃣ Calcul de la date de livraison prévue
    LocalDate dateLivraisonPrevue = calculerDateLivraisonPrevue(LocalDate.now());
    commande.setDateLivraisonPrevue(dateLivraisonPrevue.toString());

    // 🏷️ 5️⃣ Application de la promotion si le montant dépasse 400 DT
    String messagePromotionnel = appliquerPromotion(commande, user);

    // 💾 6️⃣ Sauvegarde de la commande dans la base de données
    Commande savedCommande = commandeRepository.save(commande);

    // 🛒 7️⃣ Enregistrement des produits dans commande_produit
    
    List<CommandeProduit> produitsCommande = new ArrayList<>();

    for (CommandeProduit cp : panier.getProduits()) {
        // 🔄 Association avec la commande
        cp.setCommande(savedCommande);

        // 🏷️ Mise à jour des prix avec les promotions
        cp.setPrixUnitaire(cp.getProduit().getPrixAprèsPromotion());
        cp.setPrixTotal(cp.calculerPrixTotal());

        // 💾 Sauvegarde dans la collection commande_produit
        commandeProduitRepository.save(cp);

        // ➕ Ajout à la liste de commande
        produitsCommande.add(cp);

        // 🔄 Mise à jour du stock du produit
        Produit produit = cp.getProduit();
        produit.setQuantiteEnStock(produit.getQuantiteEnStock() - cp.getQuantite());
        produitRepository.save(produit);
    }

    // 🔄 8️⃣ Mise à jour de la commande avec les produits
    savedCommande.setProduits(produitsCommande);
    commandeRepository.save(savedCommande);

    // 🧹 🔄 9️⃣ Vider le panier après la commande
    panier.getProduits().clear();
    panier.setMontantTotal(0);
    panier.setTotalTTC(0);
    panierRepository.save(panier);

    // ✉️ 🔟 Envoi de l'email de confirmation
    envoyerEmailConfirmationCommande(user, savedCommande, messagePromotionnel);

    // ✅ 🔟 Retourner la commande avec les produits inclus
    return savedCommande;
}

    private String appliquerPromotion(Commande commande, User user) {
        if (commande.getMontantTotal() > 400) {
            Random random = new Random();
            int choix = random.nextInt(3); 

            switch (choix) {
                case 0:
                    double nouveauMontant = commande.getMontantTotal() * 0.8; 
                    commande.setMontantTotal(nouveauMontant);
                    return "🎉 Vous bénéficiez d'une réduction de 20% sur votre commande !";
                case 1:
                    if (commande.getMontantTotal() >= 8) {
                        commande.setMontantTotal(commande.getMontantTotal() - 8);
                        return "🚚 Félicitations ! La livraison est gratuite (8 DT offerts) !";
                    } else {
                        commande.setMontantTotal(0.0); 
                        return "🚚 Félicitations ! La livraison est gratuite (8 DT offerts) ! Le montant total a été ajusté.";
                    }
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
            if (!(dateLivraison.getDayOfWeek() == DayOfWeek.SATURDAY || dateLivraison.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                joursAjoutes++;
            }
        }
        return dateLivraison;
    }

    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    public void supprimerCommandeUtilisateur(String userId) {
        List<Commande> commandes = commandeRepository.findAllByClient_Id(userId);

        if (commandes.isEmpty()) {
            throw new RuntimeException("Aucune commande trouvée pour cet utilisateur.");
        }

        for (Commande commande : commandes) {
            if (commande.getProduits() != null && !commande.getProduits().isEmpty()) {
                commandeProduitRepository.deleteAll(commande.getProduits());
            }
            commandeRepository.delete(commande);
        }
    }

    private void envoyerEmailConfirmationCommande(User user, Commande commande, String messagePromotionnel) {
        String subject = "Confirmation de votre commande";
        LocalDate dateLivraisonPrevue = calculerDateLivraisonPrevue(LocalDate.now());
        String dateLivraisonFormattee = dateLivraisonPrevue.toString();
        
        // ✉️ Construction de l'email
        StringBuilder text = new StringBuilder();
        text.append("Bonjour ").append(user.getNom()).append(" ").append(user.getPrenom())
                .append(",\n\nNous avons bien reçu votre commande. Voici les détails :\n");
    
        // 🎉 Application du message promotionnel
        if (messagePromotionnel != null) {
            text.append("🎉 **Félicitations ! Vous avez une promotion :** ").append(messagePromotionnel).append("\n");
        }
    
        text.append("Montant total (TTC) : ").append(String.format("%.2f", commande.getMontantTotal())).append(" DT\n")
            .append("Statut de la commande : ").append(commande.getStatutCommande()).append("\n")
            .append("✨ *La date de livraison prévue est estimée pour le : ").append(dateLivraisonFormattee).append("*\n\n");
    
        // 🛒 Liste des produits commandés
        text.append("🛒 **Voici les produits commandés :**\n");
        for (CommandeProduit produitCommande : commandeProduitRepository.findByCommande(commande)) {
            text.append("- ").append(produitCommande.getProduit().getNom())
                .append(" | Quantité : ").append(produitCommande.getQuantite())
                .append(" | Prix unitaire : ").append(String.format("%.2f", produitCommande.getPrixUnitaire())).append(" DT")
                .append(" | Total : ").append(String.format("%.2f", produitCommande.getPrixTotal())).append(" DT\n");
        }
        
        text.append("\n*Reste disponible pour la réception de votre commande à cette date.*\n\n");
    
        if (messagePromotionnel != null && messagePromotionnel.contains("prochaine commande")) {
            text.append("N'oubliez pas, vous bénéficiez de 50% de réduction sur votre prochaine commande supérieure à 200 DT !\n\n");
        }
    
        text.append("Merci pour votre confiance, cher(e) Client(e).\n\nCordialement,\nL'équipe de Livraison.");
    
        // ✉️ Envoi de l'email
        try {
            emailService.sendEmail(user.getEmail(), subject, text.toString());
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
            e.printStackTrace();
        }
    }
}    