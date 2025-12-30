package com.the_review_company.restaurant_review_system.domain.DTOs;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDTO {

    @NotBlank(message = "Street number is required.")
    private String streetNumber;

    @NotBlank(message = "Street number is required.")
    private String streetName;

    @NotBlank(message = "City is required.")
    private String city;

    private String unit;

    @NotBlank(message = "State is required.")
    private String state;

    @NotBlank(message = "PostalCode is required.")
    private String postalCode;

    @NotBlank(message = "Country is required.")
    private String country;
    
}
