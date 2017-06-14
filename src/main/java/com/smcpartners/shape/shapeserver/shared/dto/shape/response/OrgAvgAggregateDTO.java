package com.smcpartners.shape.shapeserver.shared.dto.shape.response;

import com.smcpartners.shape.shapeserver.shared.dto.common.NameDoubleValDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Responsibility: </br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 6/12/17
 */
@Data
@NoArgsConstructor
public class OrgAvgAggregateDTO {
    private int orgId;
    private String orgName;
    private List<NameDoubleValDTO> measureYearAvgDTOS;
}
