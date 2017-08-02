package com.smcpartners.shape.shapeserver.usecases.helpers.authentication.login;

import com.smcpartners.shape.shapeserver.shared.dto.shape.UserDTO;
import com.smcpartners.shape.shapeserver.shared.utils.JWTUtils;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

/**
 * Responsibility: </br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 6/24/17
 */
public class BearTokenLoginHelper implements LoginHelper {

    @Inject
    JWTUtils jwtUtils;

    /**
     * Default constructor
     *
     */
    public BearTokenLoginHelper() {
    }

    @Override
    /**
     * Return a response for the BearToken login policy
     */
    public Response loginResponse(UserDTO user, boolean neverExpires) throws Exception {
        String token = jwtUtils.generateForBearerToken(user.getId().toUpperCase(), user.getRole(), user.getOrganizationId(), neverExpires);
        return Response.status(Response.Status.OK)
                .entity("{\"token\":\"" + token + "\", \"resetRequired\":" + requirePasswordReset(user) + "}")
                .header("Authorization", "Bearer " + token).build();
    }
}
