package com.smcpartners.shape.shapeserver.usecases.helpers.authentication.logout;

import javax.ws.rs.core.Response;

/**
 * Responsibility: Implements logout logic when using bear token security</br>
 * 1. </br>
 * Created By: johndestefano
 * Date: 6/25/17
 */
public class BearTokenLogoutHelper implements LogoutHelper {

    /**
     * Default Constructor
     */
    public BearTokenLogoutHelper() {
    }

    @Override
    /**
     * Return logout response to the client
     */
    public Response logoutResponse() throws Exception {
        return Response.status(Response.Status.OK).build();
    }
}
