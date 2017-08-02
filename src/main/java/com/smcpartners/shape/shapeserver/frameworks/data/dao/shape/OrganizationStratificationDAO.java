package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape;

import com.smcpartners.shape.shapeserver.frameworks.data.dao.CrudDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationStratificationDTO;

import java.util.List;

/**
 * Responsible: Contract for managing Organization Stratification data</br>
 * 1. Handle CRUD and other data related activities for the OrganizationStratification</br>
 * <p>
 * Created by johndestefano on 10/29/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
public interface OrganizationStratificationDAO extends CrudDAO<OrganizationStratificationDTO, Integer> {

    /**
     * @return
     * @throws DataAccessException
     */
    List<OrganizationStratificationDTO> findAllOrganizationStratification() throws DataAccessException;

    /**
     * @return
     * @throws DataAccessException
     */
    List<OrganizationStratificationDTO> findAllOrganizationStratificationByOrgId(int orgId) throws DataAccessException;
}
