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
public class OrganizationStratificationDTO implements Serializable {
    private int id;
    private int genderMale;
    private int genderFemale;
    private int age17under;
    private int age1844;
    private int age4564;
    private int ageOver65;
    private int ethnicityHispanicLatino;
    private int ethnicityNotHispanicLatino;
    private int raceAfricanAmerican;
    private int raceAmericanIndian;
    private int raceAsian;
    private int raceNativeHawaiian;
    private int raceWhite;
    private int raceOther;
    private int organizationId;
    private int totalPatients;
    private int totalVisits;
    private int patientsHypertension;
    private int patientsDiabetes;
    private int patientsPreDiabetes;
    private int patientsHighBp;
    private boolean primaryCarePractice;
    private boolean fqhcLookALike;
    private boolean comHealthCenter;
    private boolean multiSpecPractice;
    private boolean pracConsortium;
    private boolean ambulatoryClinic;
    private boolean hmo;
    private boolean aco;
    private boolean pcmh;
    private boolean otherOrgDescrip;
    private boolean physicians;
    private boolean nursePrac;
    private boolean rn;
    private boolean lpn;
    private boolean pa;
    private boolean medicalAssist;
    private boolean residents;
    private boolean interns;
    private boolean communityHealthWorkers;
    private boolean trainedMotivationalInterview;
    private Integer medicarePercent;
    private Integer medicaidPercent;
    private Integer hmoPercent;
    private Integer ppoPercent;
    private Integer uninsuredSelfPercent;
    private Integer privatePercent;
    private String vendor;
    private String product;
    private String versionEHR;
    private boolean completeCEHRT;
    private boolean patientPortal;
    private String userId;
    private Date rpDate;
}


