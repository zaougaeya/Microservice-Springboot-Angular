package com.example.userservice.repository;

import com.example.userservice.model.EquipeMedicale;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipeMedicaleRepository extends MongoRepository<EquipeMedicale, String> {


}
