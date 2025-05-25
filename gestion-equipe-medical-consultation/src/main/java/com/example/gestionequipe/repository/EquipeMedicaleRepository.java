package com.example.gestionequipe.repository;

import com.example.gestionequipe.model.EquipeMedicale;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipeMedicaleRepository extends MongoRepository<EquipeMedicale, String> {


}
