package com.smcpartners.shape.shapeserver.usecases.helpers.authentication.login;

import com.smcpartners.shape.shapeserver.shared.dto.shape.UserDTO;

import javax.ws.rs.core.Response;


/**
 * Responsible: All helper required for login policy types implement this type</br>
 *
 * Created By: johndestefano
 * Date: 6/24/17
 *
 */
public interface LoginHelper {

    /**
     * Returns a JaxRS Response to the calling client
     * @param user
     * @param neverExpires
     * @return
     * @throws Exception
     */
    Response loginResponse(UserDTO user, boolean neverExpires) throws Exception;

    /**
     * Check to see if password need to be reset and return the string "true" or "false" for
     * inclusion in the client response.
     *
     * @param user
     * @return
     */
    default String requirePasswordReset(UserDTO user) {
        return user.isResetPwd() ? "true" : "false";
    }

}
