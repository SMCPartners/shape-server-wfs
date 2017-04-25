package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.MeasureDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.FindAllMeasuresService;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.shape.MeasureDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:<br/>
 * 1. Any user can look up a measure.
 * <p>
 * Created by johndestefano on 11/2/15.
 * <p>
 * Changes:<b/>
 */
@Path("/common")
public class FindAllMeasuresServiceAdapter implements FindAllMeasuresService {

    @Inject
    private Logger log;

    @EJB
    private MeasureDAO measureDAO;

    public FindAllMeasuresServiceAdapter() {
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
            log.logp(Level.SEVERE, this.getClass().getName(), "findAllMeasures", e.getMessage(), e);
            throw new UseCaseException(e.getMessage());
        }
    }
}
