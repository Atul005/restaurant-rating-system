package com.the_review_company.restaurant_review_system.domain.DTOs;

import com.the_review_company.restaurant_review_system.domain.entities.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantResponseDTO {

    private String id;

    private String name;

    private String cuisineType;

    private String contactInfo;

    private Float averageRatings;

    private GeoPointDTO geoLocation;

    private AddressDTO address;

    private OperatingHoursDTO operatingHours;

    private List<PhotoDTO> photos = new ArrayList<>();

    private List<ReviewResponseDTO> reviews = new ArrayList<>();

    private UserDTO createdBy;

    private Integer totalReviews;
}
