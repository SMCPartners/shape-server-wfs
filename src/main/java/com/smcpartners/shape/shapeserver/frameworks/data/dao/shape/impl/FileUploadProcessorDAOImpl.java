package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.impl;

import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.FileUploadDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.FileUploadProcessorDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.MeasureDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationMeasureDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.MeasureDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.FileUploadRequestDTO;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsibility: </br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/26/17
 */
@Stateless
public class FileUploadProcessorDAOImpl implements FileUploadProcessorDAO {

    @Inject
    private Logger log;

    @EJB
    private FileUploadDAO fileUploadDAO;

    @EJB
    private OrganizationMeasureDAO organizationMeasureDAO;

    @EJB
    private MeasureDAO measureDAO;

    /**
     * Default Constructor
     */
    public FileUploadProcessorDAOImpl() {
    }

    /**
     * Insert the file upload and measure data
     *
     * @param orgMeasureDTO
     * @param fileUpload
     * @return
     * @throws DataAccessException
     */
    @Override
    public BooleanValueDTO createAndLogFileMeasureUpload(OrganizationMeasureDTO orgMeasureDTO, FileUploadRequestDTO fileUpload) throws DataAccessException {
        try {
            // Find measure id based on name, should only be one!
            List<MeasureDTO> mDTOLst = measureDAO.findActiveMeasuresByName(fileUpload.getMeasureEntityName());
            int measureId = mDTOLst.get(0).getId();

            // File
            fileUpload.setMeasureEntityId(measureId);
            FileUploadRequestDTO fDTO = fileUploadDAO.create(fileUpload);

            // Organization measure
            orgMeasureDTO.setMeasureId(measureId);
            OrganizationMeasureDTO omDTO = organizationMeasureDAO.create(orgMeasureDTO);

            return BooleanValueDTO.get(true);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "createAndLogFileMeasureUpload", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }
}
