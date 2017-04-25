package com.smcpartners.shape.shapeserver.shared.dto.shape;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Responsible:</br>
 * 1.  DTO</br>
 * <p>
 * Created by johndestefano on 10/28/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Data
@NoArgsConstructor
public class OrganizationDTO implements Serializable {
    private int id;
    private String name;
    private boolean active;
    private String addressStreet;
    private String addressState;
    private String addressCity;
    private String addressZip;
    private String phone;
    private String primaryContactName;
    private String primaryContactEmail;
    private String primaryContactRole;
    private String primaryContactPhone;
    private String createdBy;
    private Date modifiedDt;
    private String modifiedBy;
}
