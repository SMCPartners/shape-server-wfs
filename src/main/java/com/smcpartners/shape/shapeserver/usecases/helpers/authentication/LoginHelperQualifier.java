package com.smcpartners.shape.shapeserver.usecases.helpers.authentication;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Responsible: CDI Qualifier for Login/Logout helpers</br>
 *
 * Created by: johndestefano
 * Date: 07/17/2017
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface LoginHelperQualifier {
}
