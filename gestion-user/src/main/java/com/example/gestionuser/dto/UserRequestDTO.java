package com.example.gestionuser.dto;

import com.example.gestionuser.model.Job;
import com.example.gestionuser.model.Role;

import jakarta.validation.constraints.*;
import org.springframework.data.mongodb.core.index.Indexed;

public record UserRequestDTO(
    @NotBlank String nomuser,
    @NotBlank String prenomuser,
    @Min(0) int ageuser,
    String phoneuser,
    String sexeuser,
    @Email @NotBlank  @Indexed(unique = true) String mailuser,
    @NotBlank @Size(min = 6) String passworduser,
    String addresseuser,
    @NotNull Job job
) {}
