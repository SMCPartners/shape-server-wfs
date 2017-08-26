package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationStratificationDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Find_All_Organization_Stratifications_ByOrganizationService;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationStratificationDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.NotAuthorizedToPerformActionException;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Responsible: Find all organization stratification for an organization. <br/>
 * 1. ADMIN and DPH_USER can see all. Others can see only their organzations
 * <p>
 * Created by johndestefano on 11/2/15.
 * <p>
 * Changes:<b/>
 */
@Path("/common")
public class Find_All_Organization_Stratifications_By_Organization_ServiceAdapter implements Find_All_Organization_Stratifications_ByOrganizationService {

    @EJB
    OrganizationStratificationDAO organizationStratificationDAO;

    @Inject
    UserExtras userExtras;

    public Find_All_Organization_Stratifications_By_Organization_ServiceAdapter() {
    }

    @Override
    @GET
    @NoCache
    @Path("/organization_stratification/findAllByOrg/{orgId}")
    @Produces("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.DPH_USER, SecurityRoleEnum.ORG_ADMIN, SecurityRoleEnum.REGISTERED})
    @Logged
    public List<OrganizationStratificationDTO> findAllOrganizationStratificationsByOrg(@PathParam("orgId") int orgId) throws UseCaseException {
        try {
            // Admin can see all
            // Other only see their organization

            // Get user and find security role
            SecurityRoleEnum reqRole = userExtras.getRole();

            if (reqRole == SecurityRoleEnum.ADMIN || reqRole == SecurityRoleEnum.DPH_USER ||
                    (orgId == userExtras.getOrgId() && (reqRole == SecurityRoleEnum.ORG_ADMIN ||
                            reqRole == SecurityRoleEnum.REGISTERED))) {
                return organizationStratificationDAO.findAllOrganizationStratificationByOrgId(orgId);
            } else {
                throw new NotAuthorizedToPerformActionException();
            }
        } catch (Exception e) {
            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException) e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }
}
