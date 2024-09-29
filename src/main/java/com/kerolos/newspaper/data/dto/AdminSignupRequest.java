package com.kerolos.newspaper.data.dto;

import com.kerolos.newspaper.data.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminSignupRequest extends SignupRequest {

    private Role role;

    public AdminSignupRequest(SignupRequest signupRequest) {
        this.setFullName(signupRequest.getFullName());
        this.setEmail(signupRequest.getEmail());
        this.setPassword(signupRequest.getPassword());
        this.setDateOfBirth(signupRequest.getDateOfBirth());
        this.role = Role.USER;
    }

}
