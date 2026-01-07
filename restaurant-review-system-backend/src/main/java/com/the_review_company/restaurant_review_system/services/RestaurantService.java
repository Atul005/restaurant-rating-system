package com.the_review_company.restaurant_review_system.services;

import com.the_review_company.restaurant_review_system.domain.DTOs.RestaurantSummaryDTO;
import com.the_review_company.restaurant_review_system.domain.DTOs.ReviewResponseDTO;
import com.the_review_company.restaurant_review_system.domain.RestaurantCreateUpdateRequest;
import com.the_review_company.restaurant_review_system.domain.entities.Restaurant;
import com.the_review_company.restaurant_review_system.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {
    Restaurant createRestaurant(RestaurantCreateUpdateRequest request, User user);

    Page<Restaurant> searchRestaurant(
            String query,
            Float minRating,
            Float latitude,
            Float longitude,
            Float radius,
            Pageable pageable
    );

    Optional<Restaurant> getRestaurant(String id);

    RestaurantSummaryDTO getRestaurantSummaryDTO(String restaurantId);

    Restaurant updateRestaurant(String id, RestaurantCreateUpdateRequest request);

    void deleteRestaurant(String id);

    void populateAuthorInReviews(List<ReviewResponseDTO> reviews, Restaurant restaurant);
}
