package com.the_review_company.restaurant_review_system.controllers;

import com.the_review_company.restaurant_review_system.domain.DTOs.RestaurantCreateUpdateRequestDTO;
import com.the_review_company.restaurant_review_system.domain.DTOs.RestaurantResponseDTO;
import com.the_review_company.restaurant_review_system.domain.RestaurantCreateUpdateRequest;
import com.the_review_company.restaurant_review_system.domain.entities.Restaurant;
import com.the_review_company.restaurant_review_system.mapper.RestaurantMapper;
import com.the_review_company.restaurant_review_system.repositories.RestaurantRepository;
import com.the_review_company.restaurant_review_system.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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


    @PostMapping("/create")
    public ResponseEntity<RestaurantResponseDTO> createRestaurant(
            @RequestBody RestaurantCreateUpdateRequestDTO request,
            @AuthenticationPrincipal Jwt jwt
    ){

        String subject = jwt.getSubject();
        RestaurantCreateUpdateRequest restaurantCreateUpdateRequest = restaurantMapper.fromDTO(request);
//        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurantCreateUpdateRequest, user);
//        return ResponseEntity.ok(restaurantMapper.toDTO(createdRestaurant));
        return null;
    }

//    private void getUserDetails(Jwt jwt) {
//        String username = jwt.getClaimAsString("sub");
//    }
}
