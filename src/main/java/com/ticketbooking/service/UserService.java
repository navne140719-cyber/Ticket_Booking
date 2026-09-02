package com.ticketbooking.service;
import com.ticketbooking.entity.User;
import com.ticketbooking.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import com.ticketbooking.dto.UserRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.ticketbooking.dto.LoginRequest;
import com.ticketbooking.dto.UserResponse;
import com.ticketbooking.dto.LoginResponse;
import com.ticketbooking.security.JwtService;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder,JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User createUser(UserRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(
                request.getName(),
                request.getEmail(),
                encodedPassword
        );
        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElse(null);
        if (user == null) return null;
        boolean matched = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );
        if (!matched) return null;
        String token = jwtService.generateToken(user.getEmail());
        return new LoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                token
        );
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                ))
                .toList();
    }

    public User getUserByEmail(String email) { return userRepository.findByEmail(email).orElse(null);}

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
