package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.impl;

import com.diffplug.common.base.Errors;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.MeasureDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationMeasureDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationMeasureDetailDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.shared.dto.shape.MeasureDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDetailDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsibility: Create a list of OrganizationMeasureDetailDTO from the Organization Measure Entity
 * and the Measure Entity</br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 5/25/17
 */
@Stateless
public class OrganizationMeasureDetailDAOImpl implements OrganizationMeasureDetailDAO {
    protected Logger log = Logger.getLogger(this.getClass().getName());

    @EJB
    private OrganizationMeasureDAO organizationMeasureDAO;

    @EJB
    private MeasureDAO measureDAO;

    public OrganizationMeasureDetailDAOImpl() {
    }

    @Override
    public List<OrganizationMeasureDetailDTO> findAllOrganizationMeasureDetailByOrgId(int orgId) throws DataAccessException {
        try {

            // Find the organizations
            List<OrganizationMeasureDTO> lOrgs = organizationMeasureDAO.findAllOrganizationMeasureByOrgId(orgId);

            // Create Organization Measure DTOs
            List<OrganizationMeasureDetailDTO> retLst = new ArrayList<>();
            if (lOrgs != null && lOrgs.size() > 0) {
                lOrgs.forEach(Errors.rethrow().wrap(om -> {
                    OrganizationMeasureDetailDTO dto = new OrganizationMeasureDetailDTO();

                    // Find org data
                    dto.setOrgData(om);

                    // Find measure data
                    MeasureDTO mDto = measureDAO.findById(om.getMeasureId());
                    dto.setMeasureInfo(mDto);

                    retLst.add(dto);
                }));
            }


            return retLst;
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "findAllOrganizationMeasureByOrgId", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }
}
