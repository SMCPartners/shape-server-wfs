package com.smcpartners.shape.shapeserver.shared.dto.shape.response;

import com.smcpartners.shape.shapeserver.shared.dto.shape.request.AbstractBaseRequestDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Responsible: Returns a password question for a user that the
 * user previously answered<br/>
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
public class UserPasswordQuestionResponseDTO extends AbstractBaseRequestDTO {
    private String passwordQuestion;

    public UserPasswordQuestionResponseDTO(String userId, String passwordQuestion) {
        this.passwordQuestion = passwordQuestion;
        this.setUserId(userId);
    }
}
