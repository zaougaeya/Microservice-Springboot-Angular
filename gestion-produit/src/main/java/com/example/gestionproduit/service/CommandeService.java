package com.example.gestionproduit.service;


import com.example.gestionproduit.model.*;
import com.example.gestionproduit.repository.*;
import com.example.userapi.client.UserClient;
import com.example.userapi.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.gestionproduit.service.EmailService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final PanierRepository panierRepository;
    private final CommandeRepository commandeRepository;
    private final CommandeProduitRepository commandeProduitRepository;
    private final ProduitRepository produitRepository;
    private final UserClient userClient;
    private final EmailService emailService;

    public Map<String, Object> creerCommandeDepuisPanier(String userId, String token) {
     
        Panier panier = panierRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Aucun panier trouvé pour l'utilisateur : " + userId));

        if (panier.getProduits().isEmpty()) {
            throw new RuntimeException("Le panier est vide.");
        }

         UserResponseDTO user = userClient.getCurrentUser(token);
        if (user == null) {
            throw new RuntimeException("Utilisateur non trouvé avec l'ID : " + userId);
        }

        // 🧾 3. Créer la commande
        Commande commande = new Commande();
        commande.setDateCommande(LocalDate.now().toString());
        commande.setMontantTotal(panier.getTotalTTC());
        commande.setStatutCommande(StatutCommande.EN_PREPARATION);
        commande.setClientId(user.id());
        commande.setAdresseLivraison(user.addresseuser());
        commande.setNomClient(user.nomuser());
        commande.setEmailClient(user.mailuser());
        commande.setTelephoneClient(user.phoneuser());

        LocalDate dateLivraison = calculerDateLivraisonPrevue(LocalDate.now());
        commande.setDateLivraisonPrevue(dateLivraison.toString());

        // 🎁 4. Appliquer une éventuelle promotion
        String messagePromo = appliquerPromotion(commande);

        // 💾 5. Sauvegarder la commande
        Commande savedCommande = commandeRepository.save(commande);

        // 📦 6. Transférer les produits du panier vers la commande
        List<CommandeProduit> produitsCommande = new ArrayList<>();

        for (CommandeProduit cp : panier.getProduits()) {
            Produit produit = cp.getProduit();

            int stockRestant = produit.getQuantiteEnStock() - cp.getQuantite();
            if (stockRestant < 0) {
                throw new RuntimeException("Stock insuffisant pour le produit : " + produit.getNom());
            }

            produit.setQuantiteEnStock(stockRestant);
            produitRepository.save(produit);

            cp.setCommande(savedCommande);
            cp.setPrixUnitaire(produit.getPrixAprèsPromotion());
            cp.setPrixTotal(cp.calculerPrixTotal());

            commandeProduitRepository.save(cp);
            produitsCommande.add(cp);
        }

        savedCommande.setProduits(produitsCommande);
        commandeRepository.save(savedCommande);

        // 🧹 7. Vider le panier
        panier.getProduits().clear();
        panier.setMontantTotal(0);
        panier.setTotalTTC(0);
        panierRepository.save(panier);

        // 📧 8. Envoyer l'email de confirmation
        envoyerEmailConfirmationCommande(user, savedCommande, messagePromo);

      Map<String, Object> result = new HashMap<>();
    result.put("commande", savedCommande);
    result.put("messagePromo", messagePromo);

    return result;
    }

    private String appliquerPromotion(Commande commande) {
        if (commande.getMontantTotal() > 400) {
            int choix = new Random().nextInt(3);
            switch (choix) {
                case 0 -> {
                    commande.setMontantTotal(commande.getMontantTotal() * 0.8);
                    return "🎉 Vous bénéficiez d'une réduction de 20% sur votre commande !";
                }
                case 1 -> {
                    commande.setMontantTotal(Math.max(0, commande.getMontantTotal() - 8));
                    return "🚚 Livraison gratuite ! (8 DT offerts)";
                }
                case 2 -> {
                    return "🎁 50% de réduction sur votre prochaine commande de plus de 200 DT !";
                }
            }
        }
        return null;
    }

    private LocalDate calculerDateLivraisonPrevue(LocalDate dateCommande) {
        int joursAjoutes = 0;
        LocalDate dateLivraison = dateCommande;

        while (joursAjoutes < 3) {
            dateLivraison = dateLivraison.plusDays(1);
            if (dateLivraison.getDayOfWeek() != DayOfWeek.SATURDAY &&
                dateLivraison.getDayOfWeek() != DayOfWeek.SUNDAY) {
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

    private void envoyerEmailConfirmationCommande(UserResponseDTO user, Commande commande, String promoMessage) {
        String subject = "Confirmation de votre commande";
        StringBuilder text = new StringBuilder();

        text.append("Bonjour ").append(user.nomuser()).append(" ").append(user.prenomuser()).append(",\n\n")
            .append("Votre commande a bien été enregistrée. Détails :\n");

        if (promoMessage != null) {
            text.append(promoMessage).append("\n");
        }

        text.append("Montant total (TTC) : ")
            .append(String.format("%.2f", commande.getMontantTotal())).append(" DT\n")
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
            emailService.sendEmail(user.mailuser(), subject, text.toString());
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }
}
