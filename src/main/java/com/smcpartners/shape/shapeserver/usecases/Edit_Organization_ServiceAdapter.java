package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Edit_Organization_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.NotAuthorizedToPerformActionException;
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
 * 1. Only ADMIN or ORG_ADMIN can edi organizations. ORG_ADMIN can only edit there organization
 * <p>
 * Created by johndestefano on 11/4/15.
 * <p>
 * Changes:<b/>
 */
@Path("/admin")
public class Edit_Organization_ServiceAdapter implements Edit_Organization_Service {

    @Inject
    private Logger log;

    @EJB
    private UserDAO userDAO;

    @EJB
    private OrganizationDAO organizationDAO;

    @Inject
    private UserExtras userExtras;

    /**
     * Default Constructor
     */
    public Edit_Organization_ServiceAdapter() {
    }

    @Override
    @POST
    @Path("/organization/edit")
    @Produces("application/json")
    @Consumes("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.ORG_ADMIN})
    @Logged
    public BooleanValueDTO editOrganization(OrganizationDTO org) throws UseCaseException {
        try {
            // Only ADMIN or ORG_ADMIN can edit organizations
            // ORG_ADMIN can only edit there organization
            SecurityRoleEnum reqRole = userExtras.getRole();
            if (reqRole == SecurityRoleEnum.ADMIN ||
                    (reqRole == SecurityRoleEnum.ORG_ADMIN && userExtras.getOrgId() == org.getId())) {
                organizationDAO.update(org, org.getId());
            } else {
                throw new NotAuthorizedToPerformActionException();
            }

            // Return value
            return new BooleanValueDTO(true);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "editOrganization", e.getMessage(), e);
            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException)e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }
}
