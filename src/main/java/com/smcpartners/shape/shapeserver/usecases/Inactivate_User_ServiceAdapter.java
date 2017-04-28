package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Inactivate_User_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.UserDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.NotAuthorizedToPerformActionException;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:</br>
 * 1. The ADMIN can inactivate anyone. ORG_ADMIN can only inactivate users in their ORG</br
 * <p>
 * <p>
 * Created by johndestefano on 9/28/15.
 * </p>
 * <p>
 * <p>
 * Changes:<br>
 * 1.
 * </p>
 */
@Path("/admin")
public class Inactivate_User_ServiceAdapter implements Inactivate_User_Service {

    @Inject
    private Logger log;

    @EJB
    private UserDAO userDAO;

    @Inject
    private UserExtras userExtras;

    /**
     * Default constructor
     */
    public Inactivate_User_ServiceAdapter() {
    }

    @Override
    @PUT
    @Path("/inactivate/{targetuserid}")
    @Produces("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.ORG_ADMIN})
    @Logged
    public BooleanValueDTO inactivateUser(String targetUserId) throws UseCaseException {
        try {
            // The ADMIN can inactivate anyone
            // ORG_ADMIN can only inactivate users in their ORG
            SecurityRoleEnum reqUserRole = userExtras.getRole();

            if (SecurityRoleEnum.ADMIN == reqUserRole) {
                userDAO.inactivateUser(targetUserId);
                return new BooleanValueDTO(true);
            } else if (SecurityRoleEnum.ORG_ADMIN == reqUserRole) {
                // Find the org of the target user
                UserDTO targetUser = userDAO.findById(targetUserId);

                // If they match then inactivate
                if (targetUser.getOrganizationId() == userExtras.getOrgId()) {
                    userDAO.inactivateUser(targetUserId);
                    return new BooleanValueDTO(true);
                } else {
                    throw new NotAuthorizedToPerformActionException();
                }
            }

            return new BooleanValueDTO(false);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "inactivateUser", e.getMessage(), e);
            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException)e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }

}
