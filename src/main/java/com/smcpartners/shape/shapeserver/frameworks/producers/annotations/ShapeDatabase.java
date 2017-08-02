package com.smcpartners.shape.shapeserver.frameworks.producers.annotations;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Responsible: Used with EntityManager producer to return a Database EntityManager<br/>
 * <p>
 * Created by johndestefano on 9/10/15.
 * <p>
 * Changes:<b/>
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
public @interface ShapeDatabase {
}
