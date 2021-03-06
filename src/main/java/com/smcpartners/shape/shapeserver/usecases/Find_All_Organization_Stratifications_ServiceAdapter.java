package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationStratificationDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Find_All_Organization_Stratifications_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationStratificationDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Responsible: Return all organization stratification<br/>
 * 1. ADMIN can find all organozation stratifications.
 * <p>
 * Created by johndestefano on 11/2/15.
 * <p>
 * Changes:<b/>
 */
@Path("/common")
public class Find_All_Organization_Stratifications_ServiceAdapter implements Find_All_Organization_Stratifications_Service {

    @EJB
    OrganizationStratificationDAO organizationStratificationDAO;

    public Find_All_Organization_Stratifications_ServiceAdapter() {
    }

    @Override
    @GET
    @NoCache
    @Path("/organization_stratification/findAll")
    @Produces("application/json")
    @Secure({SecurityRoleEnum.ADMIN})
    @Logged
    public List<OrganizationStratificationDTO> findAllOrganizationStratifications() throws UseCaseException {
        try {
            // Admin can see all
            return organizationStratificationDAO.findAllOrganizationStratification();
        } catch (Exception e) {
            throw new UseCaseException(e.getMessage());
        }
    }
}
