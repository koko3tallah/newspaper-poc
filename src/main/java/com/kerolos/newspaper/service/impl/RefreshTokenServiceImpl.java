package com.kerolos.newspaper.service.impl;

import com.kerolos.newspaper.data.dto.JwtResponse;
import com.kerolos.newspaper.data.dto.RefreshTokenRequest;
import com.kerolos.newspaper.data.entity.RefreshToken;
import com.kerolos.newspaper.data.entity.User;
import com.kerolos.newspaper.repository.RefreshTokenRepository;
import com.kerolos.newspaper.security.JwtUtils;
import com.kerolos.newspaper.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${jwt.refreshTokenDurationMs}")
    private long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;

    @Override
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public JwtResponse refreshAccessToken(RefreshTokenRequest refreshTokenRequest) {
        String requestToken = refreshTokenRequest.getRefreshToken();
        RefreshToken refreshToken = this.findByToken(requestToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (!this.isValid(refreshToken)) {
            this.deleteByToken(refreshToken.getToken());
            throw new RuntimeException("Refresh token is expired. Please login again.");
        }

        String newAccessToken = jwtUtils.generateToken(refreshToken.getUser());
        return new JwtResponse(newAccessToken, requestToken);
    }

    @Override
    @Transactional
    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    @Override
    public boolean isValid(RefreshToken refreshToken) {
        return refreshToken.getExpiryDate().isAfter(Instant.now());
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
