package com.smcpartners.shape.shapeserver.shared.exceptions;

import javax.ws.rs.core.Response;

/**
 * Responsibility: </br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/24/17
 */
public class NotAuthorizedToPerformActionException extends UseCaseException {

    public NotAuthorizedToPerformActionException() {
        super("Not authorized to perform this action.", Response.Status.UNAUTHORIZED);
    }
}
