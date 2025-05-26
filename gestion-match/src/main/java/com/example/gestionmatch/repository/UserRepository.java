package com.example.gestionmatch.repository;

import com.example.gestionmatch.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByEquipeId(String equipeId);
}