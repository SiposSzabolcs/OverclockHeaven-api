package com.example.OverclockHeaven.Security.auth;

import com.example.OverclockHeaven.Security.config.JwtService;
import com.example.OverclockHeaven.Security.user.Role;
import com.example.OverclockHeaven.Security.user.User;
import com.example.OverclockHeaven.Security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService{

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        Role role = Role.valueOf(request.getRole().toUpperCase());

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .result(true)
                .msg("User registered successfully")
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            var user = repository.findByEmail(request.getEmail())
                    .orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .result(true)
                    .msg("Authentication successful")
                    .build();
        } catch (Exception e) {
            return AuthenticationResponse.builder()
                    .result(false)
                    .msg("Invalid email or password")
                    .build();
        }
    }
}
