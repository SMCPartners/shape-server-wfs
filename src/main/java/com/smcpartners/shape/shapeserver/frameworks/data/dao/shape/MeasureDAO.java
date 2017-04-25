package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape;

import com.smcpartners.shape.shapeserver.frameworks.data.dao.CrudDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.shared.dto.shape.MeasureDTO;

import java.util.List;

/**
 * Responsible:</br>
 * 1. Handle CRUD and other data related activities for the Measure</br>
 * <p>
 * Created by johndestefano on 10/29/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
public interface MeasureDAO extends CrudDAO<MeasureDTO, Integer> {

    /**
     * Change the select status of the measure
     *
     * @param id
     * @param status
     * @throws DataAccessException
     */
    void changeMeasureSelectStatus(int id, boolean status) throws DataAccessException;


    /**
     * Return all measures
     *
     * @return
     * @throws DataAccessException
     */
    List<MeasureDTO> findAllMeasures() throws DataAccessException;

    /**
     *
     * @return
     * @throws DataAccessException
     */

    List<MeasureDTO> findAllMeasuresById(int measureId) throws DataAccessException;
}
