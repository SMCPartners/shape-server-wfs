package com.smcpartners.shape.shapeserver.shared.dto.shape.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Responsible: Extended by many other DTOs
 * <p>
 * Created by johndestefano on 10/21/15.
 * <p>
 * Changes:<b/>
 */
@Data
@NoArgsConstructor
public abstract class AbstractBaseRequestDTO implements Serializable {
    private String userId;
}
