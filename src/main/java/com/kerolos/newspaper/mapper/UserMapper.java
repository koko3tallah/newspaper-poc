package com.kerolos.newspaper.mapper;

import com.kerolos.newspaper.data.dto.UserResponse;
import com.kerolos.newspaper.data.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toUserDTO(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setRole(user.getRole());

        if (user.getCreatedBy() != null) {
            dto.setCreatedBy(user.getCreatedBy().getFullName());
        }

        if (user.getModifiedBy() != null) {
            dto.setModifiedBy(user.getModifiedBy().getFullName());
        }

        dto.setCreatedAt(user.getCreatedAt());
        dto.setModifiedAt(user.getModifiedAt());

        return dto;
    }
}
