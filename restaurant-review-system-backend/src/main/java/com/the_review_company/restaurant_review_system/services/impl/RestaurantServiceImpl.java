package com.the_review_company.restaurant_review_system.services.impl;

import com.the_review_company.restaurant_review_system.domain.GeoLocation;
import com.the_review_company.restaurant_review_system.domain.RestaurantCreateUpdateRequest;
import com.the_review_company.restaurant_review_system.domain.entities.Address;
import com.the_review_company.restaurant_review_system.domain.entities.Photo;
import com.the_review_company.restaurant_review_system.domain.entities.Restaurant;
import com.the_review_company.restaurant_review_system.domain.entities.User;
import com.the_review_company.restaurant_review_system.exceptions.RestaurantNotFoundException;
import com.the_review_company.restaurant_review_system.repositories.RestaurantRepository;
import com.the_review_company.restaurant_review_system.services.GeoLocationService;
import com.the_review_company.restaurant_review_system.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final GeoLocationService geoLocationService;

    @Override
    public Restaurant createRestaurant(RestaurantCreateUpdateRequest request, User user) {

        Address address = request.getAddress();
        GeoLocation restaurantGeolocation = geoLocationService.geoLocate(address);
        List<Photo> photoList = request.getPhotoIds()
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
                .geoLocation(new GeoPoint(restaurantGeolocation.getLatitude(), restaurantGeolocation.getLongitude()))
                .build();
        return restaurantRepository.save(restaurant);

    }

    @Override
    public Page<Restaurant> searchRestaurant(String query, Float minRating, Float latitude,
                                             Float longitude, Float radius, Pageable pageable) {

        if(null != minRating && (query == null || query.isEmpty())){
            return restaurantRepository.findByAverageRatingsGreaterThanEqual(minRating, pageable);
        }

        Float searchMinRating = null == minRating ? 0f : minRating;

        if(null != query && !query.trim().isEmpty()){
            return restaurantRepository.findByQueryAndMinRating(query, searchMinRating, pageable);
        }

        if(null != latitude && null != longitude && null != radius){
            return restaurantRepository.findByLocationNear(latitude, longitude, radius, pageable);
        }

        return restaurantRepository.findAll(pageable);

    }

    @Override
    public Optional<Restaurant> getRestaurant(String id) {
        return restaurantRepository.findById(id);
    }

    @Override
    public Restaurant updateRestaurant(String id, RestaurantCreateUpdateRequest request) {
        Restaurant restaurant = getRestaurant(id)
                .orElseThrow(() ->
                        new RestaurantNotFoundException("Restaurant with id " + id + " not found!!!"));

        GeoLocation geoLocation = geoLocationService.geoLocate(request.getAddress());
        GeoPoint newGeoPoint = new GeoPoint(geoLocation.getLatitude(), geoLocation.getLongitude());

        List<Photo> photos = request.getPhotoIds().stream()
                .map(photoUrl ->
                        Photo.builder()
                                .url(photoUrl)
                                .uploadDate(LocalDateTime.now())
                                .build())
                .toList();


        restaurant.setGeoLocation(newGeoPoint);
        restaurant.setPhotos(photos);
        restaurant.setAddress(request.getAddress());
        restaurant.setCuisineType(request.getCuisineType());
        restaurant.setName(request.getName());
        restaurant.setContactInfo(request.getContactInformation());
        restaurant.setOperatingHours(request.getOperatingHours());

        return restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(String id) {
        restaurantRepository.deleteById(id);
    }
}
