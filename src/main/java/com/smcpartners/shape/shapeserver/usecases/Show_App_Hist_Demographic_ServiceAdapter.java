package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.MeasureDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationMeasureDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Show_App_Hist_Demographic_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.MeasureDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.response.AppHistDemographicsDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:<br/>
 * 1. Formats the demographic information to align with Google Charts Polymer element that uses Google Charts API.
 * If there is no data for a specific demographic, it will send back a false for the front end to display a message of
 * no data available. Formats into two decimal percentage.
 * <p>
 * Created by bryanhokanson on 12/17/15.
 * <p>
 * Changes:<b/>
 */
@Path("/common")
public class Show_App_Hist_Demographic_ServiceAdapter implements Show_App_Hist_Demographic_Service {

    @Inject
    private Logger log;

    @EJB
    private OrganizationMeasureDAO organizationMeasureDAO;

    @EJB
    private UserDAO userDAO;

    @EJB
    private MeasureDAO mDAO;

    @EJB
    private OrganizationMeasureDAO orgMDAO;

    @EJB
    private OrganizationDAO oDAO;

    @Inject
    private UserExtras userExtras;

    /**
     * Default Constructor
     */
    public Show_App_Hist_Demographic_ServiceAdapter() {
    }

    @Override
    @GET
    @NoCache
    @Path("/show/appHistDemographic/{orgId}/{measureId}/{year}")
    @Produces("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.DPH_USER, SecurityRoleEnum.ORG_ADMIN, SecurityRoleEnum.REGISTERED})
    @Logged
    public List<AppHistDemographicsDTO> showAppHistDemographic(int orgId, int measureId, int year) throws UseCaseException {
        try{

            List<AppHistDemographicsDTO> retLst = new ArrayList<>();

            MeasureDTO mdto = mDAO.findById(measureId);

            List<OrganizationMeasureDTO> orgMList = orgMDAO.findOrgMeasureByMeasureIdAndYear(measureId, year);

            if ( orgMList.size() > 0) {
                for (OrganizationMeasureDTO om : orgMList) {
                    OrganizationDTO org = oDAO.findById(orgId);

                    if (om.getOrganizationId() == org.getId()) {

                        AppHistDemographicsDTO ahdDTO = new AppHistDemographicsDTO();

                        //add measure info to dto
                        ahdDTO.setNqfId(mdto.getNqfId());
                        ahdDTO.setName(mdto.getName());
                        ahdDTO.setDescription(mdto.getDescription());
                        ahdDTO.setReportPeriodYear(om.getReportPeriodYear());


                        List<Object> africanAmericanList = new ArrayList<>();
                        List<Object> nativeAmericanList = new ArrayList<>();
                        List<Object> asianList = new ArrayList<>();
                        List<Object> nativeHawaiianList = new ArrayList<>();
                        List<Object> whiteList = new ArrayList<>();
                        List<Object> otherRaceList = new ArrayList<>();
                        List<Object> age18to44List = new ArrayList<>();
                        List<Object> age45to64List = new ArrayList<>();
                        List<Object> age65PlusList = new ArrayList<>();
                        List<Object> hispanicList = new ArrayList<>();
                        List<Object> notHispanicList = new ArrayList<>();
                        List<Object> femaleList = new ArrayList<>();
                        List<Object> maleList = new ArrayList<>();


                        try {
                            //add to the race lists
                            africanAmericanList.add("African American/Black");
                            africanAmericanList.add(convertToDoubles(om.getRaceAfricanAmericanNum(),
                                    om.getRaceAfricanAmericanDen()));

                            nativeAmericanList.add("American Indian or Alaskan Native");
                            nativeAmericanList.add(convertToDoubles(om.getRaceAmericanIndianNum(),
                                    om.getRaceAmericanIndianDen()));

                            asianList.add("Asian");
                            asianList.add(convertToDoubles(om.getRaceAsianNum(), om.getRaceAsianDen()));

                            nativeHawaiianList.add("Native Hawaiian or Pacific Islander");
                            nativeHawaiianList.add(convertToDoubles(om.getRaceNativeHawaiianNum(),
                                    om.getRaceNativeHawaiianDen()));

                            whiteList.add("White");
                            whiteList.add(convertToDoubles(om.getRaceWhiteNum(), om.getRaceWhiteDen()));

                            otherRaceList.add("Other");
                            otherRaceList.add(convertToDoubles(om.getRaceOtherNum(), om.getRaceOtherDen()));

                            //add arrays to raceData double array
                            List<List<Object>> rdList = new ArrayList<>();
                            rdList.add(africanAmericanList);
                            rdList.add(nativeAmericanList);
                            rdList.add(asianList);
                            rdList.add(nativeHawaiianList);
                            rdList.add(whiteList);
                            rdList.add(otherRaceList);
                            //set gender data on dto
                            ahdDTO.setRaceData(rdList);
                        } catch (Exception e) {
                            List<List<Object>> rdList = new ArrayList<>();
                            List<Object> errorList = new ArrayList<>();
                            errorList.add(false);
                            rdList.add(errorList);
                            ahdDTO.setRaceData(rdList);
                        }

                        try {

                            //add to age lists
                            age18to44List.add("18-44");
                            age18to44List.add(convertToDoubles(om.getAge1844Num(), om.getAge1844Den()));

                            age45to64List.add("45-64");
                            age45to64List.add(convertToDoubles(om.getAge4564Num(), om.getAge4564Den()));

                            age65PlusList.add("65+");
                            age65PlusList.add(convertToDoubles(om.getAgeOver65Num(), om.getAgeOver65Den()));

                            //add arrays to ageData double array
                            List<List<Object>> adList = new ArrayList<>();
                            adList.add(age18to44List);
                            adList.add(age45to64List);
                            adList.add(age65PlusList);
                            //set age data on dto
                            ahdDTO.setAgeData(adList);

                        } catch (Exception e) {
                            List<List<Object>> rdList = new ArrayList<>();
                            List<Object> errorList = new ArrayList<>();
                            errorList.add(false);
                            rdList.add(errorList);
                            ahdDTO.setAgeData(rdList);
                        }


                        try {
                            //add to ethnicity data lists
                            hispanicList.add("Hispanic/Latino");
                            hispanicList.add(convertToDoubles(om.getEthnicityHispanicLatinoNum(),
                                    om.getEthnicityHispanicLatinoDen()));


                            notHispanicList.add("Not Hispanic/Latino");
                            notHispanicList.add(convertToDoubles(om.getEthnicityNotHispanicLatinoNum(),
                                    om.getEthnicityNotHispanicLatinoDen()));

                            //add arrays to ethnicityData double array
                            List<List<Object>> ethList = new ArrayList<>();
                            ethList.add(hispanicList);
                            ethList.add(notHispanicList);
                            //set ethnicity data on dto
                            ahdDTO.setEthnicityData(ethList);
                        } catch (Exception e) {
                            List<List<Object>> rdList = new ArrayList<>();
                            List<Object> errorList = new ArrayList<>();
                            errorList.add(false);
                            rdList.add(errorList);
                            ahdDTO.setEthnicityData(rdList);
                        }

                        try {
                            //add to gender list
                            femaleList.add("Female");
                            femaleList.add(convertToDoubles(om.getGenderFemaleNum(), om.getGenderFemaleDen()));

                            maleList.add("Male");
                            maleList.add(convertToDoubles(om.getGenderMaleNum(), om.getGenderMaleDen()));

                            //add arrays to genderData double Array
                            List<List<Object>> gList = new ArrayList<>();
                            gList.add(femaleList);
                            gList.add(maleList);
                            //set gender data on dto
                            ahdDTO.setGenderData(gList);
                        } catch (Exception e) {
                            List<List<Object>> rdList = new ArrayList<>();
                            List<Object> errorList = new ArrayList<>();
                            errorList.add(false);
                            rdList.add(errorList);
                            ahdDTO.setGenderData(rdList);
                        }
                        retLst.add(ahdDTO);
                    }
                }
            }

            return retLst;

        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "showAppHistDemographic", e.getMessage(), e);
            throw new UseCaseException(e.getMessage());
        }

    }

    public double convertToDoubles(int numVal, int denVal) {
        double num = (double)numVal;
        double den = (double)denVal;
        double sum = num/den;
        if (Double.isNaN(sum)){
            sum = 0.00;
        }
        String df = new DecimalFormat("#.##").format(sum);
        sum = Double.parseDouble(df);
        return sum;
    }



}