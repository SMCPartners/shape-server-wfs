package com.smcpartners.shape.shapeserver.shared.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Responsible:</br>
 * 1. Thrown by a use case on an exception</br
 * <p>
 * Created by johndestefano on 9/28/15.
 * </p>
 * <p>
 * Changes:<br>
 * 1.
 * </p>
 */
public class UseCaseException extends WebApplicationException {

    public UseCaseException(String message) {
        super(message, Response.Status.INTERNAL_SERVER_ERROR);
    }

    public UseCaseException(String message, Response.Status status) {
        super(message, status);
    }
}
