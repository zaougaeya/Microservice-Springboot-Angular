package com.example.gestionmatch.Repository;

import com.example.userservice.model.Joueur;
import com.example.userservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByEquipeId(String equipeId);
}