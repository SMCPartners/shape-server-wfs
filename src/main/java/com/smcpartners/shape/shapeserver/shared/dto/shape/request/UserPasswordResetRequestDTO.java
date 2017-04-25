package com.smcpartners.shape.shapeserver.shared.dto.shape.request;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Responsible: Sent when the user initiates a password reset<br/>
 * 1. <br/>
 * <p>
 * Created by johndestefano on 4/5/16.
 * </p>
 * <p>
 * Changes:<br/>
 * 1. <br/>
 * </p>
 */
@Data
@NoArgsConstructor
public class UserPasswordResetRequestDTO extends AbstractBaseRequestDTO {
    private String questionAnswer;
    private String newPassword;
    private String oldPassword;

    public UserPasswordResetRequestDTO(String userId, String questionAnswer, String newPassword, String oldPassword) {
        this.questionAnswer = questionAnswer;
        this.newPassword = newPassword;
        this.oldPassword = oldPassword;
        this.setUserId(userId);
    }
}
