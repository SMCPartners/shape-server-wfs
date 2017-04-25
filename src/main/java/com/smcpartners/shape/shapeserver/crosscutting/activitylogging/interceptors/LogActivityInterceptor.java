package com.smcpartners.shape.shapeserver.crosscutting.activitylogging.interceptors;


import com.smcpartners.shape.shapeserver.crosscutting.activitylogging.annotations.LogActivity;
import com.smcpartners.shape.shapeserver.crosscutting.activitylogging.exceptions.ActivityLogException;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * Responsible:</br>
 * 1. Interceptor used with logging. Currently not being used.</br>
 * <p>
 * Created by johndestefano on 9/30/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Interceptor
@LogActivity
public class LogActivityInterceptor {

    @AroundInvoke
    public Object logActivity(InvocationContext ctx) throws Exception {
        try {
            return ctx.proceed();
        } catch (Exception e) {
            throw new ActivityLogException();
        }
    }
}
