package com.example.gestionuser.repository;

import com.example.gestionuser.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByMailuser(String email);
}
