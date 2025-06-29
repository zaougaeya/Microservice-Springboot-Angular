package com.example.userapi.client;

import com.example.userapi.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "gestion-user", url = "${gestion-user.url}")
public interface UserClient {

    @GetMapping("/api/users/{id}")
    UserResponseDTO getUserById(@PathVariable("id") String id,
                                @RequestHeader("Authorization") String token);

    @GetMapping("/api/users/me")
    UserResponseDTO getCurrentUser(@RequestHeader("Authorization") String token);
}
