package com.smcpartners.shape.shapeserver.shared.dto.shape.request;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Responsible:<br/>
 * 1. DTO
 * <p>
 * Created by johndestefano on 10/21/15.
 * <p>
 * Changes:<b/>
 */
@Data
@NoArgsConstructor
public class LoginRequestDTO extends AbstractBaseRequestDTO {
    private String password;
}
