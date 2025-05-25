package com.example.gestionuser.controller;

import com.example.gestionuser.dto.UserRequestDTO;
import com.example.gestionuser.dto.UserResponseDTO;
import com.example.gestionuser.mapper.UserMapper;
import com.example.gestionuser.model.User;
import com.example.gestionuser.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {


    private final UserMapper userMapper;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequestDTO dto) {
        User createdUser = userService.register(userMapper.toEntity(dto));
        return ResponseEntity.ok(userMapper.toDto(createdUser));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("mailuser");
        String password = credentials.get("passworduser");
        String token = userService.login(email, password);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role
    ) {
        List<User> users = userService.searchUsers(name, email, role);
        List<UserResponseDTO> dtos = users.stream()
                .map(userMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> getUserById(@PathVariable String id) {
        Optional<User> userOpt = userService.getUserById(id);

        if (userOpt.isPresent()) {
            UserResponseDTO dto = userMapper.toDto(userOpt.get());
            return ResponseEntity.ok(dto);
        } else {
            Map<String, String> error = Map.of("error", "User not found");
            return ResponseEntity.status(404).body(error);
        }
    }
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDTO> getCurrentUser(Authentication auth) {
        Optional<User> userOpt = userService.getUserById(auth.getName());
        System.out.println("🧠 AUTH ID : " + auth.getName());
        return userOpt.map(user -> ResponseEntity.ok(userMapper.toDto(user)))
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @userService.isOwner(#id)")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable String id, @Valid @RequestBody User userUpdate) {
        return ResponseEntity.ok(userMapper.toDto(userService.updateUser(id, userUpdate)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @userService.isOwner(#id)")
        public ResponseEntity<?> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-request")
    public ResponseEntity<?> requestReset(@RequestBody Map<String, String> payload) {
        String email = payload.get("mailuser");
        userService.sendPasswordResetEmail(email);
        return ResponseEntity.ok(Map.of("message", "Reset token sent (check console log)."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String newPassword = payload.get("newPassword");
        userService.resetPassword(token, newPassword);
        return ResponseEntity.ok(Map.of("message", "Password updated successfully."));
    }



    @PostMapping("/admin")
    public ResponseEntity<UserResponseDTO> createAdmin(@RequestBody UserRequestDTO userDTO) {
        System.out.println("[INFO] Request received to create admin user");
        User user = userMapper.toEntity(userDTO);
        User saved = userService.createAdmin(user);
        return ResponseEntity.ok(userMapper.toDto(saved));
    }
}
