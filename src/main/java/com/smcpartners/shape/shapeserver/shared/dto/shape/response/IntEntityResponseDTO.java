package com.smcpartners.shape.shapeserver.shared.dto.shape.response;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Responsible:<br/>
 * 1. DTO
 * <p>
 * Created by johndestefano on 11/5/15.
 * <p>
 * Changes:<b/>
 */
@Data
@NoArgsConstructor
public class IntEntityResponseDTO {
    private int entId;

    public static IntEntityResponseDTO makeNew(int id) {
        IntEntityResponseDTO dto = new IntEntityResponseDTO();
        dto.setEntId(id);
        return dto;
    }
}
