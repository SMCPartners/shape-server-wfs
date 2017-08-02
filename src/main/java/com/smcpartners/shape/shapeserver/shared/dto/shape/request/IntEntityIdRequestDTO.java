package com.smcpartners.shape.shapeserver.shared.dto.shape.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Responsible: DTO<br/>
 * 1. </br>
 * <p>
 * Created by johndestefano on 11/4/15.
 * <p>
 * Changes:<br/>
 */
@Data
@NoArgsConstructor
public class IntEntityIdRequestDTO implements Serializable {
    private int entId;
}
