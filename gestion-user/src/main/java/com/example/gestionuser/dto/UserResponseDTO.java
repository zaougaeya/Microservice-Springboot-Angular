package com.example.gestionuser.dto;

import com.example.gestionuser.model.Job;
import com.example.gestionuser.model.Role;

public record UserResponseDTO(
    String id,
    String nomuser,
    String prenomuser,
    String mailuser,
    Job job,
    Role role
) {}
