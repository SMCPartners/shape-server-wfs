package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.ProviderDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Find_All_Providers_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.ProviderDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:</br>
 * 1. Return a list of providers. The ADMIN and DPH_USER can see all providers.
 * The ORG_ADMIN and REGISTERED user can only see providers associated
 * with there organization</br>
 * <p>
 * Created by johndestefano on 3/15/16.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Path("/common")
public class Find_All_Providers_ServiceAdapter implements Find_All_Providers_Service {

    @Inject
    private Logger log;

    @EJB
    ProviderDAO providerDAO;

    @Inject
    UserExtras userExtras;

    public Find_All_Providers_ServiceAdapter() {
    }

    /**
     * Return a list of providers. The ADMIN and DPH_USER can see all providers.
     * The ORG_ADMIN and REGISTERED user can only see providers associated
     * with there organization.
     *
     * @return
     * @throws UseCaseException
     */
    @Override
    @GET
    @NoCache
    @Path("/provider/findAll")
    @Produces("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.DPH_USER, SecurityRoleEnum.ORG_ADMIN, SecurityRoleEnum.REGISTERED})
    @Logged
    public List<ProviderDTO> findAllProviders() throws UseCaseException {
        try {
            // Get the requester role from the security token
            SecurityRoleEnum reqUserRole = userExtras.getRole();
            List<ProviderDTO> retLst = null;

            // ADMIN or DPH_USER get everything
            if (SecurityRoleEnum.ADMIN == reqUserRole || SecurityRoleEnum.DPH_USER == reqUserRole) {
                retLst = providerDAO.findAll();
            } else if (SecurityRoleEnum.ORG_ADMIN == reqUserRole || SecurityRoleEnum.REGISTERED == reqUserRole) {
                // ORG_ADMIN or REGISTERED get only the requester organization
                retLst = providerDAO.findAllByOrg(userExtras.getOrgId());
            }

            // Return results
            return retLst;
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "findAllProviders", e.getMessage(), e);
            throw new UseCaseException(e.getMessage());
        }
    }
}