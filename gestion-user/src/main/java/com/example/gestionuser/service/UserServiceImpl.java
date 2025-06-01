package com.example.gestionuser.service;
import com.example.gestionuser.model.Role;
import org.springframework.security.access.prepost.PreAuthorize;


import com.example.gestionuser.model.User;
import com.example.gestionuser.model.Job;
import com.example.gestionuser.repository.UserRepository;
import com.example.gestionuser.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private final Map<String, String> resetTokens = new HashMap<>();
    @Autowired
    private com.example.gestionuser.service.MailServiceImpl mailServiceImpl;

    @Override
    public User register(User user) {
        System.out.println("[INFO] Starting user registration...");

        if (userRepository.findByMailuser(user.getMailuser()).isPresent()) {
            throw new IllegalArgumentException("Email already exists.");
        }

        System.out.println("[INFO] Affecting USER as role...");
        user.setRole(Role.USER);

        System.out.println("[INFO] Encoding password...");
        user.setPassworduser(encoder.encode(user.getPassworduser()));

        // ✅ Generate verification code
        String code = String.format("%06d", new Random().nextInt(999999));
        user.setVerificationCode(code);
        user.setEmailVerified(false);
        System.out.println("🔐 Verification code for " + user.getMailuser() + ": " + code);
        System.out.println("[INFO] Saving user to database...");
        User savedUser = userRepository.save(user);

        System.out.println("[INFO] Sending verification email...");
        String subject = "Your Verification Code";
        String message = "Hello " + user.getPrenomuser() + ",\n\n"
                + "Your verification code is: " + code + "\n\n"
                + "Enter this code in the app to verify your email.\n\n"
                + "Thank you.";

        mailServiceImpl.sendEmail(user.getMailuser(), subject, message);

        return savedUser;
    }


    @Override
    public String login(String email, String password) {
        User user = userRepository.findByMailuser(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!encoder.matches(password, user.getPassworduser())) {
            throw new RuntimeException("Invalid password");
        }
        if (!user.isEmailVerified()) {
            throw new IllegalStateException("Please verify your email before logging in.");
        }
        return jwtUtil.generateToken(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.findByMailuser(email);
    }

    @Override
    public List<User> searchUsers(String name, String email, String role) {
        List<User> all = userRepository.findAll();
        return all.stream()
                .filter(u -> name == null || u.getNomuser().toLowerCase().contains(name.toLowerCase()))
                .filter(u -> email == null || u.getMailuser().toLowerCase().contains(email.toLowerCase()))
                .toList();
    }

    @Override
    public User updateUser(String id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userDetails.getNomuser() != null) user.setNomuser(userDetails.getNomuser());
        if (userDetails.getPrenomuser() != null) user.setPrenomuser(userDetails.getPrenomuser());
        if (userDetails.getAgeuser() != 0) user.setAgeuser(userDetails.getAgeuser());
        if (userDetails.getPhoneuser() != null) user.setPhoneuser(userDetails.getPhoneuser());
        if (userDetails.getSexeuser() != null) user.setSexeuser(userDetails.getSexeuser());

        if (userDetails.getMailuser() != null) {
            Optional<User> existing = userRepository.findByMailuser(userDetails.getMailuser());
            if (existing.isPresent() && !existing.get().getId().equals(id)) {
                throw new RuntimeException("Email already in use by another user");
            }
            user.setMailuser(userDetails.getMailuser());
        }

        if (userDetails.getPassworduser() != null && !userDetails.getPassworduser().isEmpty()) {
            user.setPassworduser(encoder.encode(userDetails.getPassworduser()));
        }

        if (userDetails.getAddresseuser() != null) user.setAddresseuser(userDetails.getAddresseuser());

        // ✅ Only allow ADMINs to update the role
        if (userDetails.getRole() != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ADMIN"));
            if (isAdmin) {
                user.setRole(Role.USER);
            } else {
                throw new RuntimeException("Only ADMINs can update the role.");
            }
        }

        System.out.println("[INFO] Saving user to database...");
        return userRepository.save(user);
    }


    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public void sendPasswordResetEmail(String mailuser) {
        Optional<User> userOpt = userRepository.findByMailuser(mailuser);
        if (userOpt.isEmpty()) throw new RuntimeException("Email not found");

        String token = UUID.randomUUID().toString();
        resetTokens.put(token, mailuser);

        System.out.println("Reset token for " + mailuser + ": " + token);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        String mailuser = resetTokens.get(token);
        if (mailuser == null) throw new RuntimeException("Invalid or expired token");

        User user = userRepository.findByMailuser(mailuser)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassworduser(encoder.encode(newPassword));
        userRepository.save(user);

        resetTokens.remove(token);
    }
    @Override
    public boolean isOwner(String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;

        String userId = auth.getName(); // now it's the userId from the JWT
        boolean result = userId.equals(id);

        System.out.println("🧠 Checking ownership: " + userId + " is owner of " + id + " = " + result);
        return result;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public User createAdmin(User user) {
        System.out.println("[INFO] Creating admin user...");
        if (userRepository.findByMailuser(user.getMailuser()).isPresent()) {
            throw new RuntimeException("User already exists with this email.");
        }
        System.out.println("[INFO] Encoding password...");
        user.setPassworduser(encoder.encode(user.getPassworduser()));
        user.setRole(Role.ADMIN);
        System.out.println("[INFO] Saving user to database...");
        return userRepository.save(user);
    }
    @Override
    public boolean verifyEmailCode(String email, String code) {
        Optional<User> optionalUser = userRepository.findByMailuser(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationCode() != null && user.getVerificationCode().equals(code)) {
                user.setEmailVerified(true);
                user.setVerificationCode(null); // Optional: clear code after success
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

}


