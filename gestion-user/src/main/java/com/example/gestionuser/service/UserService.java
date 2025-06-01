package com.example.gestionuser.service;

import com.example.gestionuser.model.User;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User register(User user);

    String login(String email, String password);

    List<User> getAllUsers();

    Optional<User> getUserById(String id);

    Optional<User> getByEmail(String email);

    List<User> searchUsers(String name, String email, String role);

    User updateUser(String id, User userDetails);
    boolean  isOwner(String id);

    void deleteUser(String id);

    void sendPasswordResetEmail(String mailuser);

    void resetPassword(String token, String newPassword);

    public User createAdmin(User user);

    boolean verifyEmailCode(String email, String code);

}
