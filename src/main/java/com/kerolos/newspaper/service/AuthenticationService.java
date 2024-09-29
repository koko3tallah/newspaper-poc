package com.kerolos.newspaper.service;

import com.kerolos.newspaper.data.dto.AdminSignupRequest;
import com.kerolos.newspaper.data.dto.JwtResponse;
import com.kerolos.newspaper.data.dto.LoginRequest;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * AuthenticationService provides the contract for user authentication, registration,
 * and logout functionalities. This service handles login, signup, and logout requests.
 */
public interface AuthenticationService {

    /**
     * Authenticates a user based on login credentials and generates a JWT token upon success.
     *
     * @param loginRequest the {@link LoginRequest} containing the user's email and password.
     * @return a {@link JwtResponse} containing the JWT access token and refresh token.
     * @throws Exception if authentication fails or any other error occurs during login.
     */
    JwtResponse login(LoginRequest loginRequest) throws Exception;

    /**
     * Registers a new user with the specified details.
     *
     * @param signupRequest the {@link AdminSignupRequest} containing the user details such as full name,
     *                      email, password, date of birth, and role.
     * @throws Exception if the registration fails, such as if the email already exists or validation errors occur.
     */
    void signup(AdminSignupRequest signupRequest) throws Exception;

    /**
     * Logs out the authenticated user and invalidates the current session or token.
     *
     * @param request        the {@link HttpServletRequest} containing the user's request details.
     * @param authentication the {@link Authentication} object representing the authenticated user.
     * @throws Exception if an error occurs during logout or if the user is not authenticated.
     */
    void logout(HttpServletRequest request, Authentication authentication) throws Exception;

}
