package com.smcpartners.shape.shapeserver.shared.exceptions;

import javax.ws.rs.core.Response;

/**
 * Responsibility: Indicates a that a mail message could not be sent</br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/24/17
 */
public class SendEmailException extends UseCaseException {
    public SendEmailException() {
        super("Sending email failed, you may have entered your email address incorrectly",
                Response.Status.EXPECTATION_FAILED);
    }
}
