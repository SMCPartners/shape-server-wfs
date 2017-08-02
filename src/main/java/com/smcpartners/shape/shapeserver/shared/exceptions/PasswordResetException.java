package com.smcpartners.shape.shapeserver.shared.exceptions;

import javax.ws.rs.core.Response;

/**
 * Responsibility: Indicates a password reset failed</br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/24/17
 */
public class PasswordResetException extends UseCaseException {
    public PasswordResetException(String message) {
        super(message, Response.Status.EXPECTATION_FAILED);
    }
}
