package com.smcpartners.shape.shapeserver.shared.dto.shape.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Responsible:</br>
 * 1.  DTO</br>
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
public class ListViewDTO {
    private String nqfId;
    private String name;
    private String desciption;
    private int numeratorValue;
    private int denominatorValue;
    private List<List<Object>> chartData;
    private ChartOptionsDTO chartOptions;
}
