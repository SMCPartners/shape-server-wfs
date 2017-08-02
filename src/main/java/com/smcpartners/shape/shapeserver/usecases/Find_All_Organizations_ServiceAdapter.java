package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Find_All_Organizations_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:<br/>
 * 1. ADMIN and DPH_User roles can see all organizations. ORG_ADMIN and REGISTERED user can see only their organization
 * <p>
 * Created by johndestefano on 11/2/15.
 * <p>
 * Changes:<b/>
 */
@Path("/admin")
public class Find_All_Organizations_ServiceAdapter implements Find_All_Organizations_Service {

    @Inject
    private Logger log;

    @EJB
    OrganizationDAO organizationDAO;

    @Inject
    UserExtras userExtras;

    public Find_All_Organizations_ServiceAdapter() {
    }

    @Override
    @GET
    @NoCache
    @Path("/organization/findAll")
    @Produces("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.DPH_USER, SecurityRoleEnum.ORG_ADMIN, SecurityRoleEnum.REGISTERED})
    public List<OrganizationDTO> findAllOrganizations() throws UseCaseException {
        try {
            List<OrganizationDTO> retLst = new ArrayList<>();

            // ADMIN role can see all organizations
            // ORG_ADMIN can see only their organization
            SecurityRoleEnum reqUserRole = userExtras.getRole();

            if (SecurityRoleEnum.ADMIN == reqUserRole || SecurityRoleEnum.DPH_USER == reqUserRole) {
                List<OrganizationDTO> lst = organizationDAO.findAll();
                lst.forEach(l -> {
                    OrganizationDTO dto = new OrganizationDTO();
                    dto.setId(l.getId());
                    dto.setName(l.getName());
                    dto.setActive(l.isActive());
                    dto.setAddressStreet(l.getAddressStreet());
                    dto.setAddressCity(l.getAddressCity());
                    dto.setAddressState(l.getAddressState());
                    dto.setAddressZip(l.getAddressZip());
                    dto.setPhone(l.getPhone());
                    dto.setPrimaryContactPhone(l.getPrimaryContactPhone());
                    dto.setPrimaryContactName(l.getPrimaryContactName());
                    dto.setPrimaryContactEmail(l.getPrimaryContactEmail());
                    dto.setPrimaryContactRole(l.getPrimaryContactRole());
                    retLst.add(dto);
                });
            } else if (SecurityRoleEnum.ORG_ADMIN == reqUserRole || SecurityRoleEnum.REGISTERED == reqUserRole) {
                // Find the organization
                OrganizationDTO orgDTO = organizationDAO.findById(userExtras.getOrgId());

                // Add to lst
                OrganizationDTO dto = new OrganizationDTO();
                dto.setId(orgDTO.getId());
                dto.setName(orgDTO.getName());
                dto.setActive(orgDTO.isActive());
                dto.setAddressStreet(orgDTO.getAddressStreet());
                dto.setAddressCity(orgDTO.getAddressCity());
                dto.setAddressState(orgDTO.getAddressState());
                dto.setAddressZip(orgDTO.getAddressZip());
                dto.setPhone(orgDTO.getPhone());
                dto.setPrimaryContactPhone(orgDTO.getPrimaryContactPhone());
                dto.setPrimaryContactName(orgDTO.getPrimaryContactName());
                dto.setPrimaryContactEmail(orgDTO.getPrimaryContactEmail());
                dto.setPrimaryContactRole(orgDTO.getPrimaryContactRole());
                retLst.add(dto);
            }

            return retLst;
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "findAllOrganizations", e.getMessage(), e);
            throw new UseCaseException(e.getMessage());
        }
    }
}
