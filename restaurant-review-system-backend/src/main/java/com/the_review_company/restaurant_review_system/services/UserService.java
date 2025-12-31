package com.the_review_company.restaurant_review_system.services;

import com.the_review_company.restaurant_review_system.domain.entities.User;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UserService {
    User createOrUpdateUser(User user);
    User fetchUser();
}
