package com.smcpartners.shape.shapeserver.gateway.rest.services;

import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.UserPasswordResetRequestDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by bhokanson on 12/3/2015.
 */
@Path("/common")
public interface Request_Password_Change_Service {

    @POST
    @Path("/password_change")
    @Produces("application/json")
    @Consumes("application/json")
    BooleanValueDTO requestPasswordChange(UserPasswordResetRequestDTO pwdResetReq) throws UseCaseException;

}