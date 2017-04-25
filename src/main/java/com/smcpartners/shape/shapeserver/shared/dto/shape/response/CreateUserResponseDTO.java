package com.smcpartners.shape.shapeserver.shared.dto.shape.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Responsible:<br/>
 * 1. DTO
 * <p>
 * Created by johndestefano on 11/2/15.
 * <p>
 * Changes:<b/>
 */
@Data
@NoArgsConstructor
public class CreateUserResponseDTO implements Serializable {
    private String id;
    private String password;
}
