package com.example.userservice.controller;

import com.example.userservice.model.Equipment;
import com.example.userservice.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/equipments")
public class EquipmentController {

    @Autowired
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