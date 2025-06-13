package com.example.gestionmatch.service;

import com.example.gestionmatch.model.User;
import com.example.gestionmatch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;


    public Optional<User> findByEmailAndIdEquipe(String email, String equipeId) {
        return userRepository.findByEmailAndEquipeId(email, equipeId);
    }



//    public void deleteByEmail(String email) {
//        userRepository.deleteByEmail(email);
//    }
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get a user by ID
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    // Create a new user
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Update a user
    public User updateUser(String id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setNom(userDetails.getNom());
        user.setPrenom(userDetails.getPrenom());
        user.setAge(userDetails.getAge());
        user.setSexe(userDetails.getSexe());
        user.setTel(userDetails.getTel());
        user.setAdresse(userDetails.getAdresse());
        user.setEmail(userDetails.getEmail());
        user.setProfession(userDetails.getProfession());
        user.setRole(userDetails.getRole());
        return userRepository.save(user);
    }

    // Delete a user
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}