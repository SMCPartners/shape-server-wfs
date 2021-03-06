package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.ProviderDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Edit_Provider_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.ProviderDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.NotAuthorizedToPerformActionException;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Responsible: Edit a Provider<br/>
 * 1. Only ADMIN or ORG_ADMIN can edit provider. ORG_ADMIN can only edit there organization
 * <p>
 * Created by johndestefano on 11/4/15.
 * <p>
 * Changes:<b/>
 */
@Path("/admin")
public class Edit_Provider_ServiceAdapter implements Edit_Provider_Service {

    @EJB
    ProviderDAO providerDAO;

    @Inject
    UserExtras userExtras;

    /**
     * Default Constructor
     */
    public Edit_Provider_ServiceAdapter() {
    }

    @Override
    @POST
    @Path("/provider/edit")
    @Produces("application/json")
    @Consumes("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.ORG_ADMIN})
    @Logged
    public BooleanValueDTO editProvider(ProviderDTO prov) throws UseCaseException {
        try {
            // Only ADMIN or ORG_ADMIN can edit provider
            // ORG_ADMIN can only edit their organization
            SecurityRoleEnum reqRole = userExtras.getRole();

            if (reqRole == SecurityRoleEnum.ADMIN ||
                    (reqRole == SecurityRoleEnum.ORG_ADMIN && userExtras.getOrgId() == prov.getOrganizationId())) {
                providerDAO.update(prov, prov.getId());
            } else {
                throw new NotAuthorizedToPerformActionException();
            }

            // Return value
            return new BooleanValueDTO(true);
        } catch (Exception e) {
            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException) e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }
}
