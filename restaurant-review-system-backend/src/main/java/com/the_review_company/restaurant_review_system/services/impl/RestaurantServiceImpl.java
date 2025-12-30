package com.the_review_company.restaurant_review_system.services.impl;

import com.the_review_company.restaurant_review_system.domain.GeoLocation;
import com.the_review_company.restaurant_review_system.domain.RestaurantCreateUpdateRequest;
import com.the_review_company.restaurant_review_system.domain.entities.Address;
import com.the_review_company.restaurant_review_system.domain.entities.Photo;
import com.the_review_company.restaurant_review_system.domain.entities.Restaurant;
import com.the_review_company.restaurant_review_system.domain.entities.User;
import com.the_review_company.restaurant_review_system.repositories.RestaurantRepository;
import com.the_review_company.restaurant_review_system.services.GeoLocationService;
import com.the_review_company.restaurant_review_system.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final GeoLocationService geoLocationService;

    @Override
    public Restaurant createRestaurant(RestaurantCreateUpdateRequest request, User user) {

        Address address = request.getAddress();
        GeoLocation restaurantGeolocation = geoLocationService.geoLocate(address);
        List<Photo> photoList = request.getPhotosIds()
                .stream()
                .map(
                        photoUrl -> Photo.builder()
                                .url(photoUrl)
                                .uploadDate(LocalDateTime.now())
                                .build()
                ).toList();
        Restaurant restaurant = Restaurant.builder()
                .address(address)
                .name(request.getName())
                .contactInfo(request.getContactInformation())
                .cuisineType(request.getCuisineType())
                .operatingHours(request.getOperatingHours())
                .photos(photoList)
                .averageRatings(0f)
                .createdBy(user)
                .build();
        return restaurantRepository.save(restaurant);

    }
}
