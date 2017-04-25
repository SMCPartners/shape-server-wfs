package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationStratificationDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.AddOrganizationStratificationService;
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
public class AddOrganizationStratificationServiceAdapter implements AddOrganizationStratificationService {

    @Inject
    private Logger log;

    @EJB
    private OrganizationStratificationDAO organizationStratificationDAO;

    @Context
    private UserExtras userExtras;


    public AddOrganizationStratificationServiceAdapter() {
    }

    @Override
    @POST
    @Path("/organization_stratification/add")
    @Produces("application/json")
    @Consumes("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.ORG_ADMIN, SecurityRoleEnum.REGISTERED})
    @Logged
    public IntEntityResponseDTO addOrganizationStratification(OrganizationStratificationDTO org) throws UseCaseException {
        try {
            // ADMIN can add for any organizations
            // ORG_ADMIN and REGISTERED can only add for their organizations
            // The date for the report is now

            // Users role
            Date now = new Date();
            OrganizationStratificationDTO orgDTO = null;

            if (userExtras.getRole() == SecurityRoleEnum.ADMIN) {
                org.setRpDate(now);
                orgDTO = organizationStratificationDAO.create(org);
            } else {
                // Does the orgs organization match the requesting users organization
                if (userExtras.getOrgId() == org.getOrganizationId()) {
                    org.setRpDate(now);
                    orgDTO = organizationStratificationDAO.create(org);
                } else {
                    throw new NotAuthorizedToPerformActionException();
                }
            }

            return IntEntityResponseDTO.makeNew(orgDTO.getId());
        }  catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "addOrganizationStratification", e.getMessage(), e);
            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException)e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }
}
