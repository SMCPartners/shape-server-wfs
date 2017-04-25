package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape;


import com.smcpartners.shape.shapeserver.frameworks.data.dao.CrudDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.FileUploadRequestDTO;

/**
 * Responsible:<br/>
 * 1. <br/>
 * <p>
 * Created by johndestefano on 5/9/16.
 * </p>
 * <p>
 * Changes:<br/>
 * 1. <br/>
 * </p>
 */
public interface FileUploadDAO extends CrudDAO<FileUploadRequestDTO, Integer> {

    /**
     * Creates an Organizational Measure and a file upload log entry
     *
     * @return
     * @throws DataAccessException
     */
    BooleanValueDTO createAndLogFileMeasureUpload(OrganizationMeasureDTO measureDTO, FileUploadRequestDTO fileUpload) throws DataAccessException;
}
