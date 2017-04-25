package com.smcpartners.shape.shapeserver.gateway.rest.services;

import com.smcpartners.shape.shapeserver.shared.dto.shape.request.CreateUserRequestDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.response.CreateUserResponseDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Responsible:<br/>
 * 1. Support REST framework
 * <p>
 * Created by johndestefano on 11/2/15.
 * <p>
 * Changes:<b/>
 */
@Path("/admin")
public interface AuthCreateUserAccountService {

    @POST
    @Path("/create/user")
    @Produces("application/json")
    @Consumes("application/json")
    CreateUserResponseDTO createUserAccount(CreateUserRequestDTO dto) throws UseCaseException;
}
