package com.smcpartners.shape.shapeserver.shared.dto.shape;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Responsible:</br>
 * 1. DTO </br>
 * <p>
 * Created by johndestefano on 10/29/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Data
@NoArgsConstructor
public class ProviderMeasureResponseDTO implements Serializable {
    private int id;
    private int numeratorValue;
    private int denominatorValue;
    private Date rpDate;
    private int measureByMeasureId;
    private int providerId;
}
