package com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Responsible:</br>
 * 1. Entity</br>
 * <p>
 * Created by johndestefano on 10/28/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Entity
@Table(name = "organization_measure", schema = "", catalog = "shape")
@NamedQueries({
        @NamedQuery(name = "OrganizationMeasure.findAll",
                query = "SELECT OBJECT(o) FROM OrganizationMeasureEntity o"),
        @NamedQuery(name = "OrganizationMeasure.findAllByOrgId",
                query = "SELECT OBJECT(o) FROM OrganizationMeasureEntity o " +
                        "WHERE o.organizationByOrganizationId = :org"),
        @NamedQuery(name = "OrganizationMeasure.findByMeasAndYear",
                query = "SElECT OBJECT(o) FROM OrganizationMeasureEntity o " +
                        "WHERE o.reportPeriodYear = :year AND o.measureByMeasureId = :meas"),
        @NamedQuery(name = "OrganizationMeasure.findByMeasYearOrg",
                query = "SElECT OBJECT(o) FROM OrganizationMeasureEntity o " +
                        "WHERE o.reportPeriodYear = :year AND o.measureByMeasureId = :meas " +
                        "AND o.organizationByOrganizationId = :org"),
        @NamedQuery(name = "OrganizationMeasure.avgByMeasureByYear",
                query = "SELECT om.reportPeriodYear, om.organizationByOrganizationId.id, om.organizationByOrganizationId.name, " +
                        "AVG(om.numeratorValue/om.denominatorValue) AS mAvg " +
                        "FROM OrganizationMeasureEntity om " +
                        "WHERE om.measureByMeasureId = :meas " +
                        "GROUP BY om.reportPeriodYear, om.organizationByOrganizationId.id, om.organizationByOrganizationId.name " +
                        "ORDER BY om.reportPeriodYear, om.organizationByOrganizationId.id, om.organizationByOrganizationId.name")
})
@Data
@NoArgsConstructor
public class OrganizationMeasureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    private int id;

    @Basic
    @Column(name = "numerator_value", nullable = true, insertable = true, updatable = true)
    private Integer numeratorValue;

    @Basic
    @Column(name = "denominator_value", nullable = true, insertable = true, updatable = true)
    private Integer denominatorValue;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "rpDate", nullable = false, insertable = true, updatable = true)
    private Date rpDate;

    @Basic
    @Column(name = "gender_male_num", nullable = true, insertable = true, updatable = true)
    private Integer genderMaleNum;

    @Basic
    @Column(name = "gender_male_den", nullable = true, insertable = true, updatable = true)
    private Integer genderMaleDen;

    @Basic
    @Column(name = "gender_female_num", nullable = true, insertable = true, updatable = true)
    private Integer genderFemaleNum;

    @Basic
    @Column(name = "gender_female_den", nullable = true, insertable = true, updatable = true)
    private Integer genderFemaleDen;

    @Basic
    @Column(name = "age_18_44_num", nullable = true, insertable = true, updatable = true)
    private Integer age1844Num;

    @Basic
    @Column(name = "age_18_44_den", nullable = true, insertable = true, updatable = true)
    private Integer age1844Den;

    @Basic
    @Column(name = "age_45_65_num", nullable = true, insertable = true, updatable = true)
    private Integer age4564Num;

    @Basic
    @Column(name = "age_45_65_den", nullable = true, insertable = true, updatable = true)
    private Integer age4564Den;

    @Basic
    @Column(name = "age_over_65_num", nullable = true, insertable = true, updatable = true)
    private Integer ageOver65Num;

    @Basic
    @Column(name = "age_over_65_den", nullable = true, insertable = true, updatable = true)
    private Integer ageOver65Den;

    @Basic
    @Column(name = "ethnicity_hispanic_latino_num", nullable = true, insertable = true, updatable = true)
    private Integer ethnicityHispanicLatinoNum;

    @Basic
    @Column(name = "ethnicity_hispanic_latino_den", nullable = true, insertable = true, updatable = true)
    private Integer ethnicityHispanicLatinoDen;

    @Basic
    @Column(name = "ethnicity_not_hispanic_latino_num", nullable = true, insertable = true, updatable = true)
    private Integer ethnicityNotHispanicLatinoNum;

    @Basic
    @Column(name = "ethnicity_not_hispanic_latino_den", nullable = true, insertable = true, updatable = true)
    private Integer ethnicityNotHispanicLatinoDen;

    @Basic
    @Column(name = "race_african_american_num", nullable = true, insertable = true, updatable = true)
    private Integer raceAfricanAmericanNum;

    @Basic
    @Column(name = "race_african_american_den", nullable = true, insertable = true, updatable = true)
    private Integer raceAfricanAmericanDen;

    @Basic
    @Column(name = "race_american_indian_num", nullable = true, insertable = true, updatable = true)
    private Integer raceAmericanIndianNum;

    @Basic
    @Column(name = "race_american_indian_den", nullable = true, insertable = true, updatable = true)
    private Integer raceAmericanIndianDen;

    @Basic
    @Column(name = "race_asian_num", nullable = true, insertable = true, updatable = true)
    private Integer raceAsianNum;

    @Basic
    @Column(name = "race_asian_den", nullable = true, insertable = true, updatable = true)
    private Integer raceAsianDen;

    @Basic
    @Column(name = "race_native_hawaiian_num", nullable = true, insertable = true, updatable = true)
    private Integer raceNativeHawaiianNum;

    @Basic
    @Column(name = "race_native_hawaiian_den", nullable = true, insertable = true, updatable = true)
    private Integer raceNativeHawaiianDen;

    @Basic
    @Column(name = "race_white_num", nullable = true, insertable = true, updatable = true)
    private Integer raceWhiteNum;

    @Basic
    @Column(name = "race_white_den", nullable = true, insertable = true, updatable = true)
    private Integer raceWhiteDen;

    @Basic
    @Column(name = "race_other_num", nullable = true, insertable = true, updatable = true)
    private Integer raceOtherNum;

    @Basic
    @Column(name = "race_other_den", nullable = true, insertable = true, updatable = true)
    private Integer raceOtherDen;

    @Basic
    @Column(name = "rep_per_qtr", nullable = true, insertable = true, updatable = true)
    private Integer reportPeriodQuarter;

    @Basic
    @Column(name = "rep_per_year", nullable = true, insertable = true, updatable = true)
    private Integer reportPeriodYear;

    @ManyToOne
    @JoinColumn(name = "organization_id", referencedColumnName = "id", nullable = false)
    private OrganizationEntity organizationByOrganizationId;

    @ManyToOne
    @JoinColumn(name = "measure_id", referencedColumnName = "id", nullable = false)
    private MeasureEntity measureByMeasureId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity userByUserId;
}
