package com.the_review_company.restaurant_review_system.mapper;

import com.the_review_company.restaurant_review_system.domain.DTOs.GeoPointDTO;
import com.the_review_company.restaurant_review_system.domain.DTOs.PhotoDTO;
import com.the_review_company.restaurant_review_system.domain.DTOs.RestaurantCreateUpdateRequestDTO;
import com.the_review_company.restaurant_review_system.domain.DTOs.RestaurantResponseDTO;
import com.the_review_company.restaurant_review_system.domain.RestaurantCreateUpdateRequest;
import com.the_review_company.restaurant_review_system.domain.entities.Photo;
import com.the_review_company.restaurant_review_system.domain.entities.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Component
public interface RestaurantMapper {

    RestaurantCreateUpdateRequest fromDTO(RestaurantCreateUpdateRequestDTO dto);

    @Mapping(target = "geoLocation", source = "geoLocation", qualifiedByName = "mapGeoPoint")
    RestaurantResponseDTO toDTO(Restaurant restaurant);

    @Named("mapGeoPoint")
    @Mapping(target = "lat", expression = "java(geoPoint.getLat())")
    @Mapping(target = "lon", expression = "java(geoPoint.getLon())")
    GeoPointDTO mapGeoPoint(GeoPoint geoPoint);

}
