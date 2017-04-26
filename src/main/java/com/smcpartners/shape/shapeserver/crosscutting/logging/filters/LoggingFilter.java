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
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.LogDAO;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Logged
@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Inject
    private Logger log;

    /**
     * Should logging be done at all
     */
    @Inject
    @ConfigurationValue("com.smc.server-core.logging.do_logging")
    private boolean doLogging;

    /**
     * If logging should be done then should the process log entity contents and headers
     */
    @Inject
    @ConfigurationValue("com.smc.server-core.logging.full_logging")
    private boolean fullLogging;

    @Inject
    private LogDTO logDTO;

    @EJB
    private LogDAO logDAO;


    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (doLogging) {
            String userId = getUserId(requestContext);
            userId = userId == null ? "unknown" : userId;
            String path = requestContext.getUriInfo().getPath();

            // Log everything
            String headers = null;
            String entity = null;
            if (fullLogging) {
                headers = requestContext.getHeaders().toString();
                entity = getRequestEntityBody(requestContext);
            }

            // Save in CDI request scope
            logDTO.setUser(userId);
            logDTO.setRequestDt(new Date());
            logDTO.setRequestEntity(entity);
            logDTO.setRequestHeader(headers);
            logDTO.setRequestPath(path);
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        if (doLogging) {
            try {
                // Log everything
                String headers = null;
                String entity = null;
                if (fullLogging) {
                    headers = responseContext.getHeaders().toString();
                    entity = getResponseEntityBody(responseContext);
                }
                logDTO.setResponseDt(new Date());
                logDTO.setResponseEntity(entity);
                logDTO.setResponseHeader(headers);

                // Update database
                logDAO.create(logDTO);
            } catch (Exception e) {
                log.logp(Level.SEVERE, this.getClass().getName(), "filter", e.getMessage(), e);
                throw new WebApplicationException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * Read the contents of the message body
     *
     * @param requestContext
     * @return
     */
    private String getRequestEntityBody(ContainerRequestContext requestContext) throws IOException {
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
            throw new IOException(ex);
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