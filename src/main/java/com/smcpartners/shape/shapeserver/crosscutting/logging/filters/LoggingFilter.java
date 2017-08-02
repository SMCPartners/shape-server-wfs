package com.smcpartners.shape.shapeserver.crosscutting.logging.filters;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.LogDAO;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import org.apache.commons.io.IOUtils;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Responsibility: Log request and response data.</br>
 * 1. Run this filter PreMatch. This is important!</br>
 * 2. For the data that gets logged see the LogDTO bean.</br>
 * Created By: johndestefano
 * Date: 4/22/17
 */
@Logged
@Provider
@PreMatching
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Inject
    private Logger log;

    /**
     * Should logging be done at all. This is a configuration parameter in the project-defaults.
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

    /**
     * Will be populated on secure requests
     */
    @Inject
    private UserExtras userExtras;

    @EJB
    private LogDAO logDAO;


    @Override
    /**
     * Invoked during a request and captures data for logging (if logging is turned on).
     */
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (doLogging) {
            String path = requestContext.getUriInfo().getPath();

            // Log everything
            String headers = null;
            String entity = null;
            if (fullLogging) {
                headers = requestContext.getHeaders().toString();
                entity = getRequestEntityBody(requestContext);
            }

            // Save in CDI request scope
            logDTO.setRequestDt(new Date());
            logDTO.setRequestEntity(entity);
            logDTO.setRequestHeader(headers);
            logDTO.setRequestPath(path);
        }
    }

    @Override
    /**
     * Invoked during a response and captures data for logging (if logging is turned on).
     */
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

                // If this is a request that was secured (@Secure) then
                // get the user id. UserExtras will only be populated
                // if secured.
                logDTO.setUser(userExtras.getUserId());

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

    /**
     * Return the contents of the response
     *
     * @param responseContext
     * @return
     */
    private String getResponseEntityBody(ContainerResponseContext responseContext) {
        Object obj = responseContext.getEntity();
        if (obj != null) {
            return obj.toString();
        } else {
            return "";
        }
    }
}