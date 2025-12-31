package com.the_review_company.restaurant_review_system.services.impl;

import com.the_review_company.restaurant_review_system.domain.entities.User;
import com.the_review_company.restaurant_review_system.repositories.UserRepository;
import com.the_review_company.restaurant_review_system.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createOrUpdateUser(User user) {
        return userRepository.findById(user.getId())
                .orElseGet(() -> userRepository.save(user));
    }

    @Override
    public User fetchUser() {
        return null;
    }
}
