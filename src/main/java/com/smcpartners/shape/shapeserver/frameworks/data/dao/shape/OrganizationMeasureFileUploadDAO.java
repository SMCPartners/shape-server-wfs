package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape;


import com.smcpartners.shape.shapeserver.frameworks.data.dao.CrudDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.OrganizationMeasureFileUploadRequestDTO;

import java.util.Date;

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
public interface OrganizationMeasureFileUploadDAO extends CrudDAO<OrganizationMeasureFileUploadRequestDTO, Integer> {

    OrganizationMeasureFileUploadRequestDTO getOrgMeasureFileUploadForOrgMeasureIdAndUploadDt(int orgMeasureId, Date dt) throws DataAccessException;
}
