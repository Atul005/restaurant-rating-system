package com.the_review_company.restaurant_review_system.services;

import com.the_review_company.restaurant_review_system.domain.DTOs.ReviewCreateUpdateRequest;
import com.the_review_company.restaurant_review_system.domain.DTOs.ReviewCreateUpdateRequestDTO;
import com.the_review_company.restaurant_review_system.domain.entities.Review;
import com.the_review_company.restaurant_review_system.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface ReviewService {

    Review createReview(User author, String restaurantId, ReviewCreateUpdateRequest reviewRequest);

    Page<Review> listReviews(String restaurantId, Pageable pageable);

    Optional<Review> getReview(String restaurantId, String reviewId);

    Review updateReview(User author, String restaurantId, String reviewId, ReviewCreateUpdateRequest updateRequest);

    void deleteReview(User author, String restaurantId, String reviewId);

}
