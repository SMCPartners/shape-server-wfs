package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.FindAllUsersService;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.UserDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:<br/>
 * 1. The ADMIN role can see everybody. The ORG_ADMIN can see others in their ORG
 * Everyone else gets nothing
 * <p>
 * Created by johndestefano on 9/11/15.
 * <p>
 * Changes:<b/>
 * </p>
 */
@Path("/admin")
public class FindAllUsersServiceAdapter implements FindAllUsersService {

    @Inject
    private Logger log;

    @EJB
    private UserDAO userDAO;

    @Inject
    private UserExtras userExtras;

    /**
     * Default constructor
     */
    public FindAllUsersServiceAdapter() {
    }

    @Override
    @GET
    @NoCache
    @Path("/user/findAll")
    @Produces("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.DPH_USER, SecurityRoleEnum.ORG_ADMIN})
    @Logged
    public List<UserDTO> findAllUser() throws UseCaseException {
        try {
            List<UserDTO> lst = null;

            SecurityRoleEnum reqUserRole = userExtras.getRole();

            // The ADMIN role can see everybody
            // The ORG_ADMIN can see others in their ORG
            // Everyone else gets nothing
            if (reqUserRole == SecurityRoleEnum.ADMIN || reqUserRole == SecurityRoleEnum.DPH_USER) {
                lst = userDAO.findAll();
            } else if (reqUserRole == SecurityRoleEnum.ORG_ADMIN) {
                // Find the user
                lst = userDAO.findByOrg(userExtras.getOrgId());
            }
            return lst;
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "findAllUser", e.getMessage(), e);
            throw new UseCaseException(e.getMessage());
        }
    }
}
