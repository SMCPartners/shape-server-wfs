package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.MeasureDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationMeasureDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Get_Measure_Years_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:</br>
 * 1. Any user can find the years a measure has been recorded</br>
 * <p>
 * Created by johndestefano on 3/15/16.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Path("/common")
public class Get_Measure_Years_ServiceAdapter implements Get_Measure_Years_Service {

    @Inject
    private Logger log;

    @EJB
    private MeasureDAO measureDAO;

    @EJB
    private OrganizationMeasureDAO organizationMeasureDAO;

    /**
     * Default Constructor
     */
    public Get_Measure_Years_ServiceAdapter() {
    }

    @Override
    @GET
    @NoCache
    @Path("/get/measure_years/{orgId}/{measureId}")
    @Produces("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.ORG_ADMIN, SecurityRoleEnum.REGISTERED,
            SecurityRoleEnum.DPH_USER})
    @Logged
    public List<Integer> getMeasureYears(int orgId, int measureId) throws UseCaseException {
        try {

            List<OrganizationMeasureDTO> measureList = organizationMeasureDAO.findAllOrganizationMeasureByOrgId(orgId);
            List<Integer> retLst = new ArrayList<>();

            if (measureList != null) {
                for (OrganizationMeasureDTO mL : measureList) {
                    if (mL.getReportPeriodYear() != null && mL.getMeasureId() == measureId) {
                        retLst.add(mL.getReportPeriodYear());
                    }
                }
            }

            Set duplicateRemove = new HashSet<>(retLst);
            retLst.clear();
            if (duplicateRemove.size() > 0) {
                retLst.addAll(duplicateRemove);
            }
            Collections.sort(retLst);
            Collections.reverse(retLst);

            return retLst;
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "getMeasureYears", e.getMessage(), e);
            throw new UseCaseException(e.getMessage());
        }
    }
}
