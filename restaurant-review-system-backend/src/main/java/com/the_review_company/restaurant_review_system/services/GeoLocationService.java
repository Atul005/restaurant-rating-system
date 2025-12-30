package com.the_review_company.restaurant_review_system.services;

import com.the_review_company.restaurant_review_system.domain.GeoLocation;
import com.the_review_company.restaurant_review_system.domain.entities.Address;

public interface GeoLocationService {

    GeoLocation geoLocate(Address address);
}
