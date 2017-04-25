package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape;

import com.smcpartners.shape.shapeserver.crosscutting.activitylogging.dto.ClickLogDTO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.CrudDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;

import java.util.List;


/**
 * Responsible:</br>
 * 1. Handle CRUD and other data related activities for the ClickLog </br>
 * <p>
 * Created by johndestefano on 10/3/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
public interface ClickLogDAO extends CrudDAO<ClickLogDTO, Integer> {

    /**
     * Find the activities by user
     *
     * @param userId
     * @return
     * @throws DataAccessException
     */
    List<ClickLogDTO> findByUser(String userId) throws DataAccessException;
}
