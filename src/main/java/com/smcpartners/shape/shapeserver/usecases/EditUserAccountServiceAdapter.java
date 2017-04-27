package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.EditUserAccountService;
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
import javax.ws.rs.core.Context;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:</br>
 * 1. Edit the users account. Can't change the organization or the role. </br>
 * <p>
 * Created by johndestefano on 3/15/16.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Path("/common")
public class EditUserAccountServiceAdapter implements EditUserAccountService {

    @Inject
    private Logger log;

    @EJB
    private UserDAO userDAO;

    @Inject
    private UserExtras userExtras;


    public EditUserAccountServiceAdapter() {
    }

    @Override
    @POST
    @Path("/account/edit")
    @Produces("application/json")
    @Consumes("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.ORG_ADMIN, SecurityRoleEnum.REGISTERED,
                                        SecurityRoleEnum.DPH_USER})
    @Logged
    public BooleanValueDTO editUserAccount(UserDTO user) throws UseCaseException {
        try {
            // The account must be for the requesting user
            // Must be the same organization
            // Must be the same role
            UserDTO targetUser = userDAO.findById(user.getId());

            if (userExtras.getUserId().equalsIgnoreCase(user.getId())
                    && targetUser.getId().equalsIgnoreCase(user.getId())
                    && userExtras.getOrgId() == user.getOrganizationId()
                    && targetUser.getOrganizationId() == user.getOrganizationId()
                    && userExtras.getRole().toString().equalsIgnoreCase(user.getRole())) {
                userDAO.update(user, user.getId());
            } else {
                throw new NotAuthorizedToPerformActionException();
            }

            // Return value
            return new BooleanValueDTO(true);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "editUserAccount", e.getMessage(), e);

            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException)e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }
}