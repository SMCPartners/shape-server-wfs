package com.smcpartners.shape.shapeserver.shared.dto.shape;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Responsible:</br>
 * 1. DTO </br>
 * <p>
 * Created by johndestefano on 10/28/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Data
@NoArgsConstructor
public class MeasureDTO implements Serializable {
    private int id;
    private String name;
    private String description;
    private String nqfId;
    private String numeratorDescription;
    private String denominatorDescription;
    private String exclusionsDescription;
    private boolean wellControlledNumerator;
    private boolean selected;
    private boolean active;
}
