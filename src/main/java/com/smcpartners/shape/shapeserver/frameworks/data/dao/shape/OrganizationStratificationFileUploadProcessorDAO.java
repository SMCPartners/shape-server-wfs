package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape;

import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationStratificationDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.OrganizationStratificationFileUploadRequestDTO;

/**
 * Responsible: Creating an Organization Stratification and corresponding Organization Stratification
 * File Upload</br>
 *
 *
 * Created by johndestefano on 7/24/17.
 */
public interface OrganizationStratificationFileUploadProcessorDAO {

    /**
     * Adds a row to the organization stratification and file upload tables
     *
     * @param orgStratDTO
     * @param fileUpload
     * @return
     * @throws DataAccessException
     */
    BooleanValueDTO createAndLogFileStratificationUpload(OrganizationStratificationDTO orgStratDTO, OrganizationStratificationFileUploadRequestDTO fileUpload) throws DataAccessException;
}
