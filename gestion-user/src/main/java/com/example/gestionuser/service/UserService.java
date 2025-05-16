package com.example.gestionuser.service;

import com.example.gestionuser.model.User;
import com.example.gestionuser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Temporary in-memory token store (should use DB or Redis in real apps)
    private final Map<String, String> resetTokens = new HashMap<>();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User register(User user) throws Exception {
        if (userRepository.findByMailuser(user.getMailuser()).isPresent()) {
            throw new Exception("User already exists with this email.");
        }

        user.setPassworduser(passwordEncoder.encode(user.getPassworduser()));
        return userRepository.save(user);
    }

    public String login(String mailuser, String passworduser) throws Exception {
        Optional<User> userOpt = userRepository.findByMailuser(mailuser);

        if (userOpt.isEmpty() || !passwordEncoder.matches(passworduser, userOpt.get().getPassworduser())) {
            throw new Exception("Invalid credentials");
        }

        // TODO: Replace with JWT generation
        return "MOCK-JWT-TOKEN";
    }

    public void sendPasswordResetEmail(String mailuser) throws Exception {
        Optional<User> userOpt = userRepository.findByMailuser(mailuser);
        if (userOpt.isEmpty()) throw new Exception("Email not found");

        String token = UUID.randomUUID().toString();
        resetTokens.put(token, mailuser);

        // Simulate sending email
        System.out.println("Reset token for " + mailuser + ": " + token);
    }

    public void resetPassword(String token, String newPassword) throws Exception {
        String mailuser = resetTokens.get(token);
        if (mailuser == null) throw new Exception("Invalid or expired token");

        Optional<User> userOpt = userRepository.findByMailuser(mailuser);
        if (userOpt.isEmpty()) throw new Exception("User not found");

        User user = userOpt.get();
        user.setPassworduser(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetTokens.remove(token); // Invalidate token after use
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public User updateUser(String id, User userDetails) throws Exception {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            throw new Exception("User not found with id: " + id);
        }

        User existingUser = userOpt.get();

        // Update fields if they're provided in userDetails
        if (userDetails.getNomuser() != null) {
            existingUser.setNomuser(userDetails.getNomuser());
        }
        if (userDetails.getPrenomuser() != null) {
            existingUser.setPrenomuser(userDetails.getPrenomuser());
        }
        if (userDetails.getAgeuser() != 0) {
            existingUser.setAgeuser(userDetails.getAgeuser());
        }
        if (userDetails.getPhoneuser() != null) {
            existingUser.setPhoneuser(userDetails.getPhoneuser());
        }
        if (userDetails.getSexeuser() != null) {
            existingUser.setSexeuser(userDetails.getSexeuser());
        }
        if (userDetails.getMailuser() != null) {
            // Check if new email is already taken by another user
            Optional<User> userWithSameEmail = userRepository.findByMailuser(userDetails.getMailuser());
            if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(id)) {
                throw new Exception("Email already in use by another user");
            }
            existingUser.setMailuser(userDetails.getMailuser());
        }
        if (userDetails.getPassworduser() != null && !userDetails.getPassworduser().isEmpty()) {
            existingUser.setPassworduser(passwordEncoder.encode(userDetails.getPassworduser()));
        }
        if (userDetails.getAddresseuser() != null) {
            existingUser.setAddresseuser(userDetails.getAddresseuser());
        }

        return userRepository.save(existingUser);
    }














}
