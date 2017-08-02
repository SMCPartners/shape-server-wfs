package com.smcpartners.shape.shapeserver.shared.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Responsibility: Indicated the user attempting to perform an action could not be validated as
 * as a system user</br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/21/17
 */
public class UserNotFoundException extends WebApplicationException {

    public UserNotFoundException(String message, Response.Status status) {
        super(message, status);
    }
}
