package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape;

import com.smcpartners.shape.shapeserver.frameworks.data.dao.CrudDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;

import java.util.List;

/**
 * Responsible:</br>
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


}
