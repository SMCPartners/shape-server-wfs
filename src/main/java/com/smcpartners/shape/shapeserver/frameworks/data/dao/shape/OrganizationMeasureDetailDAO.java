package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape;

import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDetailDTO;

import java.util.List;

/**
 * Responsibility: Contract for find Organization Measure Detail data</br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 5/25/17
 */
public interface OrganizationMeasureDetailDAO {
    /**
     * Brings back organization measure and measure detail
     *
     * @param orgId
     * @return
     * @throws DataAccessException
     */
    List<OrganizationMeasureDetailDTO> findAllOrganizationMeasureDetailByOrgId(int orgId) throws DataAccessException;
}
