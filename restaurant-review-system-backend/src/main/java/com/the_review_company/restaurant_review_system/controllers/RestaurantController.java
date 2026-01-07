package com.the_review_company.restaurant_review_system.controllers;

import com.the_review_company.restaurant_review_system.domain.DTOs.*;
import com.the_review_company.restaurant_review_system.domain.RestaurantCreateUpdateRequest;
import com.the_review_company.restaurant_review_system.domain.entities.Restaurant;
import com.the_review_company.restaurant_review_system.domain.entities.Review;
import com.the_review_company.restaurant_review_system.domain.entities.User;
import com.the_review_company.restaurant_review_system.exceptions.RestaurantNotFoundException;
import com.the_review_company.restaurant_review_system.mapper.RestaurantMapper;
import com.the_review_company.restaurant_review_system.mapper.UserMapper;
import com.the_review_company.restaurant_review_system.services.RestaurantService;
import com.the_review_company.restaurant_review_system.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;
    private final UserMapper userMapper;
    private final UserService userService;


    @PostMapping
    public ResponseEntity<RestaurantResponseDTO> createRestaurant(
            @RequestBody RestaurantCreateUpdateRequestDTO request,
            @AuthenticationPrincipal User user
    ){

        RestaurantCreateUpdateRequest restaurantCreateUpdateRequest = restaurantMapper.fromDTO(request);
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurantCreateUpdateRequest, user);
        return ResponseEntity.ok(restaurantMapper.toDTO(createdRestaurant));

    }


    @GetMapping
    public Page<RestaurantSummaryDTO> searchRestaurants(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Float minRating,
            @RequestParam(required = false) Float latitude,
            @RequestParam(required = false) Float longitude,
            @RequestParam(required = false) Float radius,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ){

        Page<Restaurant> result = restaurantService.searchRestaurant(
                q, minRating, latitude, longitude,
                radius, PageRequest.of(page - 1, size));

        return result.map(restaurant -> {
            RestaurantSummaryDTO restaurantSummaryDTO = restaurantMapper.toRestaurantSummaryDTO(restaurant);
            restaurantSummaryDTO.setWrittenBy(userMapper.toDTO(restaurant.getCreatedBy()));
            restaurantService.populateAuthorInReviews(restaurantSummaryDTO.getReviews(), restaurant);
            return restaurantSummaryDTO;
        });
    }

    @GetMapping(path = "/{restaurant_id}")
    public ResponseEntity<RestaurantSummaryDTO> getRestaurant(@PathVariable("restaurant_id") String restaurantId){
        RestaurantSummaryDTO restaurantSummaryDTO = restaurantService.getRestaurantSummaryDTO(restaurantId);
        return ResponseEntity.ok(restaurantSummaryDTO);
    }



    @PutMapping(path = "/{restaurant_id}")
    public ResponseEntity<RestaurantResponseDTO> updateRestaurant(@PathVariable("restaurant_id") String restaurantId,
                                                                  @RequestBody RestaurantCreateUpdateRequestDTO  requestDTO){

        RestaurantCreateUpdateRequest restaurantCreateUpdateRequest = restaurantMapper.fromDTO(requestDTO);
        Restaurant updatedRestaurant = restaurantService.updateRestaurant(restaurantId, restaurantCreateUpdateRequest);

        return ResponseEntity.ok(restaurantMapper.toDTO(updatedRestaurant));
    }

    @DeleteMapping(path = "/{restaurant_id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable("restaurant_id") String restaurantId){
        restaurantService.deleteRestaurant(restaurantId);
        return ResponseEntity.noContent().build();
    }

}
