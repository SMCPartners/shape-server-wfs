package com.smcpartners.shape.shapeserver.shared.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Responsibility: Indicates a that a user attempting to perform some action needs to have their password
 * reset first</br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/21/17
 */
public class UserNeedsPwdResetException extends WebApplicationException {
    public UserNeedsPwdResetException(String message, Response.Status status) {
        super(message, status);
    }
}
