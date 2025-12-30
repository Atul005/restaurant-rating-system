package com.the_review_company.restaurant_review_system.domain.DTOs;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeRangeDTO {

    @NotBlank(message = "Opening time is required.")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]")
    private String openTime;

    @NotBlank(message = "Closing time is required.")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]")
    private String closeTime;

}
