package com.example.testclient.controller;

import com.example.userapi.dto.UserResponseDTO;
import com.example.userapi.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final UserClient userClient;

    @GetMapping("/user/{id}")
    public UserResponseDTO testUserFetch(@PathVariable String id,
                                         @RequestHeader("Authorization") String token) {
        return userClient.getUserById(id, token);
    }
}