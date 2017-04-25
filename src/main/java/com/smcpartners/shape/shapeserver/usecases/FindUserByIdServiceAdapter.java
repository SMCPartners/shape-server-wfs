package com.smcpartners.shape.shapeserver.usecases;
import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.FindUserByIdService;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.UserDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.NotAuthorizedToPerformActionException;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:</br>
 * 1. ADMIN and DPH_USER can find any user. ORG_ADMIN and REGISTERED user can only find users in their organization.</br
 * <p>
 * Created by johndestefano on 9/28/15.
 * </p>
 * <p>
 * Changes:<br>
 * 1.
 * </p>
 */
@Path("/admin")
public class FindUserByIdServiceAdapter implements FindUserByIdService {

    @Inject
    private Logger log;

    @EJB
    private UserDAO userDAO;

    @Context
    private UserExtras userExtras;

    /**
     * Default constructor
     */
    public FindUserByIdServiceAdapter() {
    }

    @Override
    @GET
    @NoCache
    @Path("/user/find/{targetuserid}")
    @Produces("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.ORG_ADMIN, SecurityRoleEnum.REGISTERED,
                                        SecurityRoleEnum.DPH_USER})
    @Logged
    public UserDTO findUser(String targetUserId) throws UseCaseException {
        try {
            // The ADMIN can see anyone
            // ORG_ADMIN can only see users in their ORG
            SecurityRoleEnum reqUserRole = userExtras.getRole();

            if (SecurityRoleEnum.ADMIN == reqUserRole || SecurityRoleEnum.DPH_USER == reqUserRole) {
                return userDAO.findById(targetUserId);
            } else {
                UserDTO targetUser = userDAO.findById(targetUserId);

                if (targetUser.getOrganizationId() == userExtras.getOrgId()) {
                    return targetUser;
                } else {
                    throw new NotAuthorizedToPerformActionException();
                }
            }

        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "findUser", e.getMessage(), e);
            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException)e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }
}
