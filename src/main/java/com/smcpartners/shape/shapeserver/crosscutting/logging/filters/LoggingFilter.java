package com.smcpartners.shape.shapeserver.crosscutting.logging.filters;

/**
 * Responsibility: </br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/22/17
 */

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.logging.dto.LogDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

@Logged
@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Inject
    private Logger log;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        LogDTO logDTO = new LogDTO();
        String userId = getUserId(requestContext);
        logDTO.setUser(userId);
        userId = userId == null ? "unknown" : userId;
        String path = requestContext.getUriInfo().getPath();
        String headers = requestContext.getHeaders().toString();
        String entity = getRequestEntityBody(requestContext);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("Header: ").append(responseContext.getHeaders());
        sb.append(" - Entity: ").append(getResponseEntityBody(responseContext));
        log.info("HTTP RESPONSE : " + sb.toString());
    }

    /**
     * Read the contents of the message body
     *
     * @param requestContext
     * @return
     */
    private String getRequestEntityBody(ContainerRequestContext requestContext) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = requestContext.getEntityStream();

        final StringBuilder b = new StringBuilder();
        try {
            IOUtils.copy(in, out);

            byte[] requestEntity = out.toByteArray();
            if (requestEntity.length == 0) {
                b.append("").append("\n");
            } else {
                b.append(new String(requestEntity)).append("\n");
            }

            // Since we've read the original stream we need to
            // Create a new one form the copy
            requestContext.setEntityStream(new ByteArrayInputStream(requestEntity));

        } catch (IOException ex) {
            //Handle logging error
        }
        return b.toString();
    }

    private String getResponseEntityBody(ContainerResponseContext responseContext) {
        Object obj = responseContext.getEntity();
        if (obj != null) {
            return obj.toString();
        } else {
            return "";
        }
    }

    /**
     * Get the user id from the context
     *
     * @param requestContext
     * @return - Requesting users id or null if not present
     */
    private String getUserId(ContainerRequestContext requestContext) {
        String userId = null;
        try {
            UserExtras userExtras = (UserExtras) ResteasyProviderFactory.getContextDataMap().get(UserExtras.class);
            userId = userExtras.getUserId();
        } catch (Exception e) {
            // Intentionally blank
        }
        return userId;
    }

}