package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationStratificationDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.EditOrganizationStratificationService;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationStratificationDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.response.IntEntityResponseDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.NotAuthorizedToPerformActionException;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:<br/>
 * 1. ADMIN can add for any organizations. ORG_ADMIN and REGISTERED can only add for their organizations
 * <p>
 * Created by johndestefano on 11/4/15.
 * <p>
 * Changes:<b/>
 */
@Path("/common")
public class EditOrganizationStratificationServiceAdapter implements EditOrganizationStratificationService {

    @Inject
    private Logger log;

    @EJB
    private OrganizationStratificationDAO organizationStratificationDAO;

    @Inject
    private UserExtras userExtras;


    public EditOrganizationStratificationServiceAdapter() {
    }

    @Override
    @POST
    @Path("/organization_stratification/edit")
    @Produces("application/json")
    @Consumes("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.ORG_ADMIN, SecurityRoleEnum.REGISTERED})
    @Logged
    public IntEntityResponseDTO editOrganizationStratification(OrganizationStratificationDTO org) throws UseCaseException {
        try {
            // ADMIN can add for any organizations
            // ORG_ADMIN and REGISTERED can only add for their organizations
            // The date for the report is now

            // Get Users organization
            int orgId = userExtras.getOrgId();

            // Users role
            Date now = new Date();

            if (userExtras.getRole() == SecurityRoleEnum.ADMIN) {
                org.setRpDate(now);
                OrganizationStratificationDTO orgDTO = organizationStratificationDAO.update(org, org.getId());
                return IntEntityResponseDTO.makeNew(orgDTO.getId());
            } else {
                // Not the admin
                if (orgId == org.getOrganizationId()) {
                    org.setRpDate(now);
                    OrganizationStratificationDTO orgDTO = organizationStratificationDAO.update(org, org.getId());
                    return IntEntityResponseDTO.makeNew(orgDTO.getId());
                } else {
                    throw new NotAuthorizedToPerformActionException();
                }
            }

        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "editOrganizationStratification", e.getMessage(), e);
            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException)e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }
}
