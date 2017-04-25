package com.smcpartners.shape.shapeserver.usecases;



import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.MeasureDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.AddMeasureService;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.shape.MeasureDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.response.IntEntityResponseDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:<br/>
 * 1. Create a new measure. Only an ADMIN can add a measure.
 * <p>
 * Created by johndestefano on 11/4/15.
 * <p>
 * Changes:<b/>
 */
@Path("/admin")
public class AddMeasureServiceAdapter implements AddMeasureService {

    @Inject
    private Logger log;

    @EJB
    private UserDAO userDAO;

    @EJB
    private MeasureDAO measureDAO;

    /**
     * Default Constructor
     */
    public AddMeasureServiceAdapter() {
    }

    @Override
    @POST
    @Path("/measure/add")
    @Produces("application/json")
    @Consumes("application/json")
    @Secure({SecurityRoleEnum.ADMIN})
    @Logged
    public IntEntityResponseDTO addMeasure(MeasureDTO org) throws UseCaseException {
        try {
            MeasureDTO orgDTO = measureDAO.create(org);
            return IntEntityResponseDTO.makeNew(orgDTO.getId());
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "addMeasure", e.getMessage(), e);
            throw new UseCaseException(e.getMessage());
        }
    }
}
