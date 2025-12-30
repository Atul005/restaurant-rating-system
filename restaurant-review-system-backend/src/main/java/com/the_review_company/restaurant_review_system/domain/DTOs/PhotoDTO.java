package com.the_review_company.restaurant_review_system.domain.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhotoDTO {

    private String url;
    private LocalDateTime uploadDate;

}
