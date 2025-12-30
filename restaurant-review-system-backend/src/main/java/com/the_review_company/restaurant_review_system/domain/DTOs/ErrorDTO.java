package com.the_review_company.restaurant_review_system.domain.DTOs;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDTO {

    private Integer status;
    private String message;


}
