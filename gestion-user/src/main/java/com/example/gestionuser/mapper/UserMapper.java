package com.example.gestionuser.mapper;

import com.example.gestionuser.dto.*;
import com.example.gestionuser.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponseDTO toDto(User user) {
        return new UserResponseDTO(
            user.getId(),
            user.getNomuser(),
            user.getPrenomuser(),
            user.getAgeuser(),
            user.getPhoneuser(),
            user.getSexeuser(),
            user.getAddresseuser(),
            user.getMailuser(),
            user.getJob(),
            user.getRole()
        );
    }

    public User toEntity(UserRequestDTO dto) {
        return User.builder()
            .nomuser(dto.nomuser())
            .prenomuser(dto.prenomuser())
            .ageuser(dto.ageuser())
            .phoneuser(dto.phoneuser())
            .sexeuser(dto.sexeuser())
            .mailuser(dto.mailuser())
            .passworduser(dto.passworduser())
            .addresseuser(dto.addresseuser())
            .job(dto.job())
            .build();
    }
}
