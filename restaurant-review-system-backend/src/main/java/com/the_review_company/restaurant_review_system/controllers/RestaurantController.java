package com.the_review_company.restaurant_review_system.controllers;

import com.the_review_company.restaurant_review_system.domain.DTOs.RestaurantCreateUpdateRequestDTO;
import com.the_review_company.restaurant_review_system.domain.DTOs.RestaurantResponseDTO;
import com.the_review_company.restaurant_review_system.domain.RestaurantCreateUpdateRequest;
import com.the_review_company.restaurant_review_system.domain.entities.Restaurant;
import com.the_review_company.restaurant_review_system.domain.entities.User;
import com.the_review_company.restaurant_review_system.mapper.RestaurantMapper;
import com.the_review_company.restaurant_review_system.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;


    @PostMapping
    public ResponseEntity<RestaurantResponseDTO> createRestaurant(
            @RequestBody RestaurantCreateUpdateRequestDTO request,
            @AuthenticationPrincipal User user
    ){

        RestaurantCreateUpdateRequest restaurantCreateUpdateRequest = restaurantMapper.fromDTO(request);
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurantCreateUpdateRequest, user);
        return ResponseEntity.ok(restaurantMapper.toDTO(createdRestaurant));

    }

}
