package com.smcpartners.shape.shapeserver.usecases.form_builders.annotations;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Responsible: Used in CDI production of Template Engine<br/>
 * <p>
 * Created by johndestefano on 9/10/15.
 * <p>
 * Changes:<b/>
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
public @interface ConfiguredTemplateEngine {
}
