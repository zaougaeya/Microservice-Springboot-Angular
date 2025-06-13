package com.example.gestionmatch.config;

import com.example.gestionmatch.model.Role;
import lombok.Data;

public record UserResponseDTO(String id, String nomuser, String prenomuser, String mailuser, String job, Role role) {
    public UserResponseDTO(String id, String nomuser, String prenomuser, String mailuser, String job, Role role) {
        this.id = id;
        this.nomuser = nomuser;
        this.prenomuser = prenomuser;
        this.mailuser = mailuser;
        this.job = job;
        this.role = role;
    }

    public String id() {
        return this.id;
    }

    public String nomuser() {
        return this.nomuser;
    }

    public String prenomuser() {
        return this.prenomuser;
    }

    public String mailuser() {
        return this.mailuser;
    }

    public String job() {
        return this.job;
    }

    public Role role() {
        return this.role;
    }
}
