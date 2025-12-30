package com.the_review_company.restaurant_review_system.mapper;

import com.the_review_company.restaurant_review_system.domain.DTOs.PhotoDTO;
import com.the_review_company.restaurant_review_system.domain.entities.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Component
public interface PhotoMapper {

    PhotoDTO toDTO(Photo photo);

}
