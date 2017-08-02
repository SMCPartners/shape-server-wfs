package com.smcpartners.shape.shapeserver.gateway.rest.mappedexceptions;

import com.smcpartners.shape.shapeserver.shared.dto.common.ErrorMsgResponse;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Responsibility: Provides a Response for any Throwable exception not already mapped</br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/21/17
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable throwable) {

        if (throwable instanceof WebApplicationException) {
            WebApplicationException wae = (WebApplicationException) throwable;
            return Response
                    .status(wae.getResponse().getStatus())
                    .entity(new ErrorMsgResponse(wae.getResponse().getStatus(), wae.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } else {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorMsgResponse(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                            throwable.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}
