package com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations;


import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;

import javax.enterprise.util.Nonbinding;
import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Responsibility: </br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/21/17
 */
@NameBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Secure {
    @Nonbinding SecurityRoleEnum[] value() default {};
}
