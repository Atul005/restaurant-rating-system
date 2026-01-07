package com.the_review_company.restaurant_review_system.services;

import com.the_review_company.restaurant_review_system.domain.DTOs.UserDTO;
import com.the_review_company.restaurant_review_system.domain.entities.User;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Set;

public interface UserService {
    User createOrUpdateUser(User user);
    User fetchUser(String userId);
    Set<User> fetchUsers(Set<String> userIds);
    UserDTO getDummyUserDTO();
}
