package com.smcpartners.shape.shapeserver.shared.dto.shape.request;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Responsibility: DTO</br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 6/20/17
 */
@Data
@NoArgsConstructor
public class ResetPasswordRequestDTO {
    private String userId;
    private String email;
    private String challengeQuestion;
    private String ChallengeQuestionResponse;
}
