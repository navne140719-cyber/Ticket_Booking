package com.ticketbooking.controller;

import com.ticketbooking.dto.LoginResponse;
import com.ticketbooking.dto.UserResponse;
import com.ticketbooking.entity.User;
import com.ticketbooking.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.ticketbooking.dto.UserRequest;
import com.ticketbooking.dto.LoginRequest;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody UserRequest request) {
        return userService.createUser(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}


