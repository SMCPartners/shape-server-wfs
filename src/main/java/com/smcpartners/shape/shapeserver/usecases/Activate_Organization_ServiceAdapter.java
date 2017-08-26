package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Activate_Organization_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.IntEntityIdRequestDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.NotAuthorizedToPerformActionException;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.logging.Logger;

/**
 * Responsible: Activate an Organization<br/>
 * 1.
 * <p>
 * Created by johndestefano on 11/4/15.
 * <p>
 * Changes:<b/>
 */
@Path("/admin")
public class Activate_Organization_ServiceAdapter implements Activate_Organization_Service {

    @EJB
    OrganizationDAO organizationDAO;

    @Inject
    UserExtras userExtras;

    /**
     * Constructor
     */
    public Activate_Organization_ServiceAdapter() {
    }

    @Override
    @POST
    @Path("/organization/activate")
    @Produces("application/json")
    @Consumes("application/json")
    @Secure({SecurityRoleEnum.ADMIN})
    @Logged
    public BooleanValueDTO activateOrganization(IntEntityIdRequestDTO id) throws UseCaseException {
        try {
            // Request needs the organization id
            if (userExtras.getRole() == SecurityRoleEnum.ADMIN) {
                // Activate organization
                organizationDAO.changeOrganizationActiveStatus(userExtras.getOrgId(), true);
                return new BooleanValueDTO(true);
            } else {
                throw new NotAuthorizedToPerformActionException();
            }
        } catch (Exception e) {
            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException)e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }
}
