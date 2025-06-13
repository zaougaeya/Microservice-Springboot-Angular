package com.example.gestionmatch.repository;

import com.example.gestionmatch.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
   List<User> findByEquipeId(String equipeId);
   Optional<User> findByEmailAndEquipeId(String email, String equipeId);

//    Optional<User> findByEmailAndIdEquipe(String email, String equipeId);
//    void deleteByEmail(String mail);
}