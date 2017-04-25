package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationMeasureDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.DeleteOrganizationMeasureService;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:</br>
 * 1. ADMIN can delete a measure for any organization. ORG_ADMIN and REGISTERD USER can
 * only delete for their organization</br>
 * <p>
 * Created by johndestefano on 3/15/16.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Path("/admin")
public class DeleteOrganizationMeasureServiceAdapter implements DeleteOrganizationMeasureService {

    @Inject
    private Logger log;

    @EJB
    private OrganizationMeasureDAO organizationMeasureDAO;

    @Inject
    private UserExtras userExtras;


    public DeleteOrganizationMeasureServiceAdapter() {
    }

    @Override
    @POST
    @Path("/organization_measure/delete")
    @Produces("application/json")
    @Consumes("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.ORG_ADMIN, SecurityRoleEnum.REGISTERED})
    @Logged
    public BooleanValueDTO deleteOrganizationMeasure(OrganizationMeasureDTO org) throws UseCaseException {
        try {
            if (SecurityRoleEnum.ADMIN == userExtras.getRole()) {
                organizationMeasureDAO.delete(org.getId());
            } else {
                // Not the ADMIN
                // Look up measure first to get organization its for
                OrganizationMeasureDTO dto =  organizationMeasureDAO.findById(org.getId());

                // Users organization must match the organization of the measure
                if (userExtras.getOrgId() == dto.getOrganizationId()) {
                    organizationMeasureDAO.delete(org.getId());
                } else {
                    throw new NotAuthorizedToPerformActionException();
                }
            }

            return new BooleanValueDTO(true);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "deleteOrganizationMeasure", e.getMessage(), e);
            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException)e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }
}
