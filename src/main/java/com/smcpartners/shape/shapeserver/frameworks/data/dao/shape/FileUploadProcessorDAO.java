package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape;

import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.FileUploadRequestDTO;

/**
 * Created by johndestefano on 4/26/17.
 */
public interface FileUploadProcessorDAO {
    BooleanValueDTO createAndLogFileMeasureUpload(OrganizationMeasureDTO orgMeasureDTO, FileUploadRequestDTO fileUpload) throws DataAccessException;
}
