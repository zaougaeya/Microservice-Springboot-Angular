package com.example.gestionequipe.controller;
import com.example.userapi.client.UserClient;
import com.example.userapi.dto.UserResponseDTO;


import com.example.gestionequipe.model.Equipment;
import com.example.gestionequipe.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipments")
public class EquipmentController {

    
    private EquipmentService equipmentService;

    // CRUD
    @GetMapping
    public List<Equipment> getAllEquipment() {
        return equipmentService.getAllEquipment();
    }

    @GetMapping("/{id}")
    public Equipment getEquipmentById(@PathVariable String id) {
        return equipmentService.getEquipmentById(id);
    }

    @PostMapping
    public Equipment createEquipment(@RequestBody Equipment equipment) {
        return equipmentService.createEquipment(equipment);
    }

    @PutMapping("/{id}")
    public Equipment updateEquipment(@PathVariable String id, @RequestBody Equipment equipment) {
        return equipmentService.updateEquipment(id, equipment);
    }

    @DeleteMapping("/{id}")
    public void deleteEquipment(@PathVariable String id) {
        equipmentService.deleteEquipment(id);
    }

    // Endpoints spécifiques
    @GetMapping("/available")
    public List<Equipment> getAvailableEquipment() {
        return equipmentService.getAvailableEquipment();
    }

    @GetMapping("/category/{category}")
    public List<Equipment> getEquipmentByCategory(@PathVariable String category) {
        return equipmentService.getEquipmentByCategory(category);
    }
}

// TODO: Inject UserClient in EquipmentController.java via constructor
