package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationMeasureDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.EditOrganizationMeasureService;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.NotAuthorizedToPerformActionException;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:<br/>
 * 1. Only ADMIN or ORG_ADMIN can edit organization measures. ORG_ADMIN and REGISTERED user
 * can only edit their organization.
 * <p>
 * Created by johndestefano on 11/4/15.
 * <p>
 * Changes:<b/>
 */
//TODO: Should a registered user be able ot edit or delete an organization measure
@Path("/common")
public class EditOrganizationMeasureServiceAdapter implements EditOrganizationMeasureService {

    @Inject
    private Logger log;

    @EJB
    private OrganizationMeasureDAO organizationMeasureDAO;

    @Inject
    private UserExtras userExtras;

    /**
     * Default Constructor
     */
    public EditOrganizationMeasureServiceAdapter() {
    }

    @Override
    @POST
    @Path("/organization_measure/edit")
    @Produces("application/json")
    @Consumes("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.ORG_ADMIN, SecurityRoleEnum.REGISTERED})
    @Logged
    public BooleanValueDTO editOrganizationMeasure(OrganizationMeasureDTO org) throws UseCaseException {
        try {
            // Only ADMIN or ORG_ADMIN can edit organization measures
            // ORG_ADMIN can only edit there organization
            if (SecurityRoleEnum.ADMIN == userExtras.getRole()) {
                organizationMeasureDAO.update(org, org.getId());
            } else {
                // Not the ADMIN. Can only do for their organization
                if (userExtras.getOrgId() == org.getOrganizationId()) {
                    organizationMeasureDAO.update(org, org.getId());
                } else {
                    throw new NotAuthorizedToPerformActionException();
                }
            }

            // Return value
            return new BooleanValueDTO(true);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "editOrganizationMeasure", e.getMessage(), e);
            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException)e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }
}
