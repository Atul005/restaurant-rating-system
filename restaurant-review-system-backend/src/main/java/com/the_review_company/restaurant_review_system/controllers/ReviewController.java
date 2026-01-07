package com.the_review_company.restaurant_review_system.controllers;


import com.the_review_company.restaurant_review_system.domain.DTOs.ReviewCreateUpdateRequest;
import com.the_review_company.restaurant_review_system.domain.DTOs.ReviewCreateUpdateRequestDTO;
import com.the_review_company.restaurant_review_system.domain.DTOs.ReviewResponseDTO;
import com.the_review_company.restaurant_review_system.domain.DTOs.UserDTO;
import com.the_review_company.restaurant_review_system.domain.entities.Review;
import com.the_review_company.restaurant_review_system.domain.entities.User;
import com.the_review_company.restaurant_review_system.mapper.ReviewMapper;
import com.the_review_company.restaurant_review_system.mapper.UserMapper;
import com.the_review_company.restaurant_review_system.services.ReviewService;
import com.the_review_company.restaurant_review_system.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(path = "/api/restaurants/{restaurantId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewMapper reviewMapper;
    private final ReviewService reviewService;
    private final UserMapper userMapper;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createReview(
            @Valid  @RequestBody ReviewCreateUpdateRequestDTO reviewCreateUpdateRequestDTO,
            @PathVariable String restaurantId,
            @AuthenticationPrincipal User user
            ){

        ReviewCreateUpdateRequest reviewCreateUpdateRequest = reviewMapper.fromDTO(reviewCreateUpdateRequestDTO);
        Review createdReview = reviewService.createReview(user, restaurantId, reviewCreateUpdateRequest);
        ReviewResponseDTO reviewResponseDTO = reviewMapper.toDTO(createdReview);
        User author = userService.fetchUser(createdReview.getUserID());
        UserDTO userDTO = author != null ? userMapper.toDTO(author) : null;
        reviewResponseDTO.setWrittenBy(userDTO);
        return ResponseEntity.ok(reviewResponseDTO);
    }


    @GetMapping
    public Page<ReviewResponseDTO> listReviews(
            @PathVariable String restaurantId,
            @PageableDefault(
                    size=20,
                    page=0,
                    sort="datePosted",
                    direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable
    ){

        return reviewService.listReviews(restaurantId, pageable).map(
                review -> {
                    User author = userService.fetchUser(review.getUserID());
                    UserDTO userDTO = author != null ? userMapper.toDTO(author) : null;
                    ReviewResponseDTO dto = reviewMapper.toDTO(review);
                    dto.setWrittenBy(userDTO);
                    return dto;
                });
    }

    @GetMapping(path = "/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> getReview(
            @PathVariable("restaurantId") String restaurantId,
            @PathVariable("reviewId") String reviewId
    ){
        return reviewService.getReview(restaurantId, reviewId)
                .map(reviewMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PutMapping(path = "/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> getReview(
            @PathVariable("restaurantId") String restaurantId,
            @PathVariable("reviewId") String reviewId,
            @Valid @RequestBody ReviewCreateUpdateRequestDTO reviewCreateUpdateRequestDTO,
            @AuthenticationPrincipal User user
    ){
        ReviewCreateUpdateRequest reviewCreateUpdateRequest = reviewMapper.fromDTO(reviewCreateUpdateRequestDTO);
        Review updatedReview = reviewService.updateReview(user, restaurantId, reviewId, reviewCreateUpdateRequest);
        return ResponseEntity.ok(reviewMapper.toDTO(updatedReview));
    }


    @DeleteMapping(path = "/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> getReview(
            @PathVariable("restaurantId") String restaurantId,
            @PathVariable("reviewId") String reviewId,
            @AuthenticationPrincipal User user
    ){
        reviewService.deleteReview(user, restaurantId, reviewId);
        return ResponseEntity.noContent().build();
    }



}
