package com.smcpartners.shape.shapeserver.gateway.rest.services;


import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Responsible:</br>
 * 1.  Support REST framework</br>
 * <p>
 * Created by johndestefano on 9/30/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Path("/common")
public interface Logout_Service {

    @PUT
    @Path("/logout/{userid}")
    @Produces("application/json")
    Response logout(@PathParam("userid") String userId) throws UseCaseException;
}
