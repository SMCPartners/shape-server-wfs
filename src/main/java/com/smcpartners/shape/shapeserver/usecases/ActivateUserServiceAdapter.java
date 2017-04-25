package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.ActivateUserService;
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
import javax.ws.rs.core.Context;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:</br>
 * 1.If its ADMIN then make the changes. If its ORG_ADMIN find the requesting users organization
 * and make sure the requesting users organization matches the target users organization</br
 * <p>
 * Created by johndestefano on 9/28/15.
 * </p>
 * <p>
 * Changes:<br>
 * 1.
 * </p>
 */
@Path("/admin")
public class ActivateUserServiceAdapter implements ActivateUserService {

    @Inject
    private Logger log;

    @EJB
    private UserDAO userDAO;

    @Context
    private UserExtras userExtras;

    /**
     * Default constructor
     */
    public ActivateUserServiceAdapter() {
    }

    /**
     * This method is protected so only Admin users can dao it and
     * it requires an active Admin user.
     *
     * @param targetUserId - The user to be activated
     * @throws UseCaseException
     * @see ActivateUserService
     */
    @Override
    @PUT
    @Path("/activate/{targetUserId}")
    @Produces("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.ORG_ADMIN})
    @Logged
    public BooleanValueDTO activateUser(String targetUserId) throws UseCaseException {
        try {
            // Find the requesting users role

            // If its ADMIN then make the changes
            // If its ORG_ADMIN find the requesting users organization
            // Make sure the requesting users organization matches the target users organization
            // Make the change
            if (userExtras.getRole() == SecurityRoleEnum.ADMIN) {
                userDAO.activateUser(targetUserId);
            } else {
                // Its an org admin so only for there organization
                UserDTO targetUser  = userDAO.findById(targetUserId);
                if (targetUser.getOrganizationId() == userExtras.getOrgId()) {
                    userDAO.activateUser(targetUserId);
                } else {
                    throw new NotAuthorizedToPerformActionException();
                }
            }

            return new BooleanValueDTO(true);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "activateUser", e.getMessage(), e);
            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException)e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }
}
