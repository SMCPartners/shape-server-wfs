package com.smcpartners.shape.shapeserver.usecases.helpers.authentication;

import com.smcpartners.shape.shapeserver.usecases.helpers.authentication.login.BearTokenLoginHelper;
import com.smcpartners.shape.shapeserver.usecases.helpers.authentication.login.DoubleCookieLoginHelper;
import com.smcpartners.shape.shapeserver.usecases.helpers.authentication.login.LoginHelper;
import com.smcpartners.shape.shapeserver.usecases.helpers.authentication.logout.BearTokenLogoutHelper;
import com.smcpartners.shape.shapeserver.usecases.helpers.authentication.logout.DoubleCookieLogoutHelper;
import com.smcpartners.shape.shapeserver.usecases.helpers.authentication.logout.LogoutHelper;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.enterprise.inject.New;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;

/**
 * Responsibility: CDI producer for Login/Logout policy helpers</br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 6/24/17
 */
@ApplicationScoped
public class AuthenticationHelperProducer {

    /**
     * If cookie are in use then the double cookie security approach is in force
     */
    @Inject
    @ConfigurationValue("com.smc.server-core.security.jwtEmbededCookies.useCookies")
    boolean useCookies;

    /**
     * Returns the appropriate in login helper depending on the value of
     * useCookie
     *
     * @param doubleCookieLoginHelper - When useCookie is true
     * @param bearTokenLoginHelper - When useCookie is false
     * @return
     */
    @Produces
    @LoginHelperQualifier
    public LoginHelper getLoginHelper(@New DoubleCookieLoginHelper doubleCookieLoginHelper,
                                      @New BearTokenLoginHelper bearTokenLoginHelper) {
        if (useCookies) {
            return doubleCookieLoginHelper;
        } else {
            return bearTokenLoginHelper;
        }
    }

    /**
     * Returns the appropriate in logout helper depending on the value of
     * useCookie
     *
     * @param doubleCookieLogoutHelper - When useCookie is true
     * @param bearTokenLogoutHelper - When useCookie is false
     * @return
     */
    @Produces
    @LogoutHelperQualifier
    public LogoutHelper getLogoutHelper(@New DoubleCookieLogoutHelper doubleCookieLogoutHelper,
                                        @New BearTokenLogoutHelper bearTokenLogoutHelper) {
        if (useCookies) {
            return doubleCookieLogoutHelper;
        } else {
            return bearTokenLogoutHelper;
        }
    }
}
