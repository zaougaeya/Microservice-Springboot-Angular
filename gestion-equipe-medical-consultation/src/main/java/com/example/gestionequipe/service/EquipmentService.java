package com.example.gestionequipe.service;
import com.example.userapi.client.UserClient;
import com.example.userapi.dto.UserResponseDTO;


import com.example.gestionequipe.model.Equipment;
import com.example.gestionequipe.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    // CRUD de base
    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    public Equipment getEquipmentById(String id) {
        return equipmentRepository.findById(id).orElse(null);
    }

    public Equipment createEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    public Equipment updateEquipment(String id, Equipment equipment) {
        equipment.setId(id);
        return equipmentRepository.save(equipment);
    }

    public void deleteEquipment(String id) {
        equipmentRepository.deleteById(id);
    }

    // Méthodes spécifiques
    public List<Equipment> getAvailableEquipment() {
        return equipmentRepository.findByAvailable(true);
    }

    public List<Equipment> getEquipmentByCategory(String category) {
        return equipmentRepository.findByCategory(category);
    }
}