package com.the_review_company.restaurant_review_system.services;

import com.the_review_company.restaurant_review_system.domain.RestaurantCreateUpdateRequest;
import com.the_review_company.restaurant_review_system.domain.entities.Restaurant;
import com.the_review_company.restaurant_review_system.domain.entities.User;

public interface RestaurantService {
    Restaurant createRestaurant(RestaurantCreateUpdateRequest request, User user);
}
