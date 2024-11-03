package com.kerolos.newspaper.service.impl;

import com.kerolos.newspaper.data.dto.AdminSignupRequest;
import com.kerolos.newspaper.data.dto.UserResponse;
import com.kerolos.newspaper.data.entity.User;
import com.kerolos.newspaper.mapper.UserMapper;
import com.kerolos.newspaper.repository.UserRepository;
import com.kerolos.newspaper.service.AuthenticationService;
import com.kerolos.newspaper.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    @Override
    public void createUser(AdminSignupRequest adminSignupRequest) throws Exception {
        authenticationService.signup(adminSignupRequest);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserResponse> getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toUserDTO);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public UserResponse updateUser(Long id, AdminSignupRequest updatedUser) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!updatedUser.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(updatedUser.getEmail())) {
            throw new Exception("User with this email already exists");
        }
        user.setFullName(updatedUser.getFullName());
        user.setEmail(updatedUser.getEmail());
        user.setDateOfBirth(updatedUser.getDateOfBirth());
        user.setRole(updatedUser.getRole());
        if (updatedUser.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        return userMapper.toUserDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
