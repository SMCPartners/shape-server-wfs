package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape;

import com.smcpartners.shape.shapeserver.frameworks.data.dao.CrudDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;

import java.util.List;
import java.util.Map;

/**
 * Responsible: Contract for managing Organization Measure data</br>
 * 1. Handle CRUD and other data related activities for the OrganizationMeasure</br>
 * <p>
 * Created by johndestefano on 10/29/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
public interface OrganizationMeasureDAO extends CrudDAO<OrganizationMeasureDTO, Integer> {

    /**
     *
     * @return
     * @throws DataAccessException
     */
    List<OrganizationMeasureDTO> findAllOrganizationMeasure() throws DataAccessException;

    /**
     *
     * @return
     * @throws DataAccessException
     */
    List<OrganizationMeasureDTO> findAllOrganizationMeasureByOrgId(int orgId) throws DataAccessException;

    /**
     *
     * @param measureId
     * @param year
     * @return
     * @throws DataAccessException
     */
    List<OrganizationMeasureDTO> findOrgMeasureByMeasureIdAndYear(int measureId, int year) throws DataAccessException;

    /**
     *
     * @param measureId
     * @param year
     * @return
     * @throws DataAccessException
     */
    List<OrganizationMeasureDTO> findOrgMeasureByMeasureIdAndYearAndOrg(int measureId, int year, int orgId) throws DataAccessException;

    /**
     * Returns a list for all years for avg (numerator/denominator) for a given measure.
     * The return is ordered by the reporting period year
     * @param measureId
     * @param orgId - If 0 the return all orgs
     * @return
     * @throws DataAccessException
     */
    Map<String, Map> getAvgForAllByYearByMeasure(int measureId, int orgId) throws DataAccessException;

    /**
     * Checks to see if a specific measure (by measure id) has been entered for an organization
     * in the year supplied.
     *
     * @param measureId
     * @param orgId
     * @param year
     * @return
     * @throws DataAccessException
     */
    boolean checkMeasureForYearForOrgAlreadyEntered(int measureId, int orgId, int year) throws DataAccessException;

}
