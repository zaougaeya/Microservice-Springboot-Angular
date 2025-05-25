package com.example.userservice.controller;

import com.example.userservice.model.EquipeMedicale;
import com.example.userservice.service.EquipeMedicaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RequestMapping("equipes")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class EquipeMedicaleController {

    @Autowired
    private EquipeMedicaleService equipeMedicaleService;

    // CREATE

    @PostMapping
    public EquipeMedicale createEquipeMedicale(@RequestBody EquipeMedicale equipe) {
        return equipeMedicaleService.saveEquipeMedicale(equipe);
    }

    // READ all
    @GetMapping
    public List<EquipeMedicale> getAllEquipes() {
        return equipeMedicaleService.getAllEquipes();
    }

    // READ one
    @GetMapping("/{id}")
    public Optional<EquipeMedicale> getEquipeById(@PathVariable String id) {
        return equipeMedicaleService.getEquipeById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public EquipeMedicale updateEquipeMedicale(@PathVariable String id, @RequestBody EquipeMedicale equipe) {
        return equipeMedicaleService.updateEquipeMedicale(id, equipe);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteEquipeMedicale(@PathVariable String id) {
        equipeMedicaleService.deleteEquipeMedicale(id);
    }


}
