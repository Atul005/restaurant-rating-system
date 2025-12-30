package com.the_review_company.restaurant_review_system.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeoLocation {

    private double latitude;
    private double longitude;

}
