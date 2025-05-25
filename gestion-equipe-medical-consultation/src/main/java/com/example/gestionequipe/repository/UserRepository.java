package com.example.gestionequipe.repository;

import com.example.gestionequipe.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // Custom query methods can be added here
}