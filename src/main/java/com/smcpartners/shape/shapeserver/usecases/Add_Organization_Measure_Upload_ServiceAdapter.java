package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.MeasureDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationMeasureFileUploadProcessorDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationMeasureDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Add_Organization_Measure_Upload_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.MeasureDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.OrganizationMeasureFileUploadRequestDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.response.FileUploadResponseDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.MaxFileSizeExceededException;
import com.smcpartners.shape.shapeserver.shared.exceptions.NotAuthorizedToPerformActionException;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import com.smcpartners.shape.shapeserver.shared.utils.ExcelUtils;
import com.smcpartners.shape.shapeserver.usecases.helpers.UploadDownLoadHelpers;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MultivaluedMap;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible: Upload a file with organization measure data and create a new organization measure from it<br/>
 * 1. Upload a file that contains a measure. All files will be in a consistent excel format.</br>
 * 2. A record of the file processing, including the file contents must be saved.</br>
 * 3. The data in the file will create an organizational measure</br>
 * 4. Only user role ADMIN or ORG_ADMIN can perform this task</br>
 * 5. Assumes only single file upload</br>
 * 6. can only have one measure type per organization per year.</br>
 * <p>
 * Created by johndestefano on 05/09/2016.
 * <p>
 * Changes:<b/>
 * 1. Additional data for file upload table and measure/year/org check - johndestefano - 07/20/2017</br>
 */
@Path("/admin")
public class Add_Organization_Measure_Upload_ServiceAdapter implements Add_Organization_Measure_Upload_Service {

    @Inject
    private Logger log;

    @EJB
    OrganizationMeasureFileUploadProcessorDAO fileUploadProcessorDAO;

    @EJB
    OrganizationMeasureDAO organizationMeasureDAO;

    @EJB
    MeasureDAO measureDAO;

    @Inject
    UserExtras userExtras;

    @Inject
    UploadDownLoadHelpers uploadDownLoadHelpers;

    @Inject
    @ConfigurationValue("com.smc.server-core.fileUpload.measureFileUploadKey")
    String formFileUploadKey;

    @Inject
    @ConfigurationValue("com.smc.server-core.fileUpload.maxUploadSize")
    long maxUploadSize;

    @Inject
    @ConfigurationValue("com.smc.server-core.errorMsgs.onlyOneMeasureTypePerOrgPerYearError")
    String onlyOneMeasureTypePerOrgPerYearError;

    @Inject
    @ConfigurationValue("com.smc.server-core.errorMsgs.canOnlyHaveOneActiveMeasureWithAGivenNameError")
    String canOnlyHaveOneActiveMeasureWithAGivenNameError;


    /**
     * Default constructor
     */
    public Add_Organization_Measure_Upload_ServiceAdapter() {
    }

