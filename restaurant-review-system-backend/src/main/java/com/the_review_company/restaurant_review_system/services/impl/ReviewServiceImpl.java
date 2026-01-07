package com.the_review_company.restaurant_review_system.services.impl;

import com.the_review_company.restaurant_review_system.domain.DTOs.ReviewCreateUpdateRequest;
import com.the_review_company.restaurant_review_system.domain.entities.Photo;
import com.the_review_company.restaurant_review_system.domain.entities.Restaurant;
import com.the_review_company.restaurant_review_system.domain.entities.Review;
import com.the_review_company.restaurant_review_system.domain.entities.User;
import com.the_review_company.restaurant_review_system.exceptions.RestaurantNotFoundException;
import com.the_review_company.restaurant_review_system.exceptions.ReviewNotAllowedException;
import com.the_review_company.restaurant_review_system.exceptions.ReviewNotFoundException;
import com.the_review_company.restaurant_review_system.repositories.RestaurantRepository;
import com.the_review_company.restaurant_review_system.repositories.ReviewRepository;
import com.the_review_company.restaurant_review_system.services.RestaurantService;
import com.the_review_company.restaurant_review_system.services.ReviewService;
import com.the_review_company.restaurant_review_system.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final UserService userService;
    private final ReviewRepository reviewRepository;
    private final RestaurantService restaurantService;
    private final RestaurantRepository restaurantRepository;


    @Override
    public Review createReview(User author, String restaurantId, ReviewCreateUpdateRequest reviewRequest) {
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id " + restaurantId + " not found"));

        boolean hasExistingReview = restaurant.getReviews().stream()
                .anyMatch(review -> review.getUserID().equals(author.getId()));

        if(hasExistingReview){
            throw new ReviewNotAllowedException("User has already reviewed the restaurant");
        }

        LocalDateTime nowTime = LocalDateTime.now();

        List<Photo> photoList = extractPhotosFromReview(reviewRequest, nowTime);

        Review createdReview = buildReview(author, restaurantId, reviewRequest, photoList, nowTime);

        updateReviewToRestaurant(restaurant, createdReview, reviewRequest.getRating());
        reviewRepository.save(createdReview);
        return createdReview;
    }

    @Override
    public Page<Review> listReviews(String restaurantId, Pageable pageable) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("No restaurant found with this id " + restaurantId));

        List<Review> restaurantReviews = restaurant.getReviews();

        Sort sort = pageable.getSort();
        if(sort.isSorted()){
            Sort.Order order = sort.iterator().next();
            String property = order.getProperty();
            boolean isAscending = order.getDirection().isAscending();

            Comparator<Review> reviewComparator = switch (property) {
                case "data_posted" -> Comparator.comparing(Review::getDatePosted);
                case "rating" -> Comparator.comparing(Review::getRating);
                default -> Comparator.comparing(Review::getDatePosted);
            };
            restaurantReviews.sort(isAscending ? reviewComparator : reviewComparator.reversed());
        } else {
            restaurantReviews.sort(Comparator.comparing(Review::getDatePosted).reversed());
        }

        int start = (int)pageable.getOffset();
        if(start >= restaurantReviews.size()){
            return new PageImpl<>(Collections.emptyList(), pageable, restaurantReviews.size());
        }

        int end = Math.min(start + pageable.getPageSize(), restaurantReviews.size());

        return new PageImpl<>(restaurantReviews.subList(start, end), pageable, restaurantReviews.size());
    }

    @Override
    public Optional<Review> getReview(String restaurantId, String reviewId) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);
        return getReviewFromRestaurant(reviewId, restaurant);
    }



    @Override
    public Review updateReview(User author, String restaurantId, String reviewId, ReviewCreateUpdateRequest updateRequest) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);
        Review existingReview = getReviewFromRestaurant(reviewId, restaurant)
                .orElseThrow(() -> new ReviewNotFoundException("No such review found with id " + reviewId));
        if(!author.getId().equals(existingReview.getUserID())){
            throw new ReviewNotAllowedException("Cannot update another user's review");
        }
        if(LocalDateTime.now().isAfter(existingReview.getDatePosted().plusHours(48))){
            throw new ReviewNotAllowedException("Review cannot be updated after 48 hours");
        }

        existingReview.setContent(updateRequest.getContent());
        existingReview.setRating(updateRequest.getRating());
        existingReview.setLastEdited(LocalDateTime.now());
        existingReview.setPhotos(updateRequest.getPhotoIds()
                .stream()
                .map(
                        photoId -> {
                            return Photo.builder().url(photoId).uploadDate(LocalDateTime.now()).build();
                        }
                        ).toList()
        );
        List<Review> reviewList = new ArrayList<>(restaurant.getReviews()
                .stream()
                .filter(review -> !review.getId().equals(existingReview.getId()))
                .toList());
        reviewList.add(existingReview);
        restaurant.setReviews(reviewList);
        updateRestaurantAverageRatings(reviewList);
        restaurantRepository.save(restaurant);
        return existingReview;
    }

    @Override
    public void deleteReview(User author, String restaurantId, String reviewId) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);
        Review existingReview = restaurant.getReviews()
                .stream()
                .filter(
                        review ->
                                review.getId().equals(reviewId))
                .findFirst()
                .orElseThrow(() -> new ReviewNotFoundException("No such review found with id " + reviewId));
        if(!existingReview.getUserID().equals(author.getId())){
            throw new ReviewNotAllowedException("Cannot delete another user's review");
        }
        restaurant.getReviews().remove(existingReview);
        updateRestaurantAverageRatings(restaurant.getReviews());
        restaurantRepository.save(restaurant);
    }

    private static List<Photo> extractPhotosFromReview(ReviewCreateUpdateRequest reviewRequest, LocalDateTime nowTime) {
        return reviewRequest.getPhotoIds().stream().map(photoUrl -> {
            return Photo.builder()
                    .url(photoUrl)
                    .uploadDate(nowTime)
                    .build();
        }).toList();
    }

    private Review buildReview(User author, String restaurantId, ReviewCreateUpdateRequest reviewRequest, List<Photo> photoList, LocalDateTime nowTime) {
        return Review.builder()
                .id(UUID.randomUUID().toString())
                .photos(photoList)
                .content(reviewRequest.getContent())
                .rating(reviewRequest.getRating())
                .datePosted(nowTime)
                .lastEdited(nowTime)
                .userID(author.getId())
                .restaurantId(restaurantId)
                .build();
    }

    private void updateReviewToRestaurant(Restaurant restaurant, Review createdReview, Integer rating) {

        List<Review> restaurantReviews = restaurant.getReviews();
        if(null == restaurantReviews){
            restaurantReviews = new ArrayList<>();
            restaurant.setAverageRatings(0.0f);
        }
        restaurantReviews.add(createdReview);
        double ratings = updateRestaurantAverageRatings(restaurantReviews);
        restaurant.setAverageRatings((float)ratings);
        restaurantRepository.save(restaurant);
    }

    private static double updateRestaurantAverageRatings(List<Review> restaurantReviews) {
        return restaurantReviews.stream().mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }

    private Restaurant getRestaurantOrThrow(String restaurantId) {
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("No such restaurant found with this id " + restaurantId));
        return restaurant;
    }

    private static Optional<Review> getReviewFromRestaurant(String reviewId, Restaurant restaurant) {
        return restaurant.getReviews()
                .stream()
                .filter(
                        review -> review.getId().equals(reviewId))
                .findFirst();
    }

}
