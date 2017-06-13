package com.smcpartners.shape.shapeserver.shared.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Responsibility: </br>
 * 1. Data holder for string with int value</br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 6/12/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NameDoubleValDTO {
    private String key;
    private double doubleVal;
}
