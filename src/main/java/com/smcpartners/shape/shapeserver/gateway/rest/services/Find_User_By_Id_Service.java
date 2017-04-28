package com.smcpartners.shape.shapeserver.gateway.rest.services;
import com.smcpartners.shape.shapeserver.shared.dto.shape.UserDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ws.rs.GET;
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
public interface Find_User_By_Id_Service {

    /**
     * Find a user
     *
     * @return
     * @throws UseCaseException
     */
    @GET
    @NoCache
    @Path("/user/find/{targetuserid}")
    @Produces("application/json")
    UserDTO findUser(@PathParam("targetuserid") String targetUserId) throws UseCaseException;

}
