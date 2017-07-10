package com.smcpartners.shape.shapeserver.usecases.helpers.authentication.logout;

import javax.ws.rs.core.Response;

public interface LogoutHelper {
    Response logoutResponse() throws Exception;
}
