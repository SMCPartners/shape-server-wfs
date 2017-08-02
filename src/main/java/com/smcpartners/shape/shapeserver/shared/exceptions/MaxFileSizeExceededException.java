package com.smcpartners.shape.shapeserver.shared.exceptions;

import javax.ws.rs.core.Response;

/**
 * Responsibility: Indicates a maximum file size exceed condition</br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/24/17
 */
public class MaxFileSizeExceededException extends UseCaseException {

    public MaxFileSizeExceededException() {
        super("The file is to big.", Response.Status.INTERNAL_SERVER_ERROR);
    }
}
