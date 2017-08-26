package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.MeasureDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Add_Measure_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.shape.MeasureDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.response.IntEntityResponseDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Responsible: Create a new measure<br/>
 * 1. Only an ADMIN can add a measure.</br>
 * 2. When adding, there can only be 1 active measure with a given name</br>
 * <p>
 * Created by johndestefano on 11/4/15.
 * <p>
 * Changes:<b/>
 */
@Path("/admin")
public class Add_Measure_ServiceAdapter implements Add_Measure_Service {

    @EJB
    MeasureDAO measureDAO;

    /**
     * Default Constructor
     */
    public Add_Measure_ServiceAdapter() {
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
            throw new UseCaseException(e.getMessage());
        }
    }
}
