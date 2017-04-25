package com.smcpartners.shape.shapeserver.shared.dto.shape;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
public class UserProviderDTO implements Serializable {
    private int id;
    private int providerId;
    private String userId;
}
