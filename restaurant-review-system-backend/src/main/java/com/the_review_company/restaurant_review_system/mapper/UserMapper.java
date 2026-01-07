package com.the_review_company.restaurant_review_system.mapper;

import com.the_review_company.restaurant_review_system.domain.DTOs.ReviewCreateUpdateRequest;
import com.the_review_company.restaurant_review_system.domain.DTOs.ReviewCreateUpdateRequestDTO;
import com.the_review_company.restaurant_review_system.domain.DTOs.ReviewResponseDTO;
import com.the_review_company.restaurant_review_system.domain.DTOs.UserDTO;
import com.the_review_company.restaurant_review_system.domain.entities.Review;
import com.the_review_company.restaurant_review_system.domain.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Component
public interface UserMapper {
    UserDTO toDTO(User user);
}
