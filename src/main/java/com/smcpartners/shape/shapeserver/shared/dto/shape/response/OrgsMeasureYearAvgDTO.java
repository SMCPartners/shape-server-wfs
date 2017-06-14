package com.smcpartners.shape.shapeserver.shared.dto.shape.response;

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
public class OrgsMeasureYearAvgDTO {
    private int measureId;
    private List<String> yearsSpanLst;
    private List<Double> aggAvgDataByYear;
    private List<OrgAvgAggregateDTO> orgsMeasureYearAvgDTOS;
}
