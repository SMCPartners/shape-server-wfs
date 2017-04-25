package com.smcpartners.shape.shapeserver.shared.dto.shape;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

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
public class ProviderDTO implements Serializable {
    private int id;
    private String name;
    private boolean active;
    private int npi;
    private String createdBy;
    private Date modifiedDt;
    private String modifiedBy;
    private int organizationId;
}
