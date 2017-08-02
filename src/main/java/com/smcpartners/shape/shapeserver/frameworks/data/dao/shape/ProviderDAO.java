package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape;

import com.smcpartners.shape.shapeserver.frameworks.data.dao.CrudDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.shared.dto.shape.ProviderDTO;

import java.util.List;

/**
 * Responsible: Contract for managing Provider data</br>
 * 1. Handle CRUD and other data related activities for the Provider</br>
 * <p>
 * Created by johndestefano on 10/29/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
public interface ProviderDAO extends CrudDAO<ProviderDTO, Integer> {

    /**
     * Find all providers
     *
     * @return
     * @throws DataAccessException
     */
    List<ProviderDTO> findAll() throws DataAccessException;

    /**
     * Find all providers by organization
     *
     * @return
     * @throws DataAccessException
     */
    List<ProviderDTO> findAllByOrg(int orgId) throws DataAccessException;

    /**
     * Inactivate the organization
     *
     * @param id
     * @throws DataAccessException
     */
    void changeProviderActiveStatus(int id, boolean status) throws DataAccessException;
}
