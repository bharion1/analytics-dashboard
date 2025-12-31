package com.portfolio.service;

import com.portfolio.dto.LoginRequest;
import com.portfolio.dto.LoginResponse;
import com.portfolio.model.User;
import com.portfolio.repository.UserRepository;
import com.portfolio.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);
        
        User user = userRepository.findByUsername(loginRequest.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        return new LoginResponse(token, user.getUsername(), user.getName(), "Bearer");
    }
    
    @Transactional
    public User register(String username, String password, String email, String name) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username já existe");
        }
        
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email já existe");
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setName(name);
        user.setEnabled(true);
        
        Set<User.Role> roles = new HashSet<>();
        roles.add(User.Role.USER);
        user.setRoles(roles);
        
        return userRepository.save(user);
    }
}

