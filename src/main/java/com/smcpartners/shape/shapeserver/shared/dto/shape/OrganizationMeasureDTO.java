package com.smcpartners.shape.shapeserver.shared.dto.shape;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Responsible:</br>
 * 1.  DTO</br>
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
public class OrganizationMeasureDTO implements Serializable {
    private int id;
    private Integer numeratorValue;
    private Integer denominatorValue;
    private Date rpDate;
    private Integer genderMaleNum;
    private Integer genderMaleDen;
    private Integer genderFemaleNum;
    private Integer genderFemaleDen;
    private Integer age1844Num;
    private Integer age1844Den;
    private Integer age4564Num;
    private Integer age4564Den;
    private Integer ageOver65Num;
    private Integer ageOver65Den;
    private Integer ethnicityHispanicLatinoNum;
    private Integer ethnicityHispanicLatinoDen;
    private Integer ethnicityNotHispanicLatinoNum;
    private Integer ethnicityNotHispanicLatinoDen;
    private Integer raceAfricanAmericanNum;
    private Integer raceAfricanAmericanDen;
    private Integer raceAmericanIndianNum;
    private Integer raceAmericanIndianDen;
    private Integer raceAsianNum;
    private Integer raceAsianDen;
    private Integer raceNativeHawaiianNum;
    private Integer raceNativeHawaiianDen;
    private Integer raceWhiteNum;
    private Integer raceWhiteDen;
    private Integer raceOtherNum;
    private Integer raceOtherDen;
    private Integer reportPeriodYear;
    private Integer reportPeriodQuarter;
    private int organizationId;
    private int measureId;
    private String userId;
}
