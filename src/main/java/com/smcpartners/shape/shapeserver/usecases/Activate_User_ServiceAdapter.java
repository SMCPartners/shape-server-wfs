package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Activate_User_Service;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Responsible: Activate a User</br>
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
public class Activate_User_ServiceAdapter implements Activate_User_Service {

    @EJB
    UserDAO userDAO;

    @Inject
    UserExtras userExtras;

    /**
     * Default constructor
     */
    public Activate_User_ServiceAdapter() {
    }

    /**
     * This method is protected so only Admin users can dao it and
     * it requires an active Admin user.
     *
     * @param targetUserId - The user to be activated
     * @throws UseCaseException
     * @see Activate_User_Service
     */
    @Override
    @PUT
    @Path("/activate/{targetUserId}")
    @Produces("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.ORG_ADMIN})
    @Logged
    public BooleanValueDTO activateUser(@PathParam("targetUserId") String targetUserId) throws UseCaseException {
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
            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException)e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }
}
