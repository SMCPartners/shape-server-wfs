package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Logout_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import com.smcpartners.shape.shapeserver.usecases.helpers.authentication.LogoutHelperQualifier;
import com.smcpartners.shape.shapeserver.usecases.helpers.authentication.logout.LogoutHelper;

import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Responsible:</br>
 * 1. Application logout </br>
 * <p>
 * <p>
 * Created by johndestefano on 9/30/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. Added support for logout response login to be injected - 6/25/17 - johndestefano</br>
 * </p>
 */
@Path("/common")
public class Logout_ServiceAdapter implements Logout_Service {

    /**
     * Provides logic to create logout response
     */
    @Inject
    @LogoutHelperQualifier
    LogoutHelper logoutHelper;

    /**
     * Default Constructor
     */
    public Logout_ServiceAdapter() {
    }

    @Override
    @PUT
    @Path("/logout/{userid}")
    @Produces("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.REGISTERED})
    @Logged
    public Response logout(@PathParam("userid") String userId) throws UseCaseException {
        try {
            return logoutHelper.logoutResponse();
        } catch (Exception e) {
            throw new UseCaseException(e.getMessage());
        }
    }
}
