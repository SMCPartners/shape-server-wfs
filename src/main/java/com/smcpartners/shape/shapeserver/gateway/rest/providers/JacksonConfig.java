package com.smcpartners.shape.shapeserver.gateway.rest.providers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * Responsible:<br/>
 * 1.This class will make sure JSON timestamps is written in the format yyyy-MM-dd'T'hh:mm:ss'Z'<br/>
 * <p>
 * Created by johndestefano on 9/18/15.
 * <p>
 * Changes:<b/>
 */

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JacksonConfig implements ContextResolver<ObjectMapper> {

    private final ObjectMapper objectMapper;

    public JacksonConfig() {
        objectMapper = new ObjectMapper();
        // The old way Jackson 1.x way to set date serialization
        //SerializationConfig serConfig = objectMapper.getSerializationConfig();
        //serConfig.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
        //DeserializationConfig deserializationConfig = objectMapper.getDeserializationConfig();
        //deserializationConfig.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }

    @Override
    public ObjectMapper getContext(Class<?> objectType) {
        return objectMapper;
    }
}