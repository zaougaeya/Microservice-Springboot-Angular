package com.example.gestionuser.config;

import com.example.gestionuser.model.Job;
import com.example.gestionuser.model.Role;
import com.example.gestionuser.model.User;
import com.example.gestionuser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Value("${admin.default.email}")
    private String adminEmail;

    @Value("${admin.default.password}")
    private String adminPassword;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) {
        if (userRepository.findByMailuser(adminEmail).isEmpty()) {
            User admin = User.builder()
                    .nomuser("Admin")
                    .prenomuser("Bootstrap")
                    .ageuser(40)
                    .phoneuser("00000000")
                    .sexeuser("Homme")
                    .mailuser(adminEmail)
                    .passworduser(encoder.encode(adminPassword))
                    .addresseuser("HQ")
                    .role(Role.ADMIN)
                    .job(Job.ENTRAINEUR)
                    .build();

            userRepository.save(admin);

            System.out.println("✅ Admin user created via initializer:");
            System.out.println("   Email: " + adminEmail);
            System.out.println("   Password: [From Config]");
        } else {
            System.out.println("ℹ️ Admin user already exists.");
        }
    }
}
