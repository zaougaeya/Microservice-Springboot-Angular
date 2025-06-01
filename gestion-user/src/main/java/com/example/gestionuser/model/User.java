package com.example.gestionuser.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {
    @Id
    private String id;

    private String nomuser;
    private String prenomuser;
    private int ageuser;
    private String phoneuser;
    private String sexeuser;
    private String mailuser;
    private String passworduser;
    private String addresseuser;

    private Role role;
    private Job job;
    private String verificationCode;
    private boolean emailVerified = false;
}
