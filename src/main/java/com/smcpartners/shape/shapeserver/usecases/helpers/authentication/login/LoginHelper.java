package com.smcpartners.shape.shapeserver.usecases.helpers.authentication.login;

import com.smcpartners.shape.shapeserver.shared.dto.shape.UserDTO;

import javax.ws.rs.core.Response;

public interface LoginHelper {
    Response loginResponse(UserDTO user, boolean neverExpires) throws Exception;

    default String requirePasswordReset(UserDTO user) {
        return user.isResetPwd() ? "true" : "false";
    }

}
