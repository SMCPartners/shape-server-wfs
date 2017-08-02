package com.smcpartners.shape.shapeserver.crosscutting.security.rest.features;

import com.smcpartners.shape.shapeserver.crosscutting.security.rest.filters.XSSSanatizerFilter;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

/**
 * Responsibility: Register a XSS filter for responses</br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/26/17
 */
@Provider
public class XSSSanatizerFeature implements Feature {

    @Override
    public boolean configure(FeatureContext context) {
        XSSSanatizerFilter xssFilter = new XSSSanatizerFilter();
        context.register(xssFilter);
        return true;
    }
}