    @Override
    @POST
    @Path("/measure/add/upload")
    @Produces("application/json")
    @Consumes("multipart/form-data")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.ORG_ADMIN})
    @Logged
    public FileUploadResponseDTO addMeasureUpload(MultipartFormDataInput input,
                                                  @HeaderParam("content-length") long contentLength) throws UseCaseException {
        try {
            // Check maximum content length
            uploadDownLoadHelpers.checkContentLength(maxUploadSize, contentLength);

            // Create the return object
            final FileUploadResponseDTO retDTO = new FileUploadResponseDTO();

            // Get a list of uploaded files to process from the request
            Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
            List<InputPart> inputParts = uploadForm.get(formFileUploadKey);

            // Establish organization id
            // Only roles that have access to this
            // resource are ADMIN and ORG_ADMIN
            int orgId;
            if (userExtras.getRole() == SecurityRoleEnum.ADMIN) {
                String orgIdStr = input.getFormDataPart("orgId", String.class, null);
                orgId = Integer.parseInt(orgIdStr);
            } else {
                orgId = userExtras.getOrgId();
            }

            // The current file name being processed
            String fileName = null;

            // Loop through the list and process
            ByteArrayInputStream in = null;
            XSSFWorkbook wb = null;
            for (InputPart inputPart : inputParts) {
                try {
                    // Get file name and bytes
                    MultivaluedMap<String, String> header = inputPart.getHeaders();
                    fileName = uploadDownLoadHelpers.getFileName(header);

                    //convert the uploaded file to input stream
                    InputStream inputStream = inputPart.getBody(InputStream.class, null);
                    byte[] bytes = IOUtils.toByteArray(inputStream);

                    // Save b64 version of later use
                    String base64FileContents = Base64.encodeBase64String(bytes);

                    // Read sheet data into memory
                    in = new ByteArrayInputStream(bytes);
                    wb = new XSSFWorkbook(in);
                    Sheet sheet = wb.getSheetAt(0);

                    // Get the measure name string
                    // This will be used by the data layer to look up the
                    // measure and get its id. This is needed for the creation
                    // of a new organization measure.
                    Row row1 = sheet.getRow(0);
                    Cell measureIdCell = row1.getCell(1);
                    String measureName = measureIdCell.getRichStringCellValue().getString();

                    // Enforce rule of only one measure id/organization/year
                    int measureId;
                    int rptYear = ExcelUtils.convertToInt(sheet, 4, 1);
                    List<MeasureDTO> measures = measureDAO.findActiveMeasuresByName(measureName);
                    if (measures.size() > 1) {
                        // Don't see how this could happen other than direct manipulation
                        // of the database as its checked when a new measure is created.
                        throw new Exception(canOnlyHaveOneActiveMeasureWithAGivenNameError);
                    } else {
                        measureId = measures.get(0).getId();
                    }

                    if (organizationMeasureDAO.checkMeasureForYearForOrgAlreadyEntered(measureId, orgId, rptYear)) {
                        throw new Exception(onlyOneMeasureTypePerOrgPerYearError);
                    }

                    // Extract the data and submit for database load
                    // Create a measure entry using the users org id and save the
                    // entry in the file upload table
                    Date now = new Date();
                    OrganizationMeasureDTO organizationMeasureDTO = new OrganizationMeasureDTO();
                    OrganizationMeasureFileUploadRequestDTO fileUploadRequestDTO = new OrganizationMeasureFileUploadRequestDTO();

                    // File Upload Data
                    fileUploadRequestDTO.setUploadDt(now);
                    fileUploadRequestDTO.setUploadedB64File(base64FileContents);
                    fileUploadRequestDTO.setUserId(userExtras.getUserId());
                    fileUploadRequestDTO.setMeasureEntityName(measureName);
                    fileUploadRequestDTO.setMeasureEntityId(measureId);
                    fileUploadRequestDTO.setOrgId(orgId);
                    fileUploadRequestDTO.setFileName(formFileUploadKey);

                    // Organization measure data
                    organizationMeasureDTO.setRpDate(now);
                    organizationMeasureDTO.setFileUploadDate(now);
                    organizationMeasureDTO.setUserId(userExtras.getUserId());
                    organizationMeasureDTO.setOrganizationId(orgId);
                    organizationMeasureDTO.setNumeratorValue(ExcelUtils.convertToInt(sheet, 1, 1));
                    organizationMeasureDTO.setDenominatorValue(ExcelUtils.convertToInt(sheet, 2, 1));
                    organizationMeasureDTO.setReportPeriodQuarter(ExcelUtils.convertToInt(sheet, 3, 1));
                    organizationMeasureDTO.setReportPeriodYear(rptYear);
                    organizationMeasureDTO.setGenderMaleNum(ExcelUtils.convertToInt(sheet, 5, 1));
                    organizationMeasureDTO.setGenderMaleDen(ExcelUtils.convertToInt(sheet, 6, 1));
                    organizationMeasureDTO.setGenderFemaleNum(ExcelUtils.convertToInt(sheet, 7, 1));
                    organizationMeasureDTO.setGenderFemaleDen(ExcelUtils.convertToInt(sheet, 8, 1));
                    organizationMeasureDTO.setAge1844Num(ExcelUtils.convertToInt(sheet, 9, 1));
                    organizationMeasureDTO.setAge1844Den(ExcelUtils.convertToInt(sheet, 10, 1));
                    organizationMeasureDTO.setAge4564Num(ExcelUtils.convertToInt(sheet, 11, 1));
                    organizationMeasureDTO.setAge4564Den(ExcelUtils.convertToInt(sheet, 12, 1));
                    organizationMeasureDTO.setAgeOver65Num(ExcelUtils.convertToInt(sheet, 13, 1));
                    organizationMeasureDTO.setAgeOver65Den(ExcelUtils.convertToInt(sheet, 14, 1));
                    organizationMeasureDTO.setEthnicityHispanicLatinoNum(ExcelUtils.convertToInt(sheet, 15, 1));
                    organizationMeasureDTO.setEthnicityHispanicLatinoDen(ExcelUtils.convertToInt(sheet, 16, 1));
                    organizationMeasureDTO.setEthnicityNotHispanicLatinoNum(ExcelUtils.convertToInt(sheet, 17, 1));
                    organizationMeasureDTO.setEthnicityNotHispanicLatinoDen(ExcelUtils.convertToInt(sheet, 18, 1));
                    organizationMeasureDTO.setRaceAfricanAmericanNum(ExcelUtils.convertToInt(sheet, 19, 1));
                    organizationMeasureDTO.setRaceAfricanAmericanDen(ExcelUtils.convertToInt(sheet, 20, 1));
                    organizationMeasureDTO.setRaceAmericanIndianNum(ExcelUtils.convertToInt(sheet, 21, 1));
                    organizationMeasureDTO.setRaceAmericanIndianDen(ExcelUtils.convertToInt(sheet, 22, 1));
                    organizationMeasureDTO.setRaceAsianNum(ExcelUtils.convertToInt(sheet, 23, 1));
                    organizationMeasureDTO.setRaceAsianDen(ExcelUtils.convertToInt(sheet, 24, 1));
                    organizationMeasureDTO.setRaceNativeHawaiianNum(ExcelUtils.convertToInt(sheet, 25, 1));
                    organizationMeasureDTO.setRaceNativeHawaiianDen(ExcelUtils.convertToInt(sheet, 26, 1));
                    organizationMeasureDTO.setRaceWhiteNum(ExcelUtils.convertToInt(sheet, 27, 1));
                    organizationMeasureDTO.setRaceWhiteDen(ExcelUtils.convertToInt(sheet, 28, 1));
                    organizationMeasureDTO.setRaceOtherNum(ExcelUtils.convertToInt(sheet, 29, 1));
                    organizationMeasureDTO.setRaceOtherDen(ExcelUtils.convertToInt(sheet, 30, 1));

                    // Save the results of the above processing
                    BooleanValueDTO dto = fileUploadProcessorDAO.createAndLogFileMeasureUpload(organizationMeasureDTO,
                            fileUploadRequestDTO);

                    // Save entry in return map
                    retDTO.addToMap(fileName, dto.isValue() ? "succeeded" : "false");

                } catch (Exception e) {
                    log.logp(Level.SEVERE, this.getClass().getName(), "addMeasureUpload - file", e.getMessage(), e);

                    // On errors record the files name and failed
                    retDTO.addToMap(fileName, "failed");
                } finally {
                    if (in != null) {
                        in.close();
                    }

                    if (wb != null) {
                        wb.close();
                    }
                }
            }

            return retDTO;
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "addMeasureUpload", e.getMessage(), e);
            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException) e;
            } else if (e instanceof MaxFileSizeExceededException) {
                throw (MaxFileSizeExceededException) e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }
}
