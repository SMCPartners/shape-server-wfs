package com.smcpartners.shape.shapeserver.shared.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Responsibility: Indicated the user attempting to perform an action could not be validated as
 * as a system user</br>
 * 1. </br>
 * 2. </br>
 * Created By: bryanhokanson
 * Date: 8/3/17
 */
public class UserEmailDuplicateException extends WebApplicationException {
    public UserEmailDuplicateException() {
        super("Account was not created due to duplicate email address entered",
                Response.Status.EXPECTATION_FAILED);
    }
}
