package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.MeasureDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Edit_Measure_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.MeasureDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible: Edit a Measure<br/>
 * 1. The ADMIN can edit a measure.</br>
 * 2. Can't add a measure if the name matches a currently active measure</br>
 * <p>
 * Created by johndestefano on 11/4/15.
 * <p>
 * Changes:<b/>
 */
@Path("/admin")
public class Edit_Measure_ServiceAdapter implements Edit_Measure_Service {

    @Inject
    private Logger log;

    @EJB
    MeasureDAO measureDAO;

    @Inject
    UserExtras userExtras;

    /**
     * Default Constructor
     */
    public Edit_Measure_ServiceAdapter() {
    }

    @Override
    @POST
    @Path("/measure/edit")
    @Produces("application/json")
    @Consumes("application/json")
    @Secure({SecurityRoleEnum.ADMIN})
    @Logged
    public BooleanValueDTO editMeasure(MeasureDTO measure) throws UseCaseException {
        try {

            // Check to see if an active Measure is in the db with the same name
            List<MeasureDTO> activeMeasures = measureDAO.findActiveMeasuresByName(measure.getName());

            if (activeMeasures.size() == 0) {
                measureDAO.update(measure, measure.getId());
            } else {
                throw new Exception("Active measure with name already exists!");
            }

            // Return value
            return new BooleanValueDTO(true);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "editMeasure", e.getMessage(), e);
            throw new UseCaseException(e.getMessage());
        }
    }
}
