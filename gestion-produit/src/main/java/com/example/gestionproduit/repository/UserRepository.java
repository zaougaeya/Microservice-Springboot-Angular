package com.example.gestionproduit.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.gestionproduit.model.User;

public interface UserRepository extends MongoRepository<User, String> {
    // Custom query methods can be added here
}