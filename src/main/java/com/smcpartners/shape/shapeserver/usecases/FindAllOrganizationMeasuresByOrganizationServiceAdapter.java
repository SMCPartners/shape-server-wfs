package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationMeasureDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.FindAllOrganizationMeasuresByOrganizationService;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.NotAuthorizedToPerformActionException;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:<br/>
 * 1. ADMIN and DPH_USER can see measures for any organization. Other users can only see measures
 * for their organization.
 * <p>
 * Created by johndestefano on 11/2/15.
 * <p>
 * Changes:<b/>
 */
@Path("/common")
public class FindAllOrganizationMeasuresByOrganizationServiceAdapter implements FindAllOrganizationMeasuresByOrganizationService {

    @Inject
    private Logger log;

    @EJB
    private OrganizationMeasureDAO organizationMeasureDAO;

    @Context
    private UserExtras userExtras;

    public FindAllOrganizationMeasuresByOrganizationServiceAdapter() {
    }

    @Override
    @GET
    @NoCache
    @Path("/organization_measure/findAllByOrg/{orgId}")
    @Produces("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.ORG_ADMIN, SecurityRoleEnum.REGISTERED, SecurityRoleEnum.DPH_USER})
    @Logged
    public List<OrganizationMeasureDTO> findAllOrganizationMeasuresByOrg(int orgId) throws UseCaseException {
        try {
            // Admin can see all
            // Other only see their organization
            // Get user and find security role
            SecurityRoleEnum reqRole = userExtras.getRole();

            if (reqRole == SecurityRoleEnum.ADMIN || reqRole == SecurityRoleEnum.DPH_USER) {
                return getRetLst(organizationMeasureDAO.findAllOrganizationMeasureByOrgId(orgId));
            } else {
                // Not admin or dph so org ids must match
                if (userExtras.getOrgId() == orgId) {
                    return getRetLst(organizationMeasureDAO.findAllOrganizationMeasureByOrgId(orgId));
                } else {
                    throw new NotAuthorizedToPerformActionException();
                }
            }
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "findAllOrganizationMeasuresByOrg", e.getMessage(), e);
            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException)e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }

    //returns the list of organization measures in the most recent reporting period year. If there is a list of org
    //measures in 2014, 2015, 2016, returns 2016's measures
    private List<OrganizationMeasureDTO> getRetLst(List<OrganizationMeasureDTO> orgMList) {
        List<OrganizationMeasureDTO> retList = new ArrayList<>();
        if (orgMList != null) {
            int reportPeriod = 0;
            for (OrganizationMeasureDTO om : orgMList) {
                if (reportPeriod < om.getReportPeriodYear()) {
                    reportPeriod = om.getReportPeriodYear();
                }
            }

            for (OrganizationMeasureDTO omg: orgMList) {
                if (omg.getReportPeriodYear() == reportPeriod) {
                    retList.add(omg);
                }
            }
        }
        return retList;
    }
}
