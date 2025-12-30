package com.the_review_company.restaurant_review_system.services.impl;

import com.the_review_company.restaurant_review_system.domain.GeoLocation;
import com.the_review_company.restaurant_review_system.domain.entities.Address;
import com.the_review_company.restaurant_review_system.services.GeoLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RandomLondonGeoLocationServiceImpl implements GeoLocationService {

//    Bounding box features of London
    private static final float MIN_LATITUDE = 51.28f;
    private static final float MAX_LATITUDE = 51.68f;
    private static final float MIN_LONGITUDE = -0.489f;
    private static final float MAX_LONGITUDE = 0.236;

// Bounding box coorindates for India, enhance this feature as per India
//    private static final float MIN_LATITUDE = 7.96553477623f;
//    private static final float MAX_LATITUDE = 35.4940095078;
//    private static final float MIN_LONGITUDE = 68.1766451354f;
//    private static final float MIN_LONGITUDE = 97.4025614766;


    @Override
    public GeoLocation geoLocate(Address address) {
        Random randomLocation = new Random();

        double latitude = MIN_LATITUDE + randomLocation.nextDouble(MAX_LATITUDE - MIN_LATITUDE);
        double longitude = MIN_LONGITUDE + randomLocation.nextDouble(MAX_LONGITUDE - MIN_LONGITUDE);
        return GeoLocation.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
