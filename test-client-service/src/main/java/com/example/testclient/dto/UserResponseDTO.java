package com.example.testclient.dto;

import com.example.testclient.model.Job;
import com.example.testclient.model.Role;

public record UserResponseDTO(
    String id,
    String nomuser,
    String prenomuser,
    String mailuser,
    Job job,
    Role role
) {}