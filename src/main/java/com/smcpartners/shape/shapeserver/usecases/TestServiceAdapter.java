package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.MeasureDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.TestService;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.MeasureDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import com.smcpartners.shape.shapeserver.usecases.form_builders.annotations.ConfiguredTemplateEngine;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsibility: </br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/9/17
 */
@Path("/test")
public class TestServiceAdapter implements TestService  {

    @Inject
    private Logger log;

    @Inject
    @ConfigurationValue("com.smc.server-core.mail.MAIL_SERVER_ADDRESS")
    private String mailAdd;

    @EJB
    private MeasureDAO measureDAO;

    @Inject
    private UserExtras userExtras;

    @Inject
    @ConfiguredTemplateEngine
    private ITemplateEngine templateEngine;

    public TestServiceAdapter() {
    }

    @Override
    @GET
    @Path("/testmethod")
    @Secure({SecurityRoleEnum.ADMIN})
    @Logged
    public String test() {
        return "Hello World: " + userExtras.getUserId();
    }

    @Override
    @GET
    @NoCache
    @Path("/find/measure_by_id/{measureId}")
    @Produces("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.DPH_USER, SecurityRoleEnum.ORG_ADMIN, SecurityRoleEnum.REGISTERED})
    @Logged
    public String findMeasureById(@PathParam("measureId") int measureId) throws UseCaseException {
        try {
            MeasureDTO mDTO = measureDAO.findById(measureId);
            Context ctx = new Context();
            ctx.setVariable("measure", mDTO);
            String htmlContent = this.templateEngine.process("html/measuretest", ctx);
            return htmlContent;
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "findMeasureById", e.getMessage(), e);
            throw new UseCaseException(e.getMessage());
        }
    }
}
