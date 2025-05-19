package com.example.testclient.client;

import com.example.testclient.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "gestion-user")
public interface UserClient {
    @GetMapping("/api/users/{id}")
    UserResponseDTO getUserById(@PathVariable("id") String id,
                                @RequestHeader("Authorization") String token);
    @GetMapping("/api/users/me")
    UserResponseDTO getCurrentUser(@RequestHeader("Authorization") String token);
}