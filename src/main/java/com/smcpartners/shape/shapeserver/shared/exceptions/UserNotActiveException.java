package com.smcpartners.shape.shapeserver.shared.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Responsibility: Indicates a user attempting to perform an action does not have an active account</br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/21/17
 */
public class UserNotActiveException extends WebApplicationException {
    public UserNotActiveException(String message, Response.Status status) {
        super(message, status);
    }
}
