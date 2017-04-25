package com.smcpartners.shape.shapeserver.crosscutting.activitylogging.annotations;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Responsible:</br>
 * 1. Used in conjunction with the LogActivityInterceptor </br>
 * <p>
 * Created by johndestefano on 9/30/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@InterceptorBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface LogActivity {
}
