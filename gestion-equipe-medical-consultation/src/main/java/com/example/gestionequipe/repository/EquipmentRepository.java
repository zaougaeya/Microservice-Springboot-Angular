package com.example.gestionequipe.repository;

import com.example.gestionequipe.model.Equipment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EquipmentRepository extends MongoRepository<Equipment, String> {
    List<Equipment> findByCategory(String category);
    List<Equipment> findByAvailable(boolean available);
}