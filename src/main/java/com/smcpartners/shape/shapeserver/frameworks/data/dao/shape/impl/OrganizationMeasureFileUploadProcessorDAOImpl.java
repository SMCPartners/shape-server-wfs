package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.impl;

import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationMeasureFileUploadDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationMeasureFileUploadProcessorDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.MeasureDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationMeasureDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.OrganizationMeasureEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.MeasureDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.OrganizationMeasureFileUploadRequestDTO;

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
public class OrganizationMeasureFileUploadProcessorDAOImpl implements OrganizationMeasureFileUploadProcessorDAO {

    @Inject
    private Logger log;

    @EJB
    private OrganizationMeasureFileUploadDAO fileUploadDAO;

    @EJB
    private OrganizationMeasureDAO organizationMeasureDAO;

    @EJB
    private MeasureDAO measureDAO;

    /**
     * Default Constructor
     */
    public OrganizationMeasureFileUploadProcessorDAOImpl() {
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
    public BooleanValueDTO createAndLogFileMeasureUpload(OrganizationMeasureDTO orgMeasureDTO, OrganizationMeasureFileUploadRequestDTO fileUpload) throws DataAccessException {
        try {
            // Get the measure id
            int measureId = fileUpload.getMeasureEntityId();
            if (measureId == 0) {
                List<MeasureDTO> mDTOLst = measureDAO.findActiveMeasuresByName(fileUpload.getMeasureEntityName());
                measureId = mDTOLst.get(0).getId();
            }

            // Save organization measure and get the id
            orgMeasureDTO.setMeasureId(measureId);
            int orgMeasureId = organizationMeasureDAO.create(orgMeasureDTO).getId();

            // Save file upload data
            fileUpload.setMeasureEntityId(measureId);
            fileUpload.setOrgMeasureId(orgMeasureId);
            fileUploadDAO.create(fileUpload);


            return BooleanValueDTO.get(true);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "createAndLogFileMeasureUpload", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }
}
