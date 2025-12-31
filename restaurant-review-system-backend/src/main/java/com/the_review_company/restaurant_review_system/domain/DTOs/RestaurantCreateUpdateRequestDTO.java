package com.the_review_company.restaurant_review_system.domain.DTOs;

import com.the_review_company.restaurant_review_system.domain.entities.Address;
import com.the_review_company.restaurant_review_system.domain.entities.OperatingHours;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantCreateUpdateRequestDTO {

    @NotBlank(message = "Restaurant name is required.")
    private String name;

    @NotBlank(message = "Cuisine Type is required.")
    private String cuisineType;

    @NotBlank(message = "Contact information is required.")
    private String contactInformation;

    @Valid
    private AddressDTO address;

    @Valid
    private OperatingHoursDTO operatingHours;

    @Size(min = 1, message = "Atleast one photo is required")
    private List<String> photoIds;
}
