package com.example.gestionuser.dto;

public record LoginRequest(
    String email,
    String password
) {}
