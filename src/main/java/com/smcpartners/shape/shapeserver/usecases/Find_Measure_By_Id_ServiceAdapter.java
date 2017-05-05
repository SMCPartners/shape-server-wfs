package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.MeasureDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Find_Measure_By_Id_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.MeasureDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:</br>
 * 1. Any user can find a measure by its id
 * <p>
 * Created by johndestefano on 3/15/16.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Path("/common")
public class Find_Measure_By_Id_ServiceAdapter implements Find_Measure_By_Id_Service {

    @Inject
    private Logger log;

    @EJB
    private MeasureDAO measureDAO;

    @Inject
    private UserExtras userExtras;

    public Find_Measure_By_Id_ServiceAdapter() {
    }

    @Override
    @GET
    @NoCache
    @Path("/find/measure_by_id/{measureId}")
    @Produces("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.DPH_USER, SecurityRoleEnum.ORG_ADMIN, SecurityRoleEnum.REGISTERED})
    @Logged
    public MeasureDTO findMeasureById(@PathParam("measureId") int measureId) throws UseCaseException {
        try {
            return measureDAO.findById(measureId);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "findMeasureById", e.getMessage(), e);
            throw new UseCaseException(e.getMessage());
        }
    }
}