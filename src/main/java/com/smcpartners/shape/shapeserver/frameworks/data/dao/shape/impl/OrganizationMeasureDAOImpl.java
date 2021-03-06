package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.impl;

import com.diffplug.common.base.Errors;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationMeasureDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.MeasureEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.OrganizationEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.OrganizationMeasureEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.UserEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.frameworks.producers.annotations.ShapeDatabase;
import com.smcpartners.shape.shapeserver.shared.dto.common.NameDoubleValDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.response.OrgAvgAggregateDTO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.*;

/**
 * Responsible: Manage OrganizationMeasure entity data</br>
 * 1.  Implements OrganizationMaeasureDAO</br>
 * <p>
 * Created by johndestefano on 10/29/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Stateless
public class OrganizationMeasureDAOImpl extends AbstractCrudDAO<OrganizationMeasureDTO, OrganizationMeasureEntity, Integer> implements OrganizationMeasureDAO {

    /**
     * Constructor
     */
    @Inject
    public OrganizationMeasureDAOImpl(@ShapeDatabase EntityManager em) {
        this.em = em;
    }

    @Override
    public List<OrganizationMeasureDTO> findAllOrganizationMeasure() throws DataAccessException {
        try {
            List<OrganizationMeasureEntity> omLst = em.createNamedQuery("OrganizationMeasure.findAll").getResultList();

            List<OrganizationMeasureDTO> retLst = new ArrayList<>();
            if (omLst != null && omLst.size() > 0) {
                omLst.forEach(Errors.rethrow().wrap(om -> {
                    OrganizationMeasureDTO dto = this.mapEntityToDTO(om);
                    retLst.add(dto);
                }));
            }

            return retLst;
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<OrganizationMeasureDTO> findAllOrganizationMeasureByOrgId(int orgId) throws DataAccessException {
        try {
            OrganizationEntity oe = em.find(OrganizationEntity.class, orgId);
            List<OrganizationMeasureEntity> omLst = em.createNamedQuery("OrganizationMeasure.findAllByOrgId")
                    .setParameter("org", oe)
                    .getResultList();

            List<OrganizationMeasureDTO> retLst = new ArrayList<>();
            if (omLst != null && omLst.size() > 0) {
                omLst.forEach(Errors.rethrow().wrap(om -> {
                    OrganizationMeasureDTO dto = this.mapEntityToDTO(om);
                    retLst.add(dto);
                }));
            }

            return retLst;
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<OrganizationMeasureDTO> findOrgMeasureByMeasureIdAndYear(int measureId, int year) throws DataAccessException {
        try {
            MeasureEntity me = em.find(MeasureEntity.class, measureId);
            List<OrganizationMeasureEntity> omLst = em.createNamedQuery("OrganizationMeasure.findByMeasAndYear")
                    .setParameter("meas", me)
                    .setParameter("year", year)
                    .getResultList();

            List<OrganizationMeasureDTO> retLst = new ArrayList<>();
            if (omLst != null && omLst.size() > 0) {
                omLst.forEach(Errors.rethrow().wrap(om -> {
                    OrganizationMeasureDTO dto = this.mapEntityToDTO(om);
                    retLst.add(dto);
                }));
            }

            return retLst;
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<OrganizationMeasureDTO> findOrgMeasureByMeasureIdAndYearAndOrg(int measureId, int year, int orgId) throws DataAccessException {
        try {
            MeasureEntity me = em.find(MeasureEntity.class, measureId);
            OrganizationEntity org = em.find(OrganizationEntity.class, orgId);
            List<OrganizationMeasureEntity> omLst = em.createNamedQuery("OrganizationMeasure.findByMeasYearOrg")
                    .setParameter("meas", me)
                    .setParameter("year", year)
                    .setParameter("org", org)
                    .getResultList();

            List<OrganizationMeasureDTO> retLst = new ArrayList<>();
            if (omLst != null && omLst.size() > 0) {
                omLst.forEach(Errors.rethrow().wrap(om -> {
                    OrganizationMeasureDTO dto = this.mapEntityToDTO(om);
                    retLst.add(dto);
                }));
            }

            return retLst;
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Map<String, Map> getAvgForAllByYearByMeasure(int measureId, int orgId) throws DataAccessException {
        try {
            MeasureEntity me = em.find(MeasureEntity.class, measureId);
            List<Object[]> omLst = em.createNamedQuery("OrganizationMeasure.avgByMeasureByYear", Object[].class)
                    .setParameter("meas", me)
                    .getResultList();

            Map<String, List<Double>> aggMap = new TreeMap<>();
            Map<Integer, OrgAvgAggregateDTO> orgAggMap = new TreeMap<>();
            if (omLst != null && omLst.size() > 0) {
                omLst.forEach(Errors.rethrow().wrap(om -> {
                    String year = ((Integer) om[0]).toString();
                    Integer oId = (Integer) om[1];
                    String orgName = (String) om[2];
                    Double avg = (Double) om[3];

                    // Create aggregate list
                    List<Double> mapVal = aggMap.get(year);
                    if (mapVal == null) {
                        mapVal = new ArrayList<Double>();
                        aggMap.put(year, mapVal);
                    }
                    mapVal.add(avg);

                    // Create org list
                    // Id orgId is 0 then process everything
                    // If not then only process if the orgId equals the
                    // orgId of the current result
                    boolean process = orgId == 0 ? true : oId == orgId ? true : false;
                    if (process) {
                        OrgAvgAggregateDTO orgAgg = orgAggMap.get(oId);
                        if (orgAgg == null) {
                            orgAgg = new OrgAvgAggregateDTO();
                            orgAgg.setOrgId(oId);
                            orgAgg.setOrgName(orgName);
                            orgAgg.setMeasureYearAvgDTOS(new ArrayList<>());
                            orgAggMap.put(oId, orgAgg);
                        }
                        orgAgg.getMeasureYearAvgDTOS().add(new NameDoubleValDTO(year, avg));
                    }
                }));
            }

            // Return the map
            Map<String, Map> retMap = new HashMap<>();
            retMap.put("AGG", aggMap);
            retMap.put("ORG", orgAggMap);
            return retMap;
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public boolean checkMeasureForYearForOrgAlreadyEntered(int measureId, int orgId, int year) throws DataAccessException {
        try {
            MeasureEntity me = em.find(MeasureEntity.class, measureId);
            OrganizationEntity org = em.find(OrganizationEntity.class, orgId);
            List<OrganizationMeasureEntity> omLst = em.createNamedQuery("OrganizationMeasure.findByMeasYearOrg")
                    .setParameter("meas", me)
                    .setParameter("year", year)
                    .setParameter("org", org)
                    .getResultList();

            // Check to see if there is an entry
            if (omLst.size() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    protected OrganizationMeasureEntity mapDtoToEntity(OrganizationMeasureEntity et, OrganizationMeasureDTO dto) {
        et.setAge1844Num(dto.getAge1844Num());
        et.setAge1844Den(dto.getAge1844Den());
        et.setAge4564Num(dto.getAge4564Num());
        et.setAge4564Den(dto.getAge4564Den());
        et.setAgeOver65Num(dto.getAgeOver65Num());
        et.setAgeOver65Den(dto.getAgeOver65Den());
        et.setDenominatorValue(dto.getDenominatorValue());
        et.setEthnicityHispanicLatinoNum(dto.getEthnicityHispanicLatinoNum());
        et.setEthnicityHispanicLatinoDen(dto.getEthnicityHispanicLatinoDen());
        et.setEthnicityNotHispanicLatinoNum(dto.getEthnicityNotHispanicLatinoNum());
        et.setEthnicityNotHispanicLatinoDen(dto.getEthnicityNotHispanicLatinoDen());
        et.setGenderFemaleNum(dto.getGenderFemaleNum());
        et.setGenderFemaleDen(dto.getGenderFemaleDen());
        et.setGenderMaleNum(dto.getGenderMaleNum());
        et.setGenderMaleDen(dto.getGenderMaleDen());
        et.setId(dto.getId());
        et.setNumeratorValue(dto.getNumeratorValue());
        et.setRaceAfricanAmericanNum(dto.getRaceAfricanAmericanNum());
        et.setRaceAfricanAmericanDen(dto.getRaceAfricanAmericanDen());
        et.setRaceAmericanIndianNum(dto.getRaceAmericanIndianNum());
        et.setRaceAmericanIndianDen(dto.getRaceAmericanIndianDen());
        et.setRaceAsianNum(dto.getRaceAsianNum());
        et.setRaceAsianDen(dto.getRaceAsianDen());
        et.setRaceNativeHawaiianNum(dto.getRaceNativeHawaiianNum());
        et.setRaceNativeHawaiianDen(dto.getRaceNativeHawaiianDen());
        et.setRaceOtherNum(dto.getRaceOtherNum());
        et.setRaceOtherDen(dto.getRaceOtherDen());
        et.setRaceWhiteNum(dto.getRaceWhiteNum());
        et.setRaceWhiteDen(dto.getRaceWhiteDen());
        et.setReportPeriodYear(dto.getReportPeriodYear());
        et.setRpDate(dto.getRpDate());
        et.setFileUploadDate(dto.getFileUploadDate());

        // Organization
        OrganizationEntity org = em.find(OrganizationEntity.class, dto.getOrganizationId());
        et.setOrganizationByOrganizationId(org);

        // Measure
        MeasureEntity measure = em.find(MeasureEntity.class, dto.getMeasureId());
        et.setMeasureByMeasureId(measure);

        // User
        UserEntity user = em.find(UserEntity.class, dto.getUserId());
        et.setUserByUserId(user);

        // Return
        return et;
    }

    @Override
    protected Class<OrganizationMeasureEntity> getGenericEntityClass() throws Exception {
        return OrganizationMeasureEntity.class;
    }

    @Override
    protected OrganizationMeasureDTO mapEntityToDTO(OrganizationMeasureEntity e) throws Exception {
        OrganizationMeasureDTO d = new OrganizationMeasureDTO();
        d.setAge1844Num(e.getAge1844Num());
        d.setAge1844Den(e.getAge1844Den());
        d.setAge4564Num(e.getAge4564Num());
        d.setAge4564Den(e.getAge4564Den());
        d.setAgeOver65Num(e.getAgeOver65Num());
        d.setAgeOver65Den(e.getAgeOver65Den());
        d.setDenominatorValue(e.getDenominatorValue());
        d.setEthnicityHispanicLatinoNum(e.getEthnicityHispanicLatinoNum());
        d.setEthnicityHispanicLatinoDen(e.getEthnicityHispanicLatinoDen());
        d.setEthnicityNotHispanicLatinoNum(e.getEthnicityNotHispanicLatinoNum());
        d.setEthnicityNotHispanicLatinoDen(e.getEthnicityNotHispanicLatinoDen());
        d.setGenderFemaleNum(e.getGenderFemaleNum());
        d.setGenderFemaleDen(e.getGenderFemaleDen());
        d.setGenderMaleNum(e.getGenderMaleNum());
        d.setGenderMaleDen(e.getGenderMaleDen());
        d.setId(e.getId());
        d.setNumeratorValue(e.getNumeratorValue());
        d.setRaceAfricanAmericanNum(e.getRaceAfricanAmericanNum());
        d.setRaceAfricanAmericanDen(e.getRaceAfricanAmericanDen());
        d.setRaceAmericanIndianNum(e.getRaceAmericanIndianNum());
        d.setRaceAmericanIndianDen(e.getRaceAmericanIndianDen());
        d.setRaceAsianNum(e.getRaceAsianNum());
        d.setRaceAsianDen(e.getRaceAsianDen());
        d.setRaceNativeHawaiianNum(e.getRaceNativeHawaiianNum());
        d.setRaceNativeHawaiianDen(e.getRaceNativeHawaiianDen());
        d.setRaceOtherNum(e.getRaceOtherNum());
        d.setRaceOtherDen(e.getRaceOtherDen());
        d.setRaceWhiteNum(e.getRaceWhiteNum());
        d.setRaceWhiteDen(e.getRaceWhiteDen());
        d.setId(e.getId());
        d.setMeasureId(e.getMeasureByMeasureId().getId());
        d.setOrganizationId(e.getOrganizationByOrganizationId().getId());
        d.setUserId(e.getUserByUserId().getId());
        d.setReportPeriodYear(e.getReportPeriodYear());
        d.setRpDate(e.getRpDate());
        d.setFileUploadDate(e.getFileUploadDate());
        return d;
    }
}
