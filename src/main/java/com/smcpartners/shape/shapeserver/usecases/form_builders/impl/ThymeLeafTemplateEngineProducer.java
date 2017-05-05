package com.smcpartners.shape.shapeserver.usecases.form_builders.impl;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import java.util.Collections;
import com.smcpartners.shape.shapeserver.usecases.form_builders.annotations.ConfiguredTemplateEngine;

/**
 * Responsibility: </br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 5/3/17
 */
public class ThymeLeafTemplateEngineProducer {

    public ThymeLeafTemplateEngineProducer() {
    }

    @Produces
    @Singleton
    @ConfiguredTemplateEngine
    public  ITemplateEngine getConfiguredTemplateEngine() {
        // Construct engine
        TemplateEngine tEngine = new TemplateEngine();

        // Construct resolver
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(Integer.valueOf(2));
        templateResolver.setResolvablePatterns(Collections.singleton("html/*"));
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);

        // Add resolver to engine
        tEngine.addTemplateResolver(templateResolver);

        // Set to internal pointer
        return tEngine;
    }
}
