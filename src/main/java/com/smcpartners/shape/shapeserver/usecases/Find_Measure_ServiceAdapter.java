package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.MeasureDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Select_Measure_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.IntEntityIdRequestDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Responsible: Select a measure for an organization</br>
 * 1. ADMIN can select a measure for any organization </br
 * <p>
 * <p>
 * Created by johndestefano on 9/28/15.
 * </p>
 * <p>
 * <p>
 * Changes:<br>
 * 1.
 * </p>
 */
//TODO: Not names appropriately
@Path("/admin")
public class Find_Measure_ServiceAdapter implements Select_Measure_Service {
    @EJB
    MeasureDAO measureDAO;

    /**
     * Default constructor
     */
    public Find_Measure_ServiceAdapter() {
    }

    @Override
    @POST
    @Path("/measure/select")
    @Produces("application/json")
    @Consumes("application/json")
    @Secure({SecurityRoleEnum.ADMIN})
    @Logged
    public BooleanValueDTO selectMeasure(IntEntityIdRequestDTO id) throws UseCaseException {
        try {
            // The ADMIN can select a measure
            measureDAO.changeMeasureSelectStatus(id.getEntId(), true);
            return new BooleanValueDTO(true);
        } catch (Exception e) {
            throw new UseCaseException(e.getMessage());
        }
    }

}
