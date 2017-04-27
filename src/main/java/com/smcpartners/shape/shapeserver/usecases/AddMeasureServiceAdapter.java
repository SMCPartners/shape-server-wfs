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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:<br/>
 * 1. Create a new measure. Only an ADMIN can add a measure.</br>
 * 2. When adding, there can only be 1 active measure with a given name</br>
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
    public IntEntityResponseDTO addMeasure(MeasureDTO measure) throws UseCaseException {
        try {
            // Check to see if an active Measure is in the db with the same name
            List<MeasureDTO> activeMeasures = measureDAO.findActiveMeasuresByName(measure.getName());

            MeasureDTO measureDTO = null;
            if (activeMeasures.size() == 0) {
                measureDTO = measureDAO.create(measure);
            } else {
                throw new Exception("Active measure with name already exists!");
            }

            return IntEntityResponseDTO.makeNew(measureDTO.getId());
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "addMeasure", e.getMessage(), e);
            throw new UseCaseException(e.getMessage());
        }
    }
}
