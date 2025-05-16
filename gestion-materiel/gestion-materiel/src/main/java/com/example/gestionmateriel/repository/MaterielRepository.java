// src/main/java/com/example/userservice/repository/MaterielRepository.java
package com.example.gestionmateriel.repository;

import com.example.gestionmateriel.model.Materiel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MaterielRepository extends MongoRepository<Materiel, String> {
    Page<Materiel> findAll(Pageable pageable);

    // Exemple de requête pour la recherche (vous pouvez l'adapter selon vos besoins)
    @Query("{ '$or': [ { 'name': { '$regex': ?0, '$options': 'i' } }, { 'sportType': { '$regex': ?0, '$options': 'i' } }, { 'color': { '$regex': ?0, '$options': 'i' } }, { 'state': { '$regex': ?0, '$options': 'i' } }, { 'noteInterne': { '$regex': ?0, '$options': 'i' } } ] }")
    Page<Materiel> searchAllFields(String searchTerm, Pageable pageable);
}