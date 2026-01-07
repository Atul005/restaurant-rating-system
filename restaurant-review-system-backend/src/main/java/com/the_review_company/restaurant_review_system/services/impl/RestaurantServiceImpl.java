package com.the_review_company.restaurant_review_system.services.impl;

import com.the_review_company.restaurant_review_system.domain.DTOs.RestaurantSummaryDTO;
import com.the_review_company.restaurant_review_system.domain.DTOs.ReviewResponseDTO;
import com.the_review_company.restaurant_review_system.domain.DTOs.UserDTO;
import com.the_review_company.restaurant_review_system.domain.GeoLocation;
import com.the_review_company.restaurant_review_system.domain.RestaurantCreateUpdateRequest;
import com.the_review_company.restaurant_review_system.domain.entities.*;
import com.the_review_company.restaurant_review_system.exceptions.RestaurantNotFoundException;
import com.the_review_company.restaurant_review_system.mapper.RestaurantMapper;
import com.the_review_company.restaurant_review_system.mapper.UserMapper;
import com.the_review_company.restaurant_review_system.repositories.RestaurantRepository;
import com.the_review_company.restaurant_review_system.services.GeoLocationService;
import com.the_review_company.restaurant_review_system.services.RestaurantService;
import com.the_review_company.restaurant_review_system.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final GeoLocationService geoLocationService;
    private final RestaurantMapper restaurantMapper;
    private final UserMapper userMapper;
    private final UserService userService;

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
    public RestaurantSummaryDTO getRestaurantSummaryDTO(String restaurantId) {
        Restaurant restaurant = getRestaurant(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("No such restaurant found with id " + restaurantId));
        return buildRestaurantSummary(restaurant);
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

    private RestaurantSummaryDTO buildRestaurantSummary(Restaurant restaurant) {
        RestaurantSummaryDTO restaurantSummaryDTO = restaurantMapper.toRestaurantSummaryDTO(restaurant);
        User createdBy = restaurant.getCreatedBy();
        UserDTO userDTO = userMapper.toDTO(createdBy);
        restaurantSummaryDTO.setWrittenBy(userDTO);
        List<ReviewResponseDTO> reviews = restaurantSummaryDTO.getReviews();
        populateAuthorInReviews(reviews, restaurant);
        return restaurantSummaryDTO;
    }

    public void populateAuthorInReviews(List<ReviewResponseDTO> reviews, Restaurant restaurant) {
        Map<String, ReviewResponseDTO> idToReviewResponseDTOMAP = reviews.stream()
                .collect(
                        Collectors
                                .toMap(ReviewResponseDTO::getId,
                                        r -> r, (a, b) -> b));

        Map<String, Review> idToReviewMap = restaurant.getReviews()
                .stream()
                .collect(
                        Collectors
                                .toMap(Review::getId,
                                        r -> r, (a, b) -> b));

        Set<String> userIds = restaurant.getReviews().stream().map(Review::getUserID).collect(Collectors.toSet());
        Map<String, UserDTO> userDTOMap = userService.fetchUsers(userIds)
                .stream()
                .collect(Collectors.toMap(User::getId, userMapper::toDTO));

        idToReviewResponseDTOMAP.keySet().forEach(key -> {
            Review review = idToReviewMap.get(key);
            UserDTO writtenBy = review != null ? userDTOMap.getOrDefault(review.getUserID(), userService.getDummyUserDTO()) : userService.getDummyUserDTO();
            idToReviewResponseDTOMAP.get(key).setWrittenBy(writtenBy);
        });
    }


}
