package com.smcpartners.shape.shapeserver.gateway.rest.services;

import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.ResetPasswordRequestDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by jjdestef3 6/20/2017.
 */
@Path("/common")
public interface Send_Password_Reset_Service {

    @POST
    @Path("/sendpasswordreset")
    @Produces("application/json")
    @Consumes("application/json")
    BooleanValueDTO sendPasswordReset(ResetPasswordRequestDTO resetPasswordRequestDTO) throws UseCaseException;

}