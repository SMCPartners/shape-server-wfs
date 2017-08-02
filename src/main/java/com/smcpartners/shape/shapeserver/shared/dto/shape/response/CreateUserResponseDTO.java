package com.smcpartners.shape.shapeserver.shared.dto.shape.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Responsible: DTO<br/>
 * 1.
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

    public CreateUserResponseDTO(String id) {
        this.id = id;
    }
}
