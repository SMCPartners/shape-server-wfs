package com.smcpartners.shape.shapeserver.shared.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Responsible:</br>
 * 1. Holds an error message for return from a use case</br>
 * <p>
 * Created by johndestefano on 3/15/16.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMsgResponse implements Serializable {
    private int httpResponseCode;
    private String errMsg;
}
