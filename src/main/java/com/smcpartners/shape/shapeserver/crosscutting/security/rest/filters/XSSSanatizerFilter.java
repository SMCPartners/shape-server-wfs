package com.smcpartners.shape.shapeserver.crosscutting.security.rest.filters;

import org.owasp.esapi.ESAPI;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Responsibility: XSS Sanitizer for responses</br>
 * 1. Removes various suspicious response content.</br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/26/17
 */
public class XSSSanatizerFilter implements ContainerResponseFilter {

    @Inject
    private Logger log;

    /**
     * Possible attack patterns
     */
    private static Pattern[] patterns = new Pattern[]{
            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    };

    /**
     * Default constructor
     */
    public XSSSanatizerFilter() {
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {

        // Entity
        Object entity = containerResponseContext.getEntity();
        if (entity != null && entity instanceof String) {
            String escapedMsg = stripXSS((String) entity);
            containerResponseContext.setEntity(escapedMsg);
        }
    }

    /**
     * Strips out suspicious content
     *
     * @param value
     * @return
     */
    private String stripXSS(String value) {
        if (value != null) {
            value = ESAPI.encoder().canonicalize(value);

            // Avoid null characters
            value = value.replaceAll("\0", "");

            // Remove all sections that match a pattern
            for (Pattern scriptPattern : patterns) {
                value = scriptPattern.matcher(value).replaceAll("");
            }
        }
        return value;
    }
}
