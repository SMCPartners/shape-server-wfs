package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.impl;

import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.*;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.MeasureDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationStratificationDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.OrganizationMeasureFileUploadRequestDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.OrganizationStratificationFileUploadRequestDTO;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsibility: Save and log an Organization Stratification File Upload and associated
 * Organization Stratification</br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/26/17
 */
@Stateless
public class OrganizationStratificationFileUploadProcessorDAOImpl implements OrganizationStratificationFileUploadProcessorDAO {

    @EJB
    private OrganizationStratificationFileUploadDAO fileUploadDAO;

    @EJB
    private OrganizationStratificationDAO organizationStratificationDAO;

    /**
     * Default Constructor
     */
    public OrganizationStratificationFileUploadProcessorDAOImpl() {
    }

    @Override
    public BooleanValueDTO createAndLogFileStratificationUpload(OrganizationStratificationDTO orgStratDTO, OrganizationStratificationFileUploadRequestDTO fileUpload) throws DataAccessException {
        try {
            // Save organization measure and get the id
            int orgStratificationId = organizationStratificationDAO.create(orgStratDTO).getId();

            // Save file upload data
            fileUpload.setOrgStratId(orgStratificationId);
            fileUploadDAO.create(fileUpload);

            return BooleanValueDTO.get(true);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }
}
