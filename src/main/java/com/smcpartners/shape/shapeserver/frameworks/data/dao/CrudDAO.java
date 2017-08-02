package com.smcpartners.shape.shapeserver.frameworks.data.dao;

import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;

/**
 * Responsible: Base interface which other DAO's can use to implement basic crud functionality
 * <p>
 * Created by johndestefano on 9/12/15.
 */
public interface CrudDAO<T, K> {


    /**
     * Create a new instance
     *
     * @param dto
     * @return
     * @throws DataAccessException
     */
    T create(T dto) throws DataAccessException;

    /**
     * Update entity
     *
     * @param dto
     * @return
     * @throws DataAccessException
     */
    T update(T dto, K key) throws DataAccessException;

    /**
     * Delete based on the primary key
     *
     * @param key
     * @throws DataAccessException
     */
    void delete(K key) throws DataAccessException;

    /**
     * Find entity by ID
     *
     * @param key
     * @return
     * @throws DataAccessException
     */
    T findById(K key) throws DataAccessException;

}
