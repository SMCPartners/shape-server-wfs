package com.smcpartners.shape.shapeserver.gateway.rest.services;

import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ws.rs.*;

/**
 * Responsible:</br>
 * 1.  Support REST framework</br>
 * <p>
 * Created by johndestefano on 3/15/16.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Path("/admin")
public interface Delete_Organization_Measure_Service {

    @DELETE
    @Path("/organization_measure/delete")
    @Produces("application/json")
    @Consumes("application/json")
    BooleanValueDTO deleteOrganizationMeasure(OrganizationMeasureDTO org) throws UseCaseException;
}
