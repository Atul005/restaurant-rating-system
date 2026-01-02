package com.the_review_company.restaurant_review_system.domain.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantSummaryDTO {

    private String id;

    private String name;

    private String cuisineType;

    private String contactInfo;

    private Float averageRatings;

    private GeoPointDTO geoLocation;

    private AddressDTO address;

    private OperatingHoursDTO operatingHours;

    private List<PhotoDTO> photos = new ArrayList<>();

    private Integer totalReviews;

    private List<ReviewDTO> reviews = new ArrayList<>();

}
