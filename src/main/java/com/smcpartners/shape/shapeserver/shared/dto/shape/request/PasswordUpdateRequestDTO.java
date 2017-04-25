package com.smcpartners.shape.shapeserver.shared.dto.shape.request;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Responsible:<br/>
 * 1. DTO
 * <p>
 * Created by johndestefano on 12/21/15.
 * <p>
 * Changes:<b/>
 */
@Data
@NoArgsConstructor
public class PasswordUpdateRequestDTO extends AbstractBaseRequestDTO {
    /**
     * New password
     */
    private String password;

    /**
     * User question
     */
    private String question;

    /**
     * User answer for above question
     */
    private String answer;
}
