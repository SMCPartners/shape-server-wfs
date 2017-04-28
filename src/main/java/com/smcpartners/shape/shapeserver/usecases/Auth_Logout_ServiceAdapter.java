package com.smcpartners.shape.shapeserver.usecases;



import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Auth_Logout_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:</br>
 * 1.Application logout </br>
 * <p>
 * <p>
 * Created by johndestefano on 9/30/15.
 * </p>
 * <p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
//TODO: Seems like any user role should be able to call this?
//TODO: The logout functionality is handled on the front end, not sure if we should delete this? BH 3/25
@Path("/common")
public class Auth_Logout_ServiceAdapter implements Auth_Logout_Service {

    @Inject
    private Logger log;

    /**
     * Default Constructor
     */
    public Auth_Logout_ServiceAdapter() {
    }

    @Override
    @PUT
    @Path("/logout/{userid}")
    @Produces("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.REGISTERED})
    @Logged
    public Response logout(@PathParam("userid") String userId) throws UseCaseException {
        try {
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "logout", e.getMessage(), e);
            throw new UseCaseException(e.getMessage());
        }
    }
}
