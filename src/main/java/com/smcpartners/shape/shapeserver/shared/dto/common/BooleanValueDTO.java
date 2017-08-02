package com.smcpartners.shape.shapeserver.shared.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Responsible: Indicates a positive return from a use case</br>
 * <p>
 * Created by johndestefano on 10/6/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BooleanValueDTO implements Serializable {
    private boolean value;

    public static BooleanValueDTO get(boolean retVal) {
        return new BooleanValueDTO(retVal);
    }
}
