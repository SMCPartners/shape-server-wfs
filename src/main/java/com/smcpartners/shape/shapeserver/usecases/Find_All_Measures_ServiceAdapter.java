package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.MeasureDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Find_All_Measures_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.shape.MeasureDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Responsible: Find all measures<br/>
 * 1. Any user can look up a measure.
 * <p>
 * Created by johndestefano on 11/2/15.
 * <p>
 * Changes:<b/>
 */
@Path("/common")
public class Find_All_Measures_ServiceAdapter implements Find_All_Measures_Service {

    @EJB
    MeasureDAO measureDAO;

    public Find_All_Measures_ServiceAdapter() {
    }

    @Override
    @GET
    @NoCache
    @Path("/measure/findAll")
    @Produces("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.DPH_USER, SecurityRoleEnum.ORG_ADMIN, SecurityRoleEnum.REGISTERED})
    @Logged
    public List<MeasureDTO> findAllMeasures() throws UseCaseException {
        try {
            // Anyone can select a measure
            return measureDAO.findAllMeasures();
        } catch (Exception e) {
            throw new UseCaseException(e.getMessage());
        }
    }
}
