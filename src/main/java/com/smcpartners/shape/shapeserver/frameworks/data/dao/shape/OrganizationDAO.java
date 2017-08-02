package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape;

import com.smcpartners.shape.shapeserver.frameworks.data.dao.CrudDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationDTO;

import java.util.List;

/**
 * Responsible: Contract for managing Organization data</br>
 * 1. Handle CRUD and other data related activities for the Organization</br>
 * <p>
 * Created by johndestefano on 10/29/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
public interface OrganizationDAO extends CrudDAO<OrganizationDTO, Integer> {

    /**
     * Find all organizations
     *
     * @return
     * @throws DataAccessException
     */
    List<OrganizationDTO> findAll() throws DataAccessException;


    /**
     * Inactivate the organization
     *
     * @param id
     * @throws DataAccessException
     */
    void changeOrganizationActiveStatus(int id, boolean status) throws DataAccessException;
}
