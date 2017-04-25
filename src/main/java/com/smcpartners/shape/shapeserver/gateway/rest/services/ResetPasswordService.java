package com.smcpartners.shape.shapeserver.gateway.rest.services;

import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.PasswordUpdateRequestDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Responsible:<br/>
 * 1. Gateway for reset password transaction
 * <p>
 * Created by bhokanson on 11/30/15.
 * <p>
 * Changes:<b/>
 */
@Path("/common")
public interface ResetPasswordService {

    @POST
    @Path("/user/reset_password")
    @Produces("application/json")
    @Consumes("application/json")
    BooleanValueDTO resetPassword(PasswordUpdateRequestDTO userReq) throws UseCaseException;
}
