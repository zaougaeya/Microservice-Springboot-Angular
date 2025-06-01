package com.example.gestionuser.dto;

import com.example.gestionuser.model.Job;
import com.example.gestionuser.model.Role;

public record UserResponseDTO(
        String id,
        String nomuser,
        String prenomuser,
        int ageuser,
        String phoneuser,
        String sexeuser,
        String mailuser,
        String addresseuser,
        boolean emailVerified,
        Role role,
        Job job
) {}

