package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationMeasureDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Find_All_Organization_Measures_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;
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
 * 1. Finds measures. This is an ADMIN only function
 * <p>
 * Created by johndestefano on 11/2/15.
 * <p>
 * Changes:<b/>
 */
@Path("/common")
public class Find_All_Organization_Measures_ServiceAdapter implements Find_All_Organization_Measures_Service {

    @Inject
    private Logger log;

    @EJB
    private OrganizationMeasureDAO organizationMeasureDAO;

    public Find_All_Organization_Measures_ServiceAdapter() {
    }

    @Override
    @GET
    @NoCache
    @Path("/organization_measure/findAll")
    @Produces("application/json")
    @Secure({SecurityRoleEnum.ADMIN})
    @Logged
    public List<OrganizationMeasureDTO> findAllOrganizationMeasures() throws UseCaseException {
        try {
            // Admin can see all
            return organizationMeasureDAO.findAllOrganizationMeasure();
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "findAllOrganizationMeasures", e.getMessage(), e);
            throw new UseCaseException(e.getMessage());
        }
    }
}
