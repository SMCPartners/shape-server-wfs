package com.smcpartners.shape.shapeserver.gateway.rest.services;

import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.NameStringValDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by johndestefano on 6/23/2017.
 */
@Path("/common")
public interface Forgot_Username_Service {

    @POST
    @Path("/forgotusername")
    @Produces("application/json")
    @Consumes("application/json")
    BooleanValueDTO forgotUsername(NameStringValDTO emailAddress) throws UseCaseException;

}