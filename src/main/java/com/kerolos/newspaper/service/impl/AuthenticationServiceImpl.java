package com.kerolos.newspaper.service.impl;

import com.kerolos.newspaper.data.dto.AdminSignupRequest;
import com.kerolos.newspaper.data.dto.JwtResponse;
import com.kerolos.newspaper.data.dto.LoginRequest;
import com.kerolos.newspaper.data.entity.User;
import com.kerolos.newspaper.repository.UserRepository;
import com.kerolos.newspaper.security.JwtUtils;
import com.kerolos.newspaper.security.TokenBlacklistService;
import com.kerolos.newspaper.service.AuthenticationService;
import com.kerolos.newspaper.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final TokenBlacklistService tokenBlacklistService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;


    @Override
    public JwtResponse login(LoginRequest loginRequest) throws Exception {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new Exception("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new Exception("Invalid credentials");
        }

        refreshTokenService.deleteByUser(user);

        String jwtToken = jwtUtils.generateToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user).getToken();

        return new JwtResponse(jwtToken, refreshToken);
    }


    @Override
    public void signup(AdminSignupRequest signupRequest) throws Exception {

        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new Exception("User with this email already exists");
        }

        String hashedPassword = passwordEncoder.encode(signupRequest.getPassword());

        User user = this.prepareUser(signupRequest, hashedPassword);

        userRepository.save(user);
    }

    @Override
    public void logout(HttpServletRequest request, Authentication authentication) throws Exception {
        String userEmail = authentication.getName();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        tokenBlacklistService.addToBlacklist(jwtUtils.extractToken(request));
        refreshTokenService.deleteByUser(user);
    }

    private User prepareUser(AdminSignupRequest signupRequest, String hashedPassword) {
        User user = new User();
        user.setFullName(signupRequest.getFullName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(hashedPassword);
        user.setDateOfBirth(signupRequest.getDateOfBirth());
        user.setRole(signupRequest.getRole());
        return user;
    }

}
