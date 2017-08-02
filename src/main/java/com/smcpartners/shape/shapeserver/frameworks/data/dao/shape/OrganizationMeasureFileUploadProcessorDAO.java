package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape;

import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.OrganizationMeasureFileUploadRequestDTO;

/**
 * Responsible: Contract for creating an Organization Measure and corresponding Organization Measure
 * File Upload</br>
 *
 * Created by johndestefano on 4/26/17.
 */
public interface OrganizationMeasureFileUploadProcessorDAO {

    /**
     * Adds a row to the organization measure and file upload tables
     *
     * @param orgMeasureDTO
     * @param fileUpload
     * @return
     * @throws DataAccessException
     */
    BooleanValueDTO createAndLogFileMeasureUpload(OrganizationMeasureDTO orgMeasureDTO, OrganizationMeasureFileUploadRequestDTO fileUpload) throws DataAccessException;
}
