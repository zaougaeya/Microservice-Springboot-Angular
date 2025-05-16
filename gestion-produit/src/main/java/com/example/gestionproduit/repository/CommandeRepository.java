package com.example.gestionproduit.repository;



import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.gestionproduit.model.Commande;
import com.example.gestionproduit.model.StatutCommande;

@Repository
public interface CommandeRepository extends MongoRepository<Commande, String> {
   List<Commande> findAllByClient_Id(String id);
   int countByLivreur_IdLivreur(String idLivreur);
   @Query("{ 'livreur.$id' : ?0 }")
List<Commande> findByLivreurIdLivreur(String idLivreur);

    // Trouver les commandes non affectées (pour affectation automatique)
    List<Commande> findByLivreurIsNullAndStatutCommande(StatutCommande statut);

    @Query("{ 'adresseLivraison' : { $regex: ?0, $options: 'i' } }")
    List<Commande> findByAdresseLivraisonLike(String zonePattern);

    // Trouver les commandes par statut
    List<Commande> findByStatutCommande(StatutCommande statut);
}
