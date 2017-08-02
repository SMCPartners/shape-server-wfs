package com.smcpartners.shape.shapeserver.crosscutting.security.rest.features;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;


/**
 * Responsibility: Registers a CORS filter used in handling requests</br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/20/17
 */
@Provider
public class CorsFeature implements Feature {

    @Override
    public boolean configure(FeatureContext context) {
        CorsFilter corsFilter = new CorsFilter();
        corsFilter.getAllowedOrigins().add("*");
        corsFilter.setAllowedMethods("OPTIONS, GET, POST, DELETE, PUT, PATCH");
        context.register(corsFilter);
        return true;
    }
}
