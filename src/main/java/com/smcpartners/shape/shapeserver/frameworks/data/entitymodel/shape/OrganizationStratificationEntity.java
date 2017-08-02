package com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Responsible: Organization Stratification information</br>
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
@Table(name = "organization_stratification", schema = "", catalog = "shape")
@NamedQueries({
        @NamedQuery(name = "OrganizationStratification.findAll",
                query = "SELECT OBJECT(o) FROM OrganizationStratificationEntity o"),
        @NamedQuery(name = "OrganizationStratification.findAllByOrgId",
                query = "SELECT OBJECT(o) FROM OrganizationStratificationEntity o " +
                        "WHERE o.organizationByOrganizationId = :org")
})
@Data
@NoArgsConstructor
public class OrganizationStratificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    private int id;

    @Basic
    @Column(name = "gender_male", nullable = true, insertable = true, updatable = true)
    private Integer genderMale;

    @Basic
    @Column(name = "gender_female", nullable = true, insertable = true, updatable = true)
    private Integer genderFemale;

    @Basic
    @Column(name = "age_17_under", nullable = true, insertable = true, updatable = true, columnDefinition="INT(11) default 0")
    private Integer age17under;

    @Basic
    @Column(name = "age_18_44", nullable = true, insertable = true, updatable = true)
    private Integer age1844;

    @Basic
    @Column(name = "age_45_64", nullable = true, insertable = true, updatable = true)
    private Integer age4564;

    @Basic
    @Column(name = "age_over_65", nullable = true, insertable = true, updatable = true)
    private Integer ageOver65;

    @Basic
    @Column(name = "ethnicity_hispanic_latino", nullable = true, insertable = true, updatable = true)
    private Integer ethnicityHispanicLatino;

    @Basic
    @Column(name = "ethnicity_not_hispanic_latino", nullable = true, insertable = true, updatable = true)
    private Integer ethnicityNotHispanicLatino;

    @Basic
    @Column(name = "race_african_american", nullable = true, insertable = true, updatable = true)
    private Integer raceAfricanAmerican;

    @Basic
    @Column(name = "race_american_indian", nullable = true, insertable = true, updatable = true)
    private Integer raceAmericanIndian;

    @Basic
    @Column(name = "race_asian", nullable = true, insertable = true, updatable = true)
    private Integer raceAsian;

    @Basic
    @Column(name = "race_native_hawaiian", nullable = true, insertable = true, updatable = true)
    private Integer raceNativeHawaiian;

    @Basic
    @Column(name = "race_white", nullable = true, insertable = true, updatable = true)
    private Integer raceWhite;

    @Basic
    @Column(name = "race_other", nullable = true, insertable = true, updatable = true)
    private Integer raceOther;

    @Basic
    @Column(name = "total_patients", nullable = true, insertable = true, updatable = true)
    private Integer totalPatients;

    @Basic
    @Column(name = "total_visits", nullable = true, insertable = true, updatable = true)
    private Integer totalVisits;

    @Basic
    @Column(name = "patients_hypertension", nullable = true, insertable = true, updatable = true)
    private Integer patientsHypertension;

    @Basic
    @Column(name = "patients_diabetes", nullable = true, insertable = true, updatable = true)
    private Integer patientsDiabetes;

    @Basic
    @Column(name = "patients_pre_diabetes", nullable = true, insertable = true, updatable = true)
    private Integer patientsPreDiabetes;

    @Basic
    @Column(name = "patients_highBp", nullable = true, insertable = true, updatable = true)
    private Integer patientsHighBp;

    @Basic
    @Column(name = "primary_care_practice", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean primaryCarePractice;

    @Basic
    @Column(name = "fqhc_look_a_like", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean fqhcLookALike;

    @Basic
    @Column(name = "com_health_center", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean comHealthCenter;

    @Basic
    @Column(name = "multi_spec_practice", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean multiSpecPractice;

    @Basic
    @Column(name = "prac_consortium", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean pracConsortium;

    @Basic
    @Column(name = "ambulatory_clinic", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean ambulatoryClinic;

    @Basic
    @Column(name = "hmo", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean hmo;
    @Basic
    @Column(name = "aco", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean aco;

    @Basic
    @Column(name = "pcmh", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean pcmh;

    @Basic
    @Column(name = "other_org_descrip", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean otherOrgDescrip;

    @Basic
    @Column(name = "physicians", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean physicians;

    @Basic
    @Column(name = "nurse_prac", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean nursePrac;

    @Basic
    @Column(name = "rn", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean rn;

    @Basic
    @Column(name = "lpn", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean lpn;

    @Basic
    @Column(name = "pa", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean pa;

    @Basic
    @Column(name = "medical_assistant", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean medicalAssist;

    @Basic
    @Column(name = "residents", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean residents;

    @Basic
    @Column(name = "interns", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean interns;

    @Basic
    @Column(name = "community_health_workers", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean communityHealthWorkers;

    @Basic
    @Column(name = "trained_motivational_interview", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean trainedMotivationalInterview;

    @Basic
    @Column(name = "medicare_percent", nullable = true, insertable = true, updatable = true, columnDefinition = "INT(11) default 0")
    private Integer medicarePercent;

    @Basic
    @Column(name = "medicaid_percent", nullable = true, insertable = true, updatable = true, columnDefinition = "INT(11) default 0")
    private Integer medicaidPercent;

    @Basic
    @Column(name = "hmo_percent", nullable = true, insertable = true, updatable = true, columnDefinition = "INT(11) default 0")
    private Integer hmoPercent;

    @Basic
    @Column(name = "ppo_percent", nullable = true, insertable = true, updatable = true, columnDefinition = "INT(11) default 0")
    private Integer ppoPercent;

    @Basic
    @Column(name = "uninsured_self_percent", nullable = true, insertable = true, updatable = true, columnDefinition = "INT(11) default 0")
    private Integer uninsuredSelfPercent;

    @Basic
    @Column(name = "private_percent", nullable = true, insertable = true, updatable = true, columnDefinition = "INT(11) default 0")
    private Integer privatePercent;

    @Basic
    @Column(name = "vendor", nullable = true, insertable = true, updatable = true, length = 255, columnDefinition = "VARCHAR(50) default \'\'")
    private String vendor;

    @Basic
    @Column(name = "product", nullable = true, insertable = true, updatable = true, length = 255, columnDefinition = "VARCHAR(50) default \'\'")
    private String product;

    @Basic
    @Column(name = "ehr_version", nullable = true, insertable = true, updatable = true, length = 255, columnDefinition = "VARCHAR(50) default \'\'")
    private String versionEHR;

    @Basic
    @Column(name = "complete_CEHRT", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean completeCEHRT;

    @Basic
    @Column(name = "patient_portal", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean patientPortal;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "rpDate", nullable = false, insertable = true, updatable = true)
    private Date rpDate;

    @ManyToOne
    @JoinColumn(name = "organization_id", referencedColumnName = "id", nullable = false)
    private OrganizationEntity organizationByOrganizationId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity userByUserId;
}
