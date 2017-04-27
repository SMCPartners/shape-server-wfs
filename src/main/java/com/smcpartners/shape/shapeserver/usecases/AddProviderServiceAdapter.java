package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.ProviderDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.AddProviderService;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.ProviderDTO;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:<br/>
 * 1. ADMIN and ORG_ADMIN can add a provider. If its and org admin the provider must be for their organization.
 * <p>
 * Created by johndestefano on 11/4/15.
 * <p>
 * Changes:<b/>
 */
@Path("/admin")
public class AddProviderServiceAdapter implements AddProviderService {

    @Inject
    private Logger log;

    @EJB
    private ProviderDAO providerDAO;

    @Inject
    private UserExtras userExtras;


    public AddProviderServiceAdapter() {
    }

    @Override
    @POST
    @Path("/provider/add")
    @Produces("application/json")
    @Consumes("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.ORG_ADMIN})
    @Logged
    public IntEntityResponseDTO addProvider(ProviderDTO prov) throws UseCaseException {
        try {
            // Only ADMIN can add organizations
            ProviderDTO provDTO = null;

            if (userExtras.getRole() == SecurityRoleEnum.ADMIN) {
                provDTO = providerDAO.create(prov);
                return IntEntityResponseDTO.makeNew(provDTO.getId());
            } else {
                // ORG_ADMIN user
                if (userExtras.getOrgId() == prov.getOrganizationId()) {
                    provDTO = providerDAO.create(prov);
                    return IntEntityResponseDTO.makeNew(provDTO.getId());
                } else {
                    throw new NotAuthorizedToPerformActionException();
                }
            }
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "addProvider", e.getMessage(), e);
            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException)e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }
}
