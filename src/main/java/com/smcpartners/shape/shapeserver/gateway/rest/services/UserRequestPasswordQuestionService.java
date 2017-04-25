package com.smcpartners.shape.shapeserver.gateway.rest.services;

import com.smcpartners.shape.shapeserver.shared.dto.shape.response.UserPasswordQuestionResponseDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Created by bhokanson on 12/3/2015.
 */
@Path("/common")
public interface UserRequestPasswordQuestionService {

    @GET
    @Path("/password_question/{userId}")
    @Produces("application/json")
    UserPasswordQuestionResponseDTO requestPasswordQuestion(@PathParam("userId") String userId) throws UseCaseException;

}