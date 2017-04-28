package com.smcpartners.shape.shapeserver.gateway.rest.services;


import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.UserDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Responsible:<br/>
 * 1. Support REST framework
 * <p>
 * Created by bhokanson on 11/30/15.
 * <p>
 * Changes:<b/>
 */
@Path("/admin")
public interface Edit_User_Service {

    @POST
    @Path("/user/edit")
    @Produces("application/json")
    @Consumes("application/json")
    BooleanValueDTO editUser(UserDTO user) throws UseCaseException;
}
