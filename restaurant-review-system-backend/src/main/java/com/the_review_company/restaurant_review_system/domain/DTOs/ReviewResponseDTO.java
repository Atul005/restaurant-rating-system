package com.the_review_company.restaurant_review_system.domain.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponseDTO {

    private String id;
    private String content;
    private Integer rating;
    private List<PhotoDTO> photos = new ArrayList<>();
    private LocalDateTime datePosted;
    private LocalDateTime lastEdited;
    private UserDTO writtenBy;
    private String restaurantId;

}
