package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.ProviderDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.InactivateProviderService;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.ProviderDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.IntEntityIdRequestDTO;
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
 * 1. An ADMIN can inactivate any provider. An ORG_ADMIN can inactivate providers for their
 * organization.
 * <p>
 * Created by johndestefano on 11/4/15.
 * <p>
 * Changes:<b/>
 */
@Path("/admin")
public class InactivateProviderServiceAdapter implements InactivateProviderService {

    @Inject
    private Logger log;

    @EJB
    private ProviderDAO providerDAO;

    @Context
    private UserExtras userExtras;


    public InactivateProviderServiceAdapter() {
    }

    @Override
    @POST
    @Path("/provider/inactivate")
    @Produces("application/json")
    @Consumes("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.ORG_ADMIN})
    public BooleanValueDTO inactivateProvider(IntEntityIdRequestDTO id) throws UseCaseException {
        try {
            // Only ADMIN can inactivate any provider
            // ORG_ADMIN can inactivate org provider
            if (userExtras.getRole() == SecurityRoleEnum.ADMIN) {
                providerDAO.changeProviderActiveStatus(id.getEntId(), false);
            } else {
                // Is org admin
                ProviderDTO provider = providerDAO.findById(id.getEntId());

                // Organizations must match
                if (provider.getOrganizationId() == userExtras.getOrgId()) {
                    providerDAO.changeProviderActiveStatus(id.getEntId(), false);
                } else {
                    throw new NotAuthorizedToPerformActionException();
                }
            }

            // Return value
            return new BooleanValueDTO(true);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "inactivateProvider", e.getMessage(), e);
            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException)e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }
}
