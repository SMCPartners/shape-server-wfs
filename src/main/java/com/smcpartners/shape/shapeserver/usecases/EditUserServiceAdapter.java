package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.EditUserService;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.UserDTO;
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
 * Responsible:<br/>
 * 1. Admin level users can edit users. The ORG_ADMIN can only edit users for their
 * organization and only if they are not ADMINs. No one can edit a users organization and ORG_ADMIN can't make a user
 * an ADMIN<p>
 * Created by johndestefano on 3/15/16.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Path("/admin")
public class EditUserServiceAdapter implements EditUserService {

    @Inject
    private Logger log;

    @EJB
    private UserDAO userDAO;

    @Inject
    private UserExtras userExtras;

    /**
     * Default Constructor
     */
    public EditUserServiceAdapter() {
    }

    @Override
    @POST
    @Path("/user/edit")
    @Produces("application/json")
    @Consumes("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.ORG_ADMIN})
    @Logged
    public BooleanValueDTO editUser(UserDTO user) throws UseCaseException {
        try {
            // ADMIN can edit user but not the users organization
            UserDTO targetUser = userDAO.findById(user.getId());
            if (userExtras.getRole() == SecurityRoleEnum.ADMIN) {
                if (targetUser.getOrganizationId() == user.getOrganizationId()) {
                    userDAO.update(user, user.getId());
                } else {
                    throw new IllegalAccessException("Your account does not have permission to edit this user.");
                }
            } else {
                // ORG_ADMIN can edit user for their organization but not change
                // the organization or change to role to ADMIN
                // ORG_ADMIN Can't edit ADMIN
                if (!targetUser.getRole().equals("ADMIN")
                        // Must be the same organization as requester
                        && targetUser.getOrganizationId() == userExtras.getOrgId()
                        // No funny business with the incoming data
                        && user.getOrganizationId() == targetUser.getOrganizationId()
                        // ORG_ADMIN can't make the role ADMIN
                        && !user.getRole().equalsIgnoreCase("ADMIN")){
                    userDAO.update(user, user.getId());
                } else {
                    throw new NotAuthorizedToPerformActionException();
                }
            }

            // Return value
            return new BooleanValueDTO(true);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "editUser", e.getMessage(), e);
            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException)e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }
}
