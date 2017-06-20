package com.smcpartners.shape.shapeserver.gateway.rest.services;

import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.CreateUserRequestDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.ForgotPasswordRequestDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.response.ForgotPasswordResponseDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by bhokanson on 12/3/2015.
 */
@Path("/common")
public interface Forgot_Password_Service {

    @POST
    @Path("/forgotpassword")
    @Produces("application/json")
    @Consumes("application/json")
    ForgotPasswordResponseDTO forgotUserPassword(ForgotPasswordRequestDTO forgotPwdRequset) throws UseCaseException;

}