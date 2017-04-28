package com.smcpartners.shape.shapeserver.gateway.rest.services;

import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Responsible:</br>
 * 1.  Support REST framework</br>
 * <p>
 * Created by johndestefano on 3/15/16.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Path("/admin")
public interface Inactivate_User_Service {
    /**
     * Inactivate the user
     *
     * @throws UseCaseException
     */
    @PUT
    @Path("/inactivate/{targetuserid}")
    @Produces("application/json")
    BooleanValueDTO inactivateUser(@PathParam("targetuserid") String targetUserId) throws UseCaseException;
}
