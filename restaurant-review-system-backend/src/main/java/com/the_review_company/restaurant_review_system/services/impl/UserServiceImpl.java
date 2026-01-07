package com.the_review_company.restaurant_review_system.services.impl;

import com.the_review_company.restaurant_review_system.domain.DTOs.UserDTO;
import com.the_review_company.restaurant_review_system.domain.entities.User;
import com.the_review_company.restaurant_review_system.repositories.UserRepository;
import com.the_review_company.restaurant_review_system.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public User fetchUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("No such user with given id "+id));
    }

    @Override
    public Set<User> fetchUsers(Set<String> userIds) {
        Iterable<User> allById = userRepository.findAllById(userIds);
        Set<User> userSet = new HashSet<>();
        allById.forEach(userSet::add);
        return userSet;
    }

    @Override
    public UserDTO getDummyUserDTO() {
        UserDTO dto;
        dto = UserDTO.builder()
                .id("unknown")
                .username("Unknown User")
                .givenName("")
                .familyName("")
                .email("")
                .build();
        return dto;
    }
}
