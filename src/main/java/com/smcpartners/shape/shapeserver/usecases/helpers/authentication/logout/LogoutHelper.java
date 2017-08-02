package com.smcpartners.shape.shapeserver.usecases.helpers.authentication.logout;

import javax.ws.rs.core.Response;

/**
 * Responsible: All helper required for logout policy types implement this type</br>
 *
 * Created By: johndestefano
 * Date: 6/24/17
 *
 */
public interface LogoutHelper {
    Response logoutResponse() throws Exception;
}
