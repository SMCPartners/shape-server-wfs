package com.smcpartners.shape.shapeserver.shared.dto.shape.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Responsible:<br/>
 * 1. DTO
 * <p>
 * Created by johndestefano on 11/4/15.
 * <p>
 * Changes:<b/>
 */
@Data
@NoArgsConstructor
public class IntEntityIdRequestDTO implements Serializable {
    private int entId;
}
