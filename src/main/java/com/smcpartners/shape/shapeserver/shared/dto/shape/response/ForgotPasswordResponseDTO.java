package com.smcpartners.shape.shapeserver.shared.dto.shape.response;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Responsibility: </br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 6/20/17
 */
@Data
@NoArgsConstructor
public class ForgotPasswordResponseDTO {
    private String userId;
    private String randomQuestion;
}
